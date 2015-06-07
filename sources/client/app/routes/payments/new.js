import ProtectedRoute from "client/routes/base/protected";
import DirtyRouteMixin from "client/routes/base/dirty-route-mixin";

export default ProtectedRoute.extend(DirtyRouteMixin, {
  model: function(params, transition){
    let payment = this.store.createRecord("payment");
    let rentId = transition.params.rent.rent_id;
    return this.store.find("rent", rentId).then(rent => {
      payment.set("rent", rent);
      return payment;
    });
  }
});
