import RentController from "client/controllers/base/rent-controller";

export default RentController.extend({
  queryParams: ['tab'],
  tab: "general",
  isGeneral: function(){
    return this.get('tab') !== 'history';
  }.property('tab'),
  actions: {
    showOperation: function(operation){
      this.transitionToRoute('operations.edit', operation.operationId);
    },
    changeTab: function(tab) {
      this.set("tab", tab);
    }
  }
});
