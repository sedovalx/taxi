package service

import models.generated.Tables.{Payment, PaymentTable}
import repository.PaymentRepo

trait PaymentService extends EntityService[Payment, PaymentTable, PaymentRepo]

