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
  displayName: function(){
    return this.get("driver").get("displayName") + " - " + this.get("car").get("displayName");
  }.property("driver","driver.displayName","car")
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
      driver: 2,
      car: 2,
      deposit: 15000,
      status: "Closed",
      comment: "comment 1"
    }
  ]
});


export default RentModel;
