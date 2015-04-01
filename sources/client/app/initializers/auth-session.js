import Ember from "ember";
import Session from "simple-auth/session";

export default {
  name: "custom-session",
  before: "simple-auth",
  initialize: function(container) {
    Session.reopen({
      setCurrentUser: function() {
        var self = this;
        // если есть токен
        if (self.get("authenticator")){
          // спрашиваем у сервера, кто я такой
          Ember.$.getJSON("api/users/me", function(data){
            // и помещаем в хранилище
            let store = container.lookup('store:main');
            let user = store.push('user', data.user);
            self.set('currentUser', user);
          });
        }
      }.observes("authenticator")
    });
  }
};
