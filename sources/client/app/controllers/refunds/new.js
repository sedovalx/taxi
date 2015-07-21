import BaseController from "client/controllers/base/base-controller";
import DS from "ember-data";

export default BaseController.extend({
  rentItems: function() {
    return DS.PromiseArray.create({
      promise: this.store.find("rent").then(rents => rents.filter(r => r.get("status") !== "Closed"))
    });
  }.property("model"),
  discardModel: function(){
    let model = this.get("model");
    this.store.unloadRecord(model);
    this.set("model", null);
  }
});
