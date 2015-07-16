import DS from "ember-data";
import BaseController from "client/controllers/base/base-controller";
import accountTypes from "client/models/account-types";

export default BaseController.extend({
  accountTypes: accountTypes,
  accountTypesList: function(){
    return this.get("accountTypes");
  }.property("accountTypes"),
  selectedAccountType: function(key, value){
    let model = this.get("model");
    if (!model) {
      return;
    }
    if (arguments.length > 1){
      model.set("accountType", value != null ? value.id : null);
    }
    return this.get("accountTypes").filter(r => r.id === model.get("accountTypes"))[0];
  }.property("model"),
  rentItems: function() {
    return DS.PromiseArray.create({
      promise: this.store.find("rent").then(rents => rents.filter(r => r.get("status") !== "Closed"))
    });
  }.property("model")
});
