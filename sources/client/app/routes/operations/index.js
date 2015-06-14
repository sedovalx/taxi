import ProtectedRoute from "client/routes/base/protected";
import $ from "jquery";

export default ProtectedRoute.extend({
  model: function(params, transition) {
    $.getJSON("/api/reports/q-operation-list", { rent: transition.params.rent.rent_id }).then(function(data){
      console.log(data);
    });
    //return this.store.find("payment", { rent: transition.params.rent.rent_id });
  }
});
