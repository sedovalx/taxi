import DS from 'ember-data';

/* global moment */
// No import for moment, it's a global called `moment`

let attr = DS.attr;
export default DS.Model.extend({
  changeTime: attr("date"),
  amount: attr("number"),
  presence: attr("boolean"),
  rentId: attr("number"),
  comment: attr("string"),
  displayChangeTime: function(){
    return moment(this.get("changeTime")).format("DD-MM-YYYY");
  }.property("changeTime")
});

