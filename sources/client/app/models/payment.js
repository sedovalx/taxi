import DS from "ember-data";

let attr = DS.attr;
var PaymentModel = DS.Model.extend({
  payDate: attr("date",{
    defaultValue: function() {
      var today = new Date();
      var dd = today.getDate();
      var mm = today.getMonth()+1; //January is 0!
      var yyyy = today.getFullYear();
      if(dd<10) {
        dd='0'+dd;
      }
      if(mm<10) {
        mm='0'+mm;
      }
      today = dd+'.'+mm+'.'+yyyy;
      return today;
    }
  }),
  amount: attr("number"),
  rent: DS.belongsTo("rent", {inverse: null, async: true}),
  comment: attr("string"),
  creationDate: attr("date"),
  editDate: attr("date"),
  creator: DS.belongsTo("user", {inverse: null, async: true}),
  editor: DS.belongsTo("user", {inverse: null, async: true})
});


PaymentModel.reopenClass({
  FIXTURES: [
    {
      id: 1,
      payDate: "24.09.2015",
      amount: 5000,
      rent: 1,
      comment: "comment 1"
    },
    {
      id: 2,
      payDate: "21.09.2015",
      amount: 100,
      rent: 1,
      comment: "comment 1"
    }
  ]
});


export default PaymentModel;
