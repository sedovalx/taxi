package service

import models.generated.Tables.{PaymentFilter, Payment, PaymentTable}
import repository.PaymentRepo

trait PaymentService extends EntityService[Payment, PaymentTable, PaymentRepo, PaymentFilter]

