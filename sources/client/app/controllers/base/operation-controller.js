import DS from "ember-data";
import BaseController from "client/controllers/base/base-controller";

export default BaseController.extend({
  rentItems: function() {
    return DS.PromiseArray.create({
      promise: this.store.find("rent").then(rents => rents.filter(r => r.get("status") !== "Closed"))
    });
  }.property("model")
});
