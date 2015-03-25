import Ember from 'ember';
import config from './config/environment';

let Router = Ember.Router.extend({
  location: config.locationType
});

Router.map(function() {
  this.resource("users", function() {
    this.route("new");
  });
  this.resource("user", { path: "/users/:user_id" }, function() {
    this.route("edit");
  });
  this.resource("cars", function() {
    this.route("new");
  });
  this.resource("drivers", function() {
    this.route("new");
  });
});


export default Router;

