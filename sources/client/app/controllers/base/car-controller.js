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
    return model && !(
      this.get("regNumber") &&
      this.get("make") &&
      this.get("cmodel") &&
      this.get("mileage") &&
      this.get("service") &&
      this.get("carClass").get("id") );
  }.property("regNumber","make","cmodel","mileage","service","carClass.content"),

  carClassItems: function() {
    return this.store.find("car-class");
  }.property("model")
});
