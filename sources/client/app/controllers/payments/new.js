import PaymentController from "client/controllers/base/payment-controller";

export default PaymentController.extend({
  discardModel: function(){
    let model = this.get("model");
    this.store.unloadRecord(model);
    this.set("model", null);
  }
});
