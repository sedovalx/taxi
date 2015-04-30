import Ember from "ember";
import DirtyControllerMixin from "client/controllers/base/dirty-controller-mixin";

export default Ember.ObjectController.extend(DirtyControllerMixin, {
  actions: {
    save: function(){
      let that = this;
      let model = this.get("model");
      model
        .save()
        .then(() => that.transitionToRoute("payments"));
    },
    cancel: function(){
      this.transitionToRoute("payments");
    }
  },
  rentItems: function() {
    return this.store.find("rent");
  }.property("model")
});