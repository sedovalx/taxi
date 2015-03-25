import Ember from "ember";

export default Ember.Controller.extend({
  actions: {
    login: function(){
      let credentials = {
        login: this.get("login"),
        password: this.get("password")
      };
      Ember.$.post("/auth/json", credentials).then(function(response){

      });
    }
  }
});
