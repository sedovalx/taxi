select
	rent_id,
	minutes,
	rent_balance as "rent",
	repairs,
	fines,
	deposit,
	rent_balance + repairs + fines + deposit as "total"
from func_rent_balances('@controlTime')
where rent_id = @rentId
