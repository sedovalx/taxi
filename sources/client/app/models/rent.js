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
    return status ? status.label : this.get("status");
  }.property("status"),
  driverDisplayName: function(){
    return this.get("driver").get("displayName");
  }.property("model","driver"),
  carDisplayName: function(){
    return this.get("car").get("displayName");
  }.property("model","car"),
  displayName: function(){
    return this.get("driverDisplayName") + " - " + this.get("carDisplayName");
  }.property("driverDisplayName","carDisplayName")
});

export default RentModel;
