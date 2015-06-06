import Ember from "ember";

export default Ember.ArrayController.extend({
  selectedRow: null,
  selectionEmpty: function(){
    return this.get("selectedRow") == null;
  }.property("selectedRow")
});
