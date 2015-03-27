package controllers

import play.api.mvc.Controller
import utils.db.DbAccessor

/**
 * Базовый класс всех контроллеров приложения
 */
abstract class BaseController extends Controller with DbAccessor{

}
