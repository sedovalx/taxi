import DS from "ember-data";

let attr = DS.attr;
var CarsModel = DS.Model.extend({
  regNumber: attr("string"),
  carModel: attr("string"),
  make: attr("string"),
  mileage: attr("number"),
  service: attr("number"),
  rate: attr("number"),
  comment: attr("string"),
  creationDate: attr("date"),
  editDate: attr("date"),
  creator: DS.belongsTo("user", {inverse: null, async: true}),
  editor: DS.belongsTo("user", {inverse: null, async: true}),
  displayName: function(){
    return this.get('make') + ' ' + this.get('carModel') + ' ' + this.get('regNumber');
  }.property('make', 'carModel', 'regNumber')
});

export default CarsModel;
