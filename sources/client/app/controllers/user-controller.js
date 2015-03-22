import Ember from "ember";
import $ from "jquery";

export default Ember.ObjectController.extend({
  roles: [
    { id: "Administrator", label: "Администритор" },
    { id: "Accountant", label: "Бухгалтер" },
    { id: "Repairman", label: "Ремонтник" },
    { id: "Cashier", label: "Кассир" }
  ],
  selectedRole: function(key, value){
    let model = this.get("model");
    if (arguments.length > 1){
      model.set("role", value != null ? value.id : null);
    }
    return this.get("roles").filter(r => r.id === model.get("role"))[0];
  }.property("model"),
  actions: {
    save: function(){
      let that = this;
      let model = this.get("model");
      model
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
    model.rollback();
  },
  _isDirty: function(model){
    return !$.isEmptyObject(model.changedAttributes());
  },
  hasErrors: function(){
    let model = this.get("model");
    return !(model.get("login") && this.get("selectedRole"));
  }.property("model.login", "selectedRole")
});
