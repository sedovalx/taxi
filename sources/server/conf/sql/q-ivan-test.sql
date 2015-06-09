select
  rs.change_time,
  lead(rs.change_time) over (partition by rs.rent_id order by rs.change_time) as "finish_time_t"
from rent_status rs