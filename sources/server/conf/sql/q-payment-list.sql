select
  p.id,
  p.change_time,
  p.amount,
  p.presence
from payment p
join rent r on p.rent_id = r.id
where r.id = @rent_id
order by r.creation_date, p.change_time