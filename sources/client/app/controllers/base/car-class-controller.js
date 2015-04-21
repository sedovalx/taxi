import Ember from "ember";
import DirtyControllerMixin from "client/controllers/base/dirty-controller-mixin";

export default Ember.ObjectController.extend(DirtyControllerMixin, {
  actions: {
    save: function(){
      let that = this;
      let model = this.get("model");
      model
        .save()
        .then(() => that.transitionToRoute("car-classes"));
    },
    cancel: function(){
      this.transitionToRoute("car-classes");
    }
  },
  hasErrors: function(){
    let model = this.get("model");
    return model && !(model.get("name") && model.get("rate"));
  }.property("model.name","model.rate")
});
