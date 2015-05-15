package repository

import models.generated.Tables.{PaymentFilter, Payment, PaymentTable}

trait PaymentRepo extends GenericCRUD[Payment, PaymentTable, PaymentFilter]


