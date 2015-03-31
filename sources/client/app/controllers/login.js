import Ember from "ember";
import LoginControllerMixin from 'simple-auth/mixins/login-controller-mixin';

export default Ember.Controller.extend(LoginControllerMixin, {
  authenticator: 'authenticator:jwt-custom',
  errorMessage: null,
  inProcess: false,
  actions: {
    authenticate: function(){
      let that = this;
      that.set("errorMessage", null);
      that.set("inProcess", true);
      return this._super().then(function(){
        that.set("inProcess", false);
      }, function(error){
        that.set("errorMessage", error.message || "Ошибка аутентификации.");
        that.set("inProcess", false);
      });
    }
  }
});
