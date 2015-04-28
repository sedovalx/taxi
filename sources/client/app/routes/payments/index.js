import ProtectedRoute from "client/routes/base/protected";

export default ProtectedRoute.extend({
  model: function() {
    this.store.find("car");
    this.store.find("driver");
    return this.store.find("payment");
  }
});
