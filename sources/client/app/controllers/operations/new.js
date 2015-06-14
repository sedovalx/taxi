import OperationController from "client/controllers/base/operation-controller";

export default OperationController.extend({
  discardModel: function(){
    let model = this.get("model");
    this.store.unloadRecord(model);
    this.set("model", null);
  }
});
