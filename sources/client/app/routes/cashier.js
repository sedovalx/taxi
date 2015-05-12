import ProtectedRoute from "client/routes/base/protected";

export default ProtectedRoute.extend({
  model: function() {
    this.store.find("rent");
    this.store.find("fine");
    this.store.find("repair");
    this.store.find("payment");
    this.store.find("driver");
    return this.store.find("car");
  }
});
