import ProtectedRoute from "client/routes/base/protected";
import DirtyRouteMixin from "client/routes/base/dirty-route-mixin";

export default ProtectedRoute.extend(DirtyRouteMixin, {
  model: function(){
    return this.store.createRecord("payment");
  },
  setupController: function(controller, model) {
    if(model === null){
      model = this.store.createRecord("payment");
      this._super(controller, model);
    }
    // Implement your custom setup after
    let payDate = new Date();
    let newPayment = this.store.createRecord("payment", {changeTime: payDate, rent: model});
    //this.generateController('payments.new', newPayment).set('model', newPayment);
    this.controllerFor('payments.new').set('model', newPayment);
  },
  renderTemplate: function() {
    this.render('payments/new');
  }
});
