package configuration.di

import repository._

import scaldi.Module


/**
 * Created by ipopkov on 15/04/15.
 */
class RepoModule extends Module{
  bind [AccountRepo] to new AccountRepoImpl()
  bind [DriverRepo] to new DriverRepoImpl()
  bind [CarRepo] to new CarRepoImpl
  bind [RentRepo] to new RentRepoImpl
  bind [RentStatusRepo] to new RentStatusRepoImpl
}
