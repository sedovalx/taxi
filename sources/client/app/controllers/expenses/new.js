import ExpenseController from "client/controllers/base/expense-controller";

export default ExpenseController.extend({
  discardModel: function(){
    let model = this.get("model");
    this.store.unloadRecord(model);
    this.set("model", null);
  }
});
