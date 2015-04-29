import DS from "ember-data";

let attr = DS.attr;
var FineModel = DS.Model.extend({
  fineDate: attr("date",{
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
  cost: attr("number"),
  rent: DS.belongsTo("rent", {inverse: null, async: true}),
  description: attr("string"),
  creationDate: attr("date"),
  editDate: attr("date"),
  creator: DS.belongsTo("user", {inverse: null, async: true}),
  editor: DS.belongsTo("user", {inverse: null, async: true})
});


FineModel.reopenClass({
  FIXTURES: [
    {
      id: 1,
      fineDate: "24.09.2015",
      cost: 5000,
      rent: 1,
      description: "comment 1"
    },
    {
      id: 2,
      fineDate: "21.09.2015",
      cost: 100,
      rent: 1,
      description: "comment 1"
    }
  ]
});


export default FineModel;
