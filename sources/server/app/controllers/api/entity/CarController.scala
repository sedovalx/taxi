package controllers.api.entity

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.generated.Tables
import models.generated.Tables.{Car, CarFilter, CarTable}
import play.api.i18n.MessagesApi
import repository.CarRepo
import serialization.entity.CarSerializer
import service.entity.CarService

class CarController @Inject() (
  val messagesApi: MessagesApi,
  val env: Environment[Tables.SystemUser, JWTAuthenticator],
  val entityService: CarService,
  val serializer: CarSerializer)
  extends EntityController[Car, CarTable, CarRepo, CarFilter, CarSerializer] {
  override protected val entitiesName: String = "cars"
  override protected val entityName: String = "car"
}
