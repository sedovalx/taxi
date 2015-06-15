with rent_last_status as
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
select
  r.*,
  rls.status,
  c.model || ' ' || c.reg_number || ' (' || c.rate || ')' as "carDisplayName",
  d.last_name || ' ' || d.first_name || coalesce(' ' || d.middle_name, '') as "driverDisplayName"
from rent r
  join car c on c.id = r.car_id
  join driver d on d.id = r.driver_id
  left join rent_last_status rls on rls.rent_id = r.id
  --WHERE_CLAUSE_#1
order by r.creation_date