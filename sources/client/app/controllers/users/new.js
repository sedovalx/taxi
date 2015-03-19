import Ember from "ember";
import $ from "jquery";

export default Ember.ObjectController.extend({
  roles: [
    { id: "Administrator", label: "Администритор" },
    { id: "Accountant", label: "Бухгалтер" },
    { id: "Repairman", label: "Ремонтник" },
    { id: "Cashier", label: "Кассир" }
  ],
  selectedRole: null,
  actions: {
    save: function(){
      let that = this;
      this.get("model")
        .save()
        .then(() => that.transitionToRoute("users"))
        .catch(error => {
          alert(error);
        });
    },
    cancel: function(){
      this.transitionToRoute("users");
    }
  },
  isDirty: function(){
    let model = this.get("model");
    return (model && this._isDirty(model));
  },
  discardModel: function(){
    let model = this.get("model");
    this.store.unloadRecord(model);
    this.set("model", null);
  },
  _isDirty: function(model){
    return !$.isEmptyObject(model.changedAttributes());
  }
});
