import DS from "ember-data";

let attr = DS.attr;
var CarClassModel = DS.Model.extend({
  name: attr("string"),
  rate: attr("number"),
  comment: attr("string"),
  creationDate: attr("date"),
  editDate: attr("date"),
  creator: DS.belongsTo("user", {inverse: null, async: true}),
  editor: DS.belongsTo("user", {inverse: null, async: true})
});

export default CarClassModel;
