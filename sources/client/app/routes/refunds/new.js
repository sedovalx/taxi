import ProtectedRoute from "client/routes/base/protected";
import DirtyRouteMixin from "client/routes/base/dirty-route-mixin";

export default ProtectedRoute.extend(DirtyRouteMixin, {
  model: function(params, transition){
    let refund = this.store.createRecord("refund");
    let rentId = transition.params.rent.rent_id;
    return this.store.find("rent", rentId).then(rent => {
      refund.set("rent", rent);
      return refund;
    });
  }
});
