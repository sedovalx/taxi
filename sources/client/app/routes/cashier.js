import ProtectedRoute from "client/routes/base/protected";
import Ember from "ember";
import DS from "ember-data";

export default ProtectedRoute.extend({
  model: function() {
    this.store.find("rent");
    this.store.find("driver");
    return this.store.find("car");
  }
});
