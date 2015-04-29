import FineController from "client/controllers/base/fine-controller";

export default FineController.extend({
  discardModel: function(){
    let model = this.get("model");
    this.store.unloadRecord(model);
    this.set("model", null);
  }
});
