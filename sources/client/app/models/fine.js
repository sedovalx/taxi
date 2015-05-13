import DS from "ember-data";

/* global moment */
// No import for moment, it's a global called `moment`

let attr = DS.attr;
var FineModel = DS.Model.extend({
  fineDate: attr("date",{
    defaultValue: () => new Date()
  }),
  cost: attr("number"),
  rent: DS.belongsTo("rent", {inverse: null, async: true}),
  description: attr("string"),
  creationDate: attr("date"),
  editDate: attr("date"),
  creator: DS.belongsTo("user", {inverse: null, async: true}),
  editor: DS.belongsTo("user", {inverse: null, async: true}),
  
  displayFineDate: function(){
    return moment(this.get("fineDate")).format("DD-MM-YYYY");
  }.property("fineDate")
});

export default FineModel;
