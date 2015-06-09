package configuration.di

import models.generated.Tables._
import repository._

import scaldi.Module

class RepoModule extends Module{
  bind [SystemUserRepo] to new SystemUserRepoImpl
  bind [RentStatusRepo] to new RentStatusRepoImpl
  bind [OperationRepo] to new OperationRepoImpl

  bind [DriverRepo] to new DriverRepo { val tableQuery = DriverTable }
  bind [CarRepo] to new CarRepo {val tableQuery = CarTable }
  bind [RentRepo] to new RentRepo { val tableQuery = RentTable }
}
