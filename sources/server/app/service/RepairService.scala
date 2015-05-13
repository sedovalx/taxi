package service

import models.generated.Tables.{Repair, RepairTable}
import repository.RepairRepo

trait RepairService extends EntityService[Repair, RepairTable, RepairRepo]