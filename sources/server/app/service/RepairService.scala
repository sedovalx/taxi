package service

import models.generated.Tables.{RepairFilter, Repair, RepairTable}
import repository.RepairRepo

trait RepairService extends EntityService[Repair, RepairTable, RepairRepo, RepairFilter]