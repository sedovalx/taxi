package controllers

import models.Cars
import play.api.mvc.{Action, Controller}

object CarController extends Controller{
  def cars = Action {
    Ok(Cars.getAll.toString())
  }
}
