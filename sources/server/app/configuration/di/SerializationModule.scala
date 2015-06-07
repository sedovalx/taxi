package configuration.di

import scaldi.Module
import serialization._

class SerializationModule extends Module {
  binding to new AccountSerializer
  binding to new PaymentSerializer
  binding to new CarSerializer
  binding to new DriverSerializer
  binding to new FineSerializer
  binding to new RentSerializer
  binding to new RepairSerializer
}
