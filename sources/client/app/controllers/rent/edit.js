import RentController from "client/controllers/base/rent-controller";

export default RentController.extend({
  actions: {
    showOperation: function(operation){
      this.transitionToRoute('operations.edit', operation.operationId);
    }
  }
});
