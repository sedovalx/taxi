package controllers

import play.api.mvc.Controller
import repository.db.DbAccessor
import scaldi.{Injectable, Injector}

/**
 * Базовый класс всех контроллеров приложения
 */
abstract class BaseController extends Controller with Injectable {

}
