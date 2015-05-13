package repository

import models.generated.Tables.{Payment, PaymentTable}

trait PaymentRepo extends GenericCRUD[Payment, PaymentTable]


