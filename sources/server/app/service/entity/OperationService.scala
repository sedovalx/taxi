package service.entity

import javax.inject.Inject

import models.generated.Tables.{Operation, OperationFilter, OperationTable}
import repository.OperationRepo

trait OperationService extends EntityService[Operation, OperationTable, OperationRepo, OperationFilter]

class OperationServiceImpl @Inject() (val repo: OperationRepo) extends OperationService

