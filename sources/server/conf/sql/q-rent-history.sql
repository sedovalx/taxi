with statuses as (
	select
		s.change_time,
		s.creation_date,
		s.status,
		s.id,
		s.rent_id,
		coalesce(s.end_time, '@controlTime') as end_time
	from status_with_bounds s
)
select
  rs.change_time as status_time,
  rs.status,
  rs.id as status_id,
  o.change_time as operation_time,
  o.account_type,
  o.amount as amount,
  o.id as operation_id
from statuses rs
left join operation o on rs.rent_id = o.rent_id and (o.change_time >= rs.change_time and o.change_time < rs.end_time)
where rs.rent_id = @rentId and rs.creation_date < '@controlTime' and o.creation_date < '@controlTime'
order by rs.change_time, o.change_time
