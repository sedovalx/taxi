/**
 * Created by Кирилл on 18.03.2015.
 */
import Ember from "ember";
import $ from "jquery";

export default Ember.Component.extend({
  onInsert: function() {
    var component = this;
    var username;
    this.set('login',"testotron");
    username = $.getJSON("/auth/current", function(json) {
      var login = null;
      var lastname = null;
      var firstName = null;
      var middleName = null;
      var DisplayName = null;
      login = json.user.login;
      lastname = json.user.lastName;
      firstName = json.user.firstName;
      middleName = json.user.middleName;
      if (lastname!==null && firstName !==null){
        DisplayName = firstName + ' ' + lastname;
      }
      else {
        DisplayName = login;
      }
      component.set('DisplayName',DisplayName);
    });
  }.on('didInsertElement'),
  DisplayName: null
});
