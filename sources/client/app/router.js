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
    this.route("new");
    this.route("edit", { path: "/:rent_id/edit" });
    this.route("newPayment", { path: "/:rent_id/payments/new"});
  });
  this.resource("payments", function() {
    this.route("new");
    this.route("edit", { path: "/:payment_id/edit" });
  });
  this.resource("fines", function() {
    this.route("new");
    this.route("edit", { path: "/:fine_id/edit" });
  });
  this.resource("repairs", function() {
    this.route("new");
    this.route("edit", { path: "/:repair_id/edit" });
  });
  this.resource("expenses", function() {
    this.route("new");
    this.route("edit", { path: "/:expense_id/edit" });
  });
  this.resource("cashier-list");
  this.route('login');
});


export default Router;
