import ProtectedRoute from "client/routes/base/protected";
import DirtyRouteMixin from "client/routes/base/dirty-route-mixin";
import Ember from "ember";

export default ProtectedRoute.extend(DirtyRouteMixin, {
  model: function(params, transition){
    let refund = this.store.createRecord("refund");
    let rentId = transition.params.rent.rent_id;
    return Ember.RSVP.all([
      this.store.find("rent", rentId).then(rent => {
        refund.set("rent", rent);
        return refund;
      }),
      Ember.$.getJSON('/api/reports/q-rent-total', { rent: rentId })
    ]);
  },
  setupController: function(controller, data) {
    let [model, total] = data;
    let balance = total.total;
    if (balance > 0){
      model.set('amount', balance);
    } else {
      model.set('amount', 0);
    }
    controller.set('model', model);
    controller.set('rentTotal', total);
  }
});
