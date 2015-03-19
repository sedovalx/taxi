import Ember from "ember";

export default Ember.Component.extend({
  selected: null,
  items: [],
  actions: {
    select: function(item){
      this.set('selected', item);
    }
  }
});
