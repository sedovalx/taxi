import Ember from "ember";
import Session from "simple-auth/session";

export default {
  name: "custom-session",
  before: "simple-auth",
  initialize: function(container) {
    Session.reopen({
      setCurrentUser: function() {
        var id = this.get("userId");
        var self = this;

        if (!Ember.isEmpty(id)) {
          return container.lookup("store:main").find("user", id).then(function(user) {
            self.set("currentUser", user);
          });
        }
      }.observes("user_id")
    });
  }
};
