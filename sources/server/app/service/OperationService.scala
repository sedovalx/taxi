package service

import models.generated.Tables.{OperationFilter, Operation, OperationTable}
import repository.OperationRepo

trait OperationService extends EntityService[Operation, OperationTable, OperationRepo, OperationFilter]

