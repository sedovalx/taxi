import Ember from "ember";
import DirtyControllerMixin from "client/controllers/base/dirty-controller-mixin";

export default Ember.ObjectController.extend(DirtyControllerMixin, {
  statuses: [
    { id: "Active", label: "Активна" },
    { id: "OnHold", label: "Приостановлена" },
    { id: "Account", label: "Под расчет" },
    { id: "Closed", label: "Закрыта" }
  ],
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
  actions: {
    save: function(){
      let that = this;
      let model = this.get("model");
      model
        .save()
        .then(() => that.transitionToRoute("rents"))
        .catch(error => {
          alert(error);
        });
    },
    cancel: function(){
      this.transitionToRoute("rents");
    }
  },

  hasErrors: function(){
    let model = this.get("model");
    return model && !(
      this.get("driver").get("id") &&
      this.get("car").get("id") &&
      this.get("deposit") &&
      this.get("selectedStatus")
      );
  }.property("driver","car","deposit","selectedStatus","car.content","driver.content"),

  carItems: function() {
    return this.store.find("car");
  }.property("model"),
  driverItems: function() {
    return this.store.find("driver");
  }.property("model")


});
