import Ember from "ember";
import $ from "jquery";

export default Ember.ObjectController.extend({
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
  isDirty: function(){
    let model = this.get("model");
    return (model && this._isDirty(model));
  },
  discardModel: function(){
    let model = this.get("model");
    model.rollback();
  },
  _isDirty: function(model){
    return !$.isEmptyObject(model.changedAttributes());
  },
  hasErrors: function(){
    let model = this.get("model");
    //return !(model.get("firstName") && model.get("lastName"));
    return !(model.get("lastName") && model.get("firstName") && model.get("middleName") && model.get("pass") && model.get("address") && model.get("license") && model.get("phone") && model.get("secPhone"));
  }.property("model.firstName","model.lastName","model.middleName","model.pass","model.address","model.license","model.phone","model.secPhone")
});
