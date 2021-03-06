import ProtectedRoute from "client/routes/base/protected";
import DirtyRouteMixin from "client/routes/base/dirty-route-mixin";

export default ProtectedRoute.extend(DirtyRouteMixin, {
  model: function(params, transition){
    let operation = this.store.createRecord("operation");
    let rentId = transition.params.rent.rent_id;
    let operationType = params.operationType;
    return this.store.find("rent", rentId).then(rent => {
      operation.set("rent", rent);
      operation.set("operationType",operationType);
      return operation;
    });
  }
});
