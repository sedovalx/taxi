import DS from "ember-data";

/* global moment */
// No import for moment, it's a global called `moment`

let attr = DS.attr;
var PaymentModel = DS.Model.extend({
  payDate: attr("date", {
    defaultValue: () => new Date()
  }),
  amount: attr("number"),
  rent: DS.belongsTo("rent", {inverse: null, async: true}),
  comment: attr("string"),
  creationDate: attr("date"),
  editDate: attr("date"),
  creator: DS.belongsTo("user", {inverse: null, async: true}),
  editor: DS.belongsTo("user", {inverse: null, async: true}),

  displayPayDate: function(){
    return moment(this.get("payDate")).format("DD-MM-YYYY");
  }.property("payDate") 
});

export default PaymentModel;
