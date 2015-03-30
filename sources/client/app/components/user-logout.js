/**
 * Created by Кирилл on 18.03.2015.
 */
import Ember from "ember";

export default Ember.Component.extend({
  displayName: function(){
    let currentUser = this.get("session").get("currentUser");
    if (currentUser) {
      var login = currentUser.get("login");
      var lastName = currentUser.get("lastName");
      var firstName = currentUser.get("firstName");
      return (lastName != null && firstName != null) ? firstName + ' ' + lastName : login;
    }
  }.property("session.currentUser")
});
