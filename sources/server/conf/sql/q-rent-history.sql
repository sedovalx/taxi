select
  rs.change_time as status_time,
  rs.status,
  rs.id as status_id,
  o.change_time as operation_time,
  o.id as operation_id,
  o.amount as amount
from rent_status rs
  left join operation o on rs.rent_id = o.rent_id
  where rs.rent_id = @rentId and rs.creation_date < '@controlTime' and o.creation_date < '@controlTime'
order by rs.change_time, o.change_time
