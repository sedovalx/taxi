import DS from "ember-data";

let attr = DS.attr;
var CarsModel = DS.Model.extend({
  regNumber: attr("string"),
  cmodel: attr("string"),
  make: attr("string"),
  mileage: attr("number"),
  service: attr("number"),
  carClass: DS.belongsTo("car-class", {inverse: null, async: true}),
  comment: attr("string"),
  creationDate: attr("date"),
  editDate: attr("date"),
  creator: DS.belongsTo("user", {inverse: null, async: true}),
  editor: DS.belongsTo("user", {inverse: null, async: true}),
  displayName: function(){
    return this.get('make') + ' ' + this.get('cmodel') + ' ' + this.get('regNumber');
  }.property('make','cmodel', 'regNumber')
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
      carClass: 1,
      comment: "test comment"
    },
    {
      id: 2,
      regNumber: "АН111Р77",
      cmodel: "X5",
      make: "BMW",
      mileage: 100000,
      service: 90000,
      carClass: 2,
      comment: ""
    }
  ]
});


export default CarsModel;
