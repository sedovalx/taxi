import ProtectedRoute from "client/routes/base/protected";
import DirtyRouteMixin from "client/routes/base/dirty-route-mixin";
import Ember from "ember";

export default ProtectedRoute.extend(DirtyRouteMixin, { });
