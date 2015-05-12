import Ember from "ember";
import DS from "ember-data";
import statuses from "client/models/rent-statuses";

let attr = DS.attr;
var RentModel = DS.Model.extend({
  driver: DS.belongsTo("driver", {inverse: null, async: true}),
  car: DS.belongsTo("car", {inverse: null, async: true}),
  deposit: attr("number"),
  status: attr("string"),
  comment: attr("string"),
  creationDate: attr("date"),
  editDate: attr("date"),
  creator: DS.belongsTo("user", {inverse: null, async: true}),
  editor: DS.belongsTo("user", {inverse: null, async: true}),
  displayStatus: function(){
    let status = statuses.filter(r => r.id === this.get("status"))[0];
    return status ? status.label : "";
  }.property("status"),
  driverDisplayName: function(){
    return this.get("driver").get("displayName");
  }.property("model","driver"),
  carDisplayName: function(){
    return this.get("car").get("displayName");
  }.property("model","car"),
  displayName: function(){
    //let model = this;
    ////let model = this.get("model");
    //return DS.PromiseObject.create({
    //  promise: model.get("driver").then(function(driver){
    //    return driver.get("id");
    //  })
    //});
    //return DS.PromiseObject.create({
    //  promise: Ember.RSVP.all([
    //    model.get("driver"),
    //    model.get("car"),
    //    this.get("driver"),
    //    this.get("car"),
    //    model.get("driver.id"),
    //    model.get("car.id")
    //  ]).then(objects => {
    //    let driver = objects[0];
    //    let car = objects[1];
    //    return driver.get("displayName") + " - " + car.get("make");
    //  })
    //});
    //return this.get("driver").then(function(driver){return driver.displayName;}) + " - " + this.get("car").then(function(car){return car.displayName;});
    //return this.get("driver").then(function(driver){return driver.get("displayName");}) + " - " + this.get("car").then(function(car){return car.get("displayName");});
    return this.get("driverDisplayName") + " - " + this.get("carDisplayName");
  }.property("driverDisplayName","carDisplayName")
});



RentModel.reopenClass({
  FIXTURES: [
    {
      id: 1,
      driver: 1,
      car: 1,
      deposit: 5000,
      status: "Active",
      comment: "comment 1"
    },
    {
      id: 2,
      driver: 4,
      car: 2,
      deposit: 15000,
      status: "Closed",
      comment: "comment 1"
    }
  ]
});


export default RentModel;
