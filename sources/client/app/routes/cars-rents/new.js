import ProtectedRoute from "client/routes/base/protected";
import DirtyRouteMixin from "client/routes/base/dirty-route-mixin";

export default ProtectedRoute.extend(DirtyRouteMixin, {
  setupController: function(controller, model) {
    this.controllerFor('rents.new').set('model', model)/*.setProperties({isNew:false,content:model})*/;
  },
  renderTemplate: function(/*controller, model*/) {
    //var controller = this.controllerFor('rents/new');
    //let model = this.get('model');
    this.render('rents/new');
  },
  model: function(params, transition){
    let rent = this.store.createRecord("rent");
    let carId = transition.params["cars-rents"].car_id;
    //let accountType = params.accountType;
    return this.store.find("car", carId).then(car => {
      rent.set("car", car);
      rent.set("carDisplayName", car.get("displayName"));
      //operation.set("accountType",accountType);
      return rent;
    });
  }
});
