import Ember from "ember";

export default Ember.Component.extend({
  saveAction: "save",
  cancelAction: "cancel",
  actions: {
    save: function(){
      this.sendAction('saveAction');
    },
    cancel: function(){
      this.sendAction('cancelAction');
    }
  }
});
