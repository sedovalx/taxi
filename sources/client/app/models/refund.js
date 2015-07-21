import DS from "ember-data";
/* global moment */
// No import for moment, it's a global called `moment`

let attr = DS.attr;
var RefundModel = DS.Model.extend({
  changeTime: attr("date", {
    defaultValue: () => new Date()
  }),
  amount: attr("number"),
  rent: DS.belongsTo("rent", {inverse: null, async: true}),
  comment: attr("string"),
  creationDate: attr("date"),
  editDate: attr("date"),
  creator: DS.belongsTo("user", {inverse: null, async: true}),
  editor: DS.belongsTo("user", {inverse: null, async: true}),
  displayChangeTime: function(){
    return moment(this.get("changeTime")).format("DD-MM-YYYY");
  }.property("changeTime")
  //rentDisplayName: function(){
  //  let rentId = this.get("rent");
  //  this.store.find("rent",rentId).then(rent => {return rent.get("driverDisplayName") + " - " + rent.get("carDisplayName");});
  //}.property("rent")
});

export default RefundModel;
