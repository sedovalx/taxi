/*
variables:
  - @control_date timestamp
*/
with status_finish_time as (
	select
		*,
		case 
			when t.finish_time_t is null then @control_date
			when @control_date is not null and t.finish_time_t > @control_date then @control_date
			else t.finish_time_t
		end as "finish_time"
	from (
		select 
			*,
			lead(rs.change_time) over (partition by rs.rent_id order by rs.change_time) as "finish_time_t"
		from rent_status rs
	) t
)
, rent_status_view as (
	select 
		*,
		case 
			when t.finish_time <= t.change_time then 0
			else (DATE_PART('day', t.finish_time - t.change_time) * 24 + 
				DATE_PART('hour', t.finish_time - t.change_time)) * 60 +
				DATE_PART('minute', t.finish_time - t.change_time)
		end as diff_in_min
	from status_finish_time t
)
, rent_last_status as
(
	select 
	*
	from 
	(
		select 
			rs.*,
			rank() over (partition by rs.rent_id order by rs.change_time desc) as rk
		from rent_status rs
	) t
	where t.rk = 1
)
, rent_payments as (
	select
		r.id,
		sum(coalesce(p.amount, 0)) as "payments"
	from rent r
	left join payment p on p.rent_id = r.id and p.creation_date < @control_date
	group by r.id
)
, rent_fines as (
	select
		r.id,
		sum(coalesce(p.amount, 0)) as "fines"
	from rent r
	left join fine p on p.rent_id = r.id and p.creation_date < @control_date
	group by r.id
)
, rent_repairs as (
	select
		r.id,
		sum(coalesce(p.amount, 0)) as "repairs"
	from rent r
	left join repair p on p.rent_id = r.id and p.creation_date < @control_date
	group by r.id
)
, rent_minutes as (
	select 
		r.id, 
		sum(coalesce(rsv.diff_in_min, 0)) as "active_minutes"
	from rent r
	left join rent_status_view rsv on r.id = rsv.rent_id and rsv.status = 'Active'
	group by r.id
)
, rent_view as 
(
	select 
		r.id, 
		r.driver_id, 
		r.car_id, 
		r.deposit, 
		r.creation_date,
		rls.status,
		rm.active_minutes,
		rp.payments,
		rf.fines,
		rr.repairs
	from rent r
	join rent_minutes rm on rm.id = r.id
	join rent_payments rp on rp.id = r.id
	join rent_fines rf on rf.id = r.id
	join rent_repairs rr on rr.id = r.id
	left join rent_last_status rls on r.id = rls.rent_id
)
, car_last_rent as 
(
	select 
	*
	from
	(
		select
			r.*,
			rank() over (partition by r.car_id order by r.creation_date) as rk
		from rent_view r
	) t
	where t.rk = 1
)
select
	c.id as "car_id",
	c.make || ' ' || c.reg_number || ' (' || c.rate || ')' as "car",
	r.id as "rent_id",
	r.last_name || ' ' || r.first_name || coalesce(' ' || r.middle_name, '') as "driver",
	coalesce(p.presence, false) as "presence",
	r.repairs,
	r.fines,
	r.deposit + r.payments - r.fines - r.repairs - r.active_minutes*(c.rate/24/60) as "balance",
	c.mileage,
	c.service,
	r.status
from car c
left join (
	select
		clr.*,
		d.last_name,
		d.first_name,
		d.middle_name
	from car_last_rent clr 
	join driver d on clr.driver_id = d.id
) r on r.car_id = c.id and r.status <> 'Closed'
left join payment p on p.rent_id = r.id and p.change_time::date = @control_date::date
order by r.status, r.creation_date, c.reg_number