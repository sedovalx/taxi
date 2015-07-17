package service.entity

import com.google.inject.Inject
import models.generated.Tables.{Refund, RefundFilter, RefundTable}
import repository.RefundRepo

trait RefundService extends EntityService[Refund, RefundTable, RefundRepo, RefundFilter]

class RefundServiceImpl @Inject() (val repo: RefundRepo) extends RefundService
