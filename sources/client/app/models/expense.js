import DS from "ember-data";

let attr = DS.attr;
var ExpenseModel = DS.Model.extend({
  date: attr("date",{
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
  subject: attr("string"),
  comment: attr("string"),
  creationDate: attr("date"),
  editDate: attr("date"),
  creator: DS.belongsTo("user", {inverse: null, async: true}),
  editor: DS.belongsTo("user", {inverse: null, async: true})
});


ExpenseModel.reopenClass({
  FIXTURES: [
    {
      id: 1,
      date: "24.09.2015",
      amount: 5000,
      subject: "КА914АН77",
      comment: "comment 1"
    },
    {
      id: 2,
      date: "21.09.2015",
      amount: 100,
      subject: "ремонтник",
      comment: "comment 1"
    }
  ]
});


export default ExpenseModel;
