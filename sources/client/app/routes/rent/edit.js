import ProtectedRoute from "client/routes/base/protected";
import DirtyRouteMixin from "client/routes/base/dirty-route-mixin";
import Ember from "ember";

export default ProtectedRoute.extend(DirtyRouteMixin, {
  model: function(params, transition){
    return Ember.RSVP.all([
      this.store.find("rent", transition.params.rent.rent_id),
      Ember.$.getJSON('/api/reports/q-rent-history', { rent: transition.params.rent.rent_id })
    ]);
  },
  setupController: function(controller, data){
    // see https://babeljs.io/docs/learn-es2015/#destructuring
    let [model, history] = data;
    controller.set('model', model);
    controller.set('history', history);
  },
  actions: {
    invalidateModel: function(){
      this.refresh();
    }
  }
});
