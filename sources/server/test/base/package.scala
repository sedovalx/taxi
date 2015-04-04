import play.api.db.BoneCPPlugin

/**
 * Created by ipopkov on 04/04/15.
 */
package object base {


}

package play.api.db {

class RestartableBoneCPPlugin(app: play.api.Application) extends BoneCPPlugin(app) {
  override def onStop() {}
}

}