select
	rent_id,
	minutes,
	payments,
	repairs,
	fines,
	deposit,
	payments + repairs + fines + deposit as "total"
from func_rent_balances('@controlTime')
where rent_id = @rentId
