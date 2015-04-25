import RentController from "client/controllers/base/rent-controller";

export default RentController.extend({
  discardModel: function(){
    let model = this.get("model");
    this.store.unloadRecord(model);
    this.set("model", null);
  }
});
