import OperationController from "client/controllers/base/operation-controller";

export default OperationController.extend({
  actions: {
    //redefining save method from base-controller to chane amount sign
    save: function(){
      let amount = this.get("amount");
      let operationType = this.get("operationType");
      if(operationType === "Charge"){
        this.set("amount",-amount);
      }
      let model = this.get("model");
      model
        .save()
        .then(() => history.back());
    }},
  discardModel: function(){
    let model = this.get("model");
    this.store.unloadRecord(model);
    this.set("model", null);
  }
});
