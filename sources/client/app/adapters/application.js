import Ember from "ember";
import DS from "ember-data";

export default DS.RESTAdapter.extend({
  namespace: "api",
  ajaxError: function(jqXHR) {
    var error = this._super.apply(this, arguments);

    if (jqXHR && jqXHR.status === 422) {
      return new DS.InvalidError(Ember.$.parseJSON(jqXHR.responseText));
    } else {
      return error;
    }
  }
  //pathForType: function(modelName) {
  //  if (modelName === "cashierList"){
  //    return "reports/q-cashier-list";
  //  } else {
  //    return this._super(modelName);
  //  }
  //}
});
