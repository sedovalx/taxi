import roles from "client/models/roles";
import BaseController from "client/controllers/base/base-controller";

export default BaseController.extend({
  roles: roles,
  selectedRole: function(key, value){
    let model = this.get("model");
    if (!model) {
      return;
    }

    if (arguments.length > 1){
      model.set("role", value != null ? value.id : null);
    }
    return this.get("roles").filter(r => r.id === model.get("role"))[0];
  }.property("model")
});
