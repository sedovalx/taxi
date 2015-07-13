import Ember from 'ember';
import config from './config/environment';

let Router = Ember.Router.extend({
  location: config.locationType
});

Router.map(function() {
  this.resource("users", function(){
    this.route("new");
    this.route("edit", { path: "/:user_id/edit" });
  });
  this.resource("drivers", function() {
    this.route("new");
    this.route("edit", { path: "/:driver_id/edit" });
  });
  this.resource("cars", function() {
    this.route("new");
    this.route("edit", { path: "/:car_id/edit" });
  });
  this.resource("rents", function() {
    this.resource("rent", { path: "/:rent_id" }, function(){
      this.route("edit");
      this.resource("operations", function() {
        this.route("new", { path: "/:operationType"});
        this.route("edit", { path: "/:operation_id/edit" });
      });
    });
    this.route("new");
  });
  this.resource("expenses", function() {
    this.route("new");
    this.route("edit", { path: "/:expense_id/edit" });
  });
  this.resource("cashier-list", function() {
    this.resource("test.rents",{ path: "cars/:car_id/rents" }, function(){
      this.route("new");
    });
  });
  this.route('login');
});


export default Router;
