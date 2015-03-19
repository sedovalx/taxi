/**
 * Created by Кирилл on 18.03.2015.
 */
import Ember from "ember";
import $ from "jquery";

export default Ember.Component.extend({
  didInsertElement: function() {
    var component = this;
    var username = $.getJSON("/auth/current", function(json) {
      var login = json.user.login;
      var lastname = json.user.lastName;
      var firstName = json.user.firstName;
      var middleName = json.user.middleName;
      if (lastname!==null && firstName !==null){
        var displayName = firstName + ' ' + lastname;
      }
      else {
        var displayName = login;
      }
      component.set('displayName',displayName);
    });
  },
  displayName: null
});
