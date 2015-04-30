import RepairController from "client/controllers/base/repair-controller";

export default RepairController.extend({
  discardModel: function(){
    let model = this.get("model");
    this.store.unloadRecord(model);
    this.set("model", null);
  }
});
