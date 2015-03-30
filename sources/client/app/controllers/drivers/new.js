import DriverController from "./../driver-controller.js";

export default DriverController.extend({
  discardModel: function(){
    let model = this.get("model");
    this.store.unloadRecord(model);
    this.set("model", null);
  }
});
