import DS from "ember-data";

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
  editor: DS.belongsTo("user", {inverse: null, async: true})
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
