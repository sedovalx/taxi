import Ember from "ember";
import DS from "ember-data";
import DirtyControllerMixin from "client/controllers/base/dirty-controller-mixin";

export default Ember.ObjectController.extend(DirtyControllerMixin, {
  actions: {
    save: function(){
      let that = this;
      let model = this.get("model");
      model
        .save()
        .then(() => that.transitionToRoute("fines"));
    },
    cancel: function(){
      this.transitionToRoute("fines");
    }
  },
  rentItems: function() {
    return DS.PromiseArray.create({
      promise: this.store.find("rent").then(rents => rents.filter(r => r.get("status") !== "Closed"))
    });
  }.property("model")
});
