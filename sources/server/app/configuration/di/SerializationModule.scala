package configuration.di

import scaldi.Module
import serialization._

class SerializationModule extends Module {
  binding to new SystemUserSerializer
  binding to new OperationSerializer
  binding to new CarSerializer
  binding to new DriverSerializer
  binding to new RentSerializer
}
