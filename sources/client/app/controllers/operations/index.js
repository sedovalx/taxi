import Ember from "ember";
import ListController from "client/controllers/base/list-controller";

export default ListController.extend({
  sortProperties: ["changeTime", "rent.displayName"],
  actions: {
    edit: function(){
      let row = this.get("selectedRow");
      if (row) {
        this.transitionToRoute("operations.edit", row.id);
      }
    },
    create: function(){
      this.transitionToRoute("operations.new");
    },
    remove: function(){
      let row = this.get("selectedRow");
      if (row && confirm("Вы подтверждаете удаление операции?")) {
        row.deleteRecord();
        row.save();
	    }
    }
  },

  // Ember Table
  columns: function(){
    let payDateColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Дата платежа",
      contentPath: "displayChangeTime"
    });
    let amountColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Сумма, р",
      contentPath: "amount"
    });
    let presenceColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      textAlign: "text-align-center",
      tableCellViewClass: "check-icon-cell",
      headerCellName: "Явка",
      contentPath: "presence"
    });
    let commentColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Примечания",
      contentPath: "comment"
    });
    return [payDateColumn, amountColumn, presenceColumn, commentColumn];
  }.property(),
  operations: function(){
    return this;
  }.property("@each")
});
