import Ember from "ember";
import DirtyControllerMixin from "client/controllers/base/dirty-controller-mixin";
import roles from "client/models/roles"

export default Ember.ObjectController.extend(DirtyControllerMixin, {
  roles: roles,
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
        .then(() => that.transitionToRoute("users"));
    },
    cancel: function(){
      this.transitionToRoute("users");
    }
  }
});
