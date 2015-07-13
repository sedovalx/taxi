import DS from "ember-data";
import accountTypes from "client/models/account-types";
import operationTypes from "client/models/operation-types";
/* global moment */
// No import for moment, it's a global called `moment`

let attr = DS.attr;
var OperationModel = DS.Model.extend({
  changeTime: attr("date", {
    defaultValue: () => new Date()
  }),
  amount: attr("number"),
  rent: DS.belongsTo("rent", {inverse: null, async: true}),
  accountType: attr("string"),
  accountTypeDisplayName: function(){
    let accountType = accountTypes.filter(r => r.id === this.get("accountType"))[0];
    return accountType ? accountType.label : this.get("accountType");
  }.property("accountType"),
  rentDisplayName: attr("string"),
  comment: attr("string"),
  creationDate: attr("date"),
  editDate: attr("date"),
  creator: DS.belongsTo("user", {inverse: null, async: true}),
  editor: DS.belongsTo("user", {inverse: null, async: true}),
  presence: attr("boolean", {
    defaultValue: true
  }),
  operationType: attr("string"),
  operationTypeDisplayName: function(){
    let operationType = operationTypes.filter(r => r.id === this.get("operationType"))[0];
    return operationType ? operationType.label : this.get("operationType");
  }.property("operationType"),
  displayChangeTime: function(){
    return moment(this.get("changeTime")).format("DD-MM-YYYY");
  }.property("changeTime")
});

export default OperationModel;
