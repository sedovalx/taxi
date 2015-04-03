import Ember from "ember";
import DirtyControllerMixin from "client/controllers/base/dirty-controller-mixin";

export default Ember.ObjectController.extend(DirtyControllerMixin, {
  roles: [
    { id: "Administrator", label: "Администритор" },
    { id: "Accountant", label: "Бухгалтер" },
    { id: "Repairman", label: "Ремонтник" },
    { id: "Cashier", label: "Кассир" }
  ],
  selectedRole: function(key, value){
    let model = this.get("model");
    if (!model) {
      return;
    }

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
  hasErrors: function(){
    let model = this.get("model");
    return model && !(model.get("login") && this.get("selectedRole"));
  }.property("model.login", "selectedRole")
});