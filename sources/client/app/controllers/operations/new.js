import OperationController from "client/controllers/base/operation-controller";
import operationTypes from "client/models/operation-types";

export default OperationController.extend({
  operationTypes: operationTypes,
  accountTypesList: function(){
    let model = this.get("model");
    let accountTypesList = this.get("accountTypes");
    if(model){
      if (model.get("operationType") === this.get("operationTypes")[0].id){
        accountTypesList = accountTypesList.filter(a => a.id !== accountTypesList[0].id); // this.get("operationTypes")[0].id = "Rent"
      }
    }
    return accountTypesList;
  }.property("model","accountTypes","operationTypes"),
  actions: {
    //redefining save method from base-controller to chane amount sign
    save: function(){
      let amount = this.get("amount");
      let operationType = this.get("operationType");
      if(operationType === this.get("operationTypes")[0].id){ // this.get("operationTypes")[0].id = "charge"
        this.set("amount",-amount);
      }
      let model = this.get("model");
      model
        .save()
        .then(() => history.back());
    }},
  discardModel: function(){
    let model = this.get("model");
    this.store.unloadRecord(model);
    this.set("model", null);
  }
});
