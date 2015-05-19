package repository

import models.generated.Tables.{RepairFilter, Repair, RepairTable}

trait RepairRepo extends GenericCRUD[Repair, RepairTable, RepairFilter]
