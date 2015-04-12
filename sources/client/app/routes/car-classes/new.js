import ProtectedRoute from "client/routes/base/protected";
import DirtyRouteMixin from "client/routes/base/dirty-route-mixin";

export default ProtectedRoute.extend(DirtyRouteMixin, {
  model: function(){
    return this.store.createRecord("car-class");
  }
});
