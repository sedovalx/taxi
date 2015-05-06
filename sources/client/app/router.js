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
  this.resource("rents", function() {
    this.route("new");
  });
  this.resource("rent", { path: "/rents/:rent_id" }, function() {
    this.route("edit");
  });
  this.resource("payments", function() {
    this.route("new");
  });
  this.resource("payment", { path: "/payments/:payment_id" }, function() {
    this.route("edit");
  });
  this.resource("fines", function() {
    this.route("new");
  });
  this.resource("fine", { path: "/fines/:fine_id" }, function() {
    this.route("edit");
  });
  this.resource("repairs", function() {
    this.route("new");
  });
  this.resource("repair", { path: "/repairs/:repair_id" }, function() {
    this.route("edit");
  });
  this.route('login');
});


export default Router;
