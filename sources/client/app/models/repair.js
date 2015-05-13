import DS from "ember-data";

/* global moment */
// No import for moment, it's a global called `moment`

let attr = DS.attr;
var RepairModel = DS.Model.extend({
  repairDate: attr("date",{
    defaultValue: () => new Date()
  }),
  cost: attr("number"),
  rent: DS.belongsTo("rent", {inverse: null, async: true}),
  description: attr("string"),
  creationDate: attr("date"),
  editDate: attr("date"),
  creator: DS.belongsTo("user", {inverse: null, async: true}),
  editor: DS.belongsTo("user", {inverse: null, async: true}),
  
  displayRepairDate: function(){
    return moment(this.get("repairDate")).format("DD-MM-YYYY");
  }.property("repairDate")
});

export default RepairModel;
