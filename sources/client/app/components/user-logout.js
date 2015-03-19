/**
 * Created by Кирилл on 18.03.2015.
 */
import Ember from "ember";
import $ from "jquery";

export default Ember.Component.extend({
  didInsertElement: function() {
    var component = this;
    $.getJSON("/auth/current", function(json) {
      var login = json.user.login;
      var lastName = json.user.lastName;
      var firstName = json.user.firstName;
      var displayName = (lastName !== null && firstName !== null) ? firstName + ' ' + lastName : login;
      component.set('displayName', displayName);
    });
  },
  displayName: null
});
