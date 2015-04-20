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


CarClassModel.reopenClass({
  FIXTURES: [
    {
      id: 1,
      name: "Эконом1",
      rate: 500,
      comment: "test comment"
    },
    {
      id: 2,
      name: "Бизнес1",
      rate: 1000,
      comment: "пармпампам"
    }
  ]
});


export default CarClassModel;
