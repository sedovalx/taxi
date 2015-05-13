package repository

import models.generated.Tables.{Repair, RepairTable}

trait RepairRepo extends GenericCRUD[Repair, RepairTable]
