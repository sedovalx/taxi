package configuration.di

import models.generated.Tables._
import repository._

import scaldi.Module

class RepoModule extends Module{
  bind [AccountRepo] to new AccountRepoImpl
  bind [RentStatusRepo] to new RentStatusRepoImpl

  bind [DriverRepo] to new DriverRepo { val tableQuery = DriverTable }
  bind [CarRepo] to new CarRepo {val tableQuery = CarTable }
  bind [RentRepo] to new RentRepo { val tableQuery = RentTable }
  bind [PaymentRepo] to new PaymentRepo { val tableQuery = PaymentTable }
  bind [FineRepo] to new FineRepo { val tableQuery = FineTable }
}
