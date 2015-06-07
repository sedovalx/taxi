import ProtectedRoute from "client/routes/base/protected";
import $ from "jquery";

export default ProtectedRoute.extend({
  model: function() {
    return $.getJSON("/api/reports/q-rent").then(rents => {
      return rents.map(r => this.store.push("rent", r));
    });
  }
});
