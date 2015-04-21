import ProtectedRoute from "client/routes/base/protected";

export default ProtectedRoute.extend({
  model: function() {
    return this.store.find("rent");
  }
});
