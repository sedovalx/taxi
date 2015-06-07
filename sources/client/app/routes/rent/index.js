import ProtectedRoute from "client/routes/base/protected";
import DirtyRouteMixin from "client/routes/base/dirty-route-mixin";

export default ProtectedRoute.extend(DirtyRouteMixin, {
  beforeModel: function(rent){
    this.transitionTo("rent.edit", rent);
  }
});
