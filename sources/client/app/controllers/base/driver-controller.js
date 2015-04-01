import Ember from "ember";
import DirtyControllerMixin from "client/controllers/base/dirty-controller-mixin";

export default Ember.ObjectController.extend(DirtyControllerMixin, {
  actions: {
    save: function(){
      let that = this;
      let model = this.get("model");
      model
        .save()
        .then(() => that.transitionToRoute("drivers"))
        .catch(error => {
          alert(error);
        });
    },
    cancel: function(){
      this.transitionToRoute("drivers");
    }
  },
  hasErrors: function(){
    let model = this.get("model");
    return model && !(model.get("lastName") && model.get("firstName") && model.get("middleName") &&
            model.get("pass") && model.get("address") && model.get("license") &&
            model.get("phone") && model.get("secPhone"));
  }.property("model.firstName","model.lastName","model.middleName","model.pass","model.address","model.license","model.phone","model.secPhone")
});
