import DriverController from "client/controllers/base/driver-controller";

export default DriverController.extend({
  discardModel: function(){
    let model = this.get("model");
    this.store.unloadRecord(model);
    this.set("model", null);
  }
});
