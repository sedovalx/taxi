import Ember from "ember";

export default Ember.Component.extend({
  saveAction: "save",
  cancelAction: "cancel",
  readOnly: false,
  actions: {
    save: function(){
      this.sendAction('saveAction');
    },
    cancel: function(){
      this.sendAction('cancelAction');
    }
  }
});
