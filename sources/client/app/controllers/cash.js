import BaseController from "client/controllers/base/base-controller";

export default BaseController.extend({
  discardModel: function(){
    let model = this.get("model");
    this.store.unloadRecord(model);
    this.set("model", null);
  },
  lastChangeTime: 'lastChangeTime',
  lastAmount: 'lastAmount',
  lastComment: 'lastComment'
  //actions: {
  //  //save profit object
  //  removeCash: function(){
  //    //let selected = this.get("selectedRow");
  //    //let routeString = "cars."+selected.get("driverId")+".rents.new";
  //    this.transitionToRoute("rents.new"/*,selected.get("id")*/);
  //  }
  //}
});
