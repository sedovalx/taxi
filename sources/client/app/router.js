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
  this.resource("drivers", function() {
    this.route("new");
  });
  this.resource("driver", { path: "/drivers/:driver_id" }, function() {
    this.route("edit");
  });
  this.resource("cars", function() {
    this.route("new");
  });
  this.resource("car", { path: "/cars/:car_id" }, function() {
    this.route("edit");
  });
  this.resource("carclasses", function() {
    this.route("new");
  });
  this.resource("carclass", { path: "/carclasses/:carclass_id" }, function() {
    this.route("edit");
  });
  this.route('login');
});


export default Router;
