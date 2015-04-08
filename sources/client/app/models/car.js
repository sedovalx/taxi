import DS from "ember-data";

let attr = DS.attr;
var CarsModel = DS.Model.extend({
  regNumber: attr("string"),
  cmodel: attr("string"),
  make: attr("string"),
  mileage: attr("number"),
  service: attr("number"),
  classID: attr("number"),
  comment: attr("string"),
  creationDate: attr("date"),
  editDate: attr("date"),
  creator_id: DS.belongsTo("user", {inverse: null, async: true}),
  editor_id: DS.belongsTo("user", {inverse: null, async: true})
});


CarsModel.reopenClass({
  FIXTURES: [
    {
      id: 1,
      regNumber: "АН555Р77",
      cmodel: "Focus",
      make: "Ford",
      mileage: 150000,
      service: 140000,
      classID: 5,
      comment: "test comment"
    },
    {
      id: 2,
      regNumber: "АН111Р77",
      cmodel: "X5",
      make: "BMW",
      mileage: 100000,
      service: 90000,
      classID: 5,
      comment: ""
    }
  ]
});


export default CarsModel;
