import Ember from "ember";
import DirtyControllerMixin from "client/controllers/base/dirty-controller-mixin";

export default Ember.ObjectController.extend(DirtyControllerMixin, {
  actions: {
    save: function(){
      let that = this;
      let model = this.get("model");
      model
        .save()
        .then(() => that.transitionToRoute("cars"))
        .catch(error => {
          alert(error);
        });
    },
    cancel: function(){
      this.transitionToRoute("cars");
    }
  },
  hasErrors: function(){
    let model = this.get("model");
    return model && !(model.get("regNumber") && model.get("make") && model.get("cmodel") &&
            model.get("mileage") && model.get("service"));
  }.property("model.regNumber","model.make","model.cmodel","model.mileage","model.service")
});
