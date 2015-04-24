import Ember from "ember";
import DirtyControllerMixin from "client/controllers/base/dirty-controller-mixin";
import statuses from "client/models/rent-statuses";

export default Ember.ObjectController.extend(DirtyControllerMixin, {
  actions: {
    save: function(){
      let that = this;
      let model = this.get("model");
      model
        .save()
        .then(() => that.transitionToRoute("rents"));
    },
    cancel: function(){
      this.transitionToRoute("rents");
    }
  },
  statuses: statuses,
  selectedStatus: function(key, value){
    let model = this.get("model");
    if (!model) {
      return;
    }

    if (arguments.length > 1){
      model.set("status", value != null ? value.id : null);
    }
    return this.get("statuses").filter(r => r.id === model.get("status"))[0];
  }.property("model"),
  carItems: function() {
    return this.store.find("car");
  }.property("model"),
  driverItems: function() {
    return this.store.find("driver");
  }.property("model")
});
