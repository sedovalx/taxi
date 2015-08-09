import ProtectedRoute from "client/routes/base/protected";
import DirtyRouteMixin from "client/routes/base/dirty-route-mixin";
import Ember from "ember";

export default ProtectedRoute.extend(DirtyRouteMixin, {
  model: function(){
    return Ember.$.getJSON('/api/profits/current').then(response => {
        return {
          currentAmount: response.amount,
          model: this.store.createRecord("profit")
        };
      }
    );
  },
  setupController: function(controller, data) {
    data.model.set('amount', data.currentAmount);
    controller.set('model', data.model);
    controller.set('currentAmount', data.currentAmount);
  }
});
