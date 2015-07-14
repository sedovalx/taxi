import ProtectedRoute from "client/routes/base/protected";
import DirtyRouteMixin from "client/routes/base/dirty-route-mixin";
import Ember from "ember";

function defineAmountSign(history) {
  if (history && history.length) {
    for (var i = 0; i < history.length; i++) {
      let status = history[i];
      for (var j = 0; j < status.operations.length; j++) {
        let op = status.operations[j];
        op.isPositive = op.amount >= 0;
      }
    }
  }

  return history;
}

export default ProtectedRoute.extend(DirtyRouteMixin, {
  queryParams: {
    tab: {
      replace: true
    },
    panel: {
      replace: true
    }
  },
  model: function(params, transition){
    return Ember.RSVP.all([
      this.store.find("rent", transition.params.rent.rent_id),
      Ember.$.getJSON('/api/reports/q-rent-history', { rent: transition.params.rent.rent_id }),
      Ember.$.getJSON('/api/reports/q-rent-total', { rent: transition.params.rent.rent_id })
    ]);
  },
  setupController: function(controller, data){
    // see https://babeljs.io/docs/learn-es2015/#destructuring
    let [model, history, total] = data;
    controller.set('model', model);
    controller.set('history', defineAmountSign(history));
    controller.set('rentTotal', total);
  },
  actions: {
    invalidateModel: function(){
      this.refresh();
    }
  }
});
