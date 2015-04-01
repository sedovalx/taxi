/**
 * Created by Кирилл on 18.03.2015.
 */
import Ember from "ember";

export default Ember.Component.extend({
  displayName: null,
  didInsertElement: function(){
    this.setDisplayName();
  },
  setDisplayName: function(){
    let currentUser = this.get("session").get("currentUser");
    if (currentUser) {
      var login = currentUser.get("login");
      var lastName = currentUser.get("lastName");
      var firstName = currentUser.get("firstName");
      this.set("displayName", (lastName != null && firstName != null) ? firstName + ' ' + lastName : login);
    }
  }.observes("session.currentUser.lastName", "session.currentUser.firstName"),
  actions: {
    invalidateSession: function(){
      this.get("session").invalidate();
    }
  }
});
