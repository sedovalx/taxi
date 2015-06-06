import Ember from "ember";
import DS from "ember-data";
import BaseController from "client/controllers/base/base-controller";
import statuses from "client/models/rent-statuses";

export default BaseController.extend({
  statuses: statuses,
  selectedStatus: function(key, value){
    let model = this.get("model");
    if (!model) {
      return;
    }

    if (arguments.length > 1){
      model.set("status", value != null ? value.id : null);
    }
    return this.get("statuses").filter(r => r.id === model.get("status"))[0];
  }.property("model"),
  carItems: function() {
    return DS.PromiseArray.create({
      promise: Ember.RSVP.all([
        this.store.find("car"),
        this.store.find("rent")
      ]).then(arrays => {
        let cars = arrays[0];
        let rents = arrays[1];
        // выбираем все занятые в открытых арендах машины
        let busyCarIds = rents.filter(r => r.get("status") !== "Closed").map(r => r.get("car").get("id"));
        // из всех машин отфильтровываем свободные
        return cars.filter(c => busyCarIds.indexOf(c.get("id")) < 0);
      })
    });
  }.property("model"),
  driverItems: function() {
    //return this.store.find("driver");
    return DS.PromiseArray.create({
      promise: Ember.RSVP.all([
        this.store.find("driver"),
        this.store.find("rent")
      ]).then(arrays => {
        let drivers = arrays[0];
        let rents = arrays[1];
        // выбираем все занятые в открытых арендах водители
        let busyDriverIds = rents.filter(r => r.get("status") !== "Closed").map(r => r.get("driver").get("id"));
        // из всех водителей отфильтровываем свободные
        return drivers.filter(d => busyDriverIds.indexOf(d.get("id")) < 0);
      })
    });
  }.property("model")
});
