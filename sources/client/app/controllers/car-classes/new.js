import CarClassController from "client/controllers/base/car-class-controller";

export default CarClassController.extend({
  discardModel: function(){
    let model = this.get("model");
    this.store.unloadRecord(model);
    this.set("model", null);
  }
});
