import Ember from "ember";
import ListController from "client/controllers/base/list-controller";

export default ListController.extend({
  sortProperties: ["date", "subject"],
  actions: {
    edit: function(){
      let row = this.get("selectedRow");
      if (row) {
        this.transitionToRoute("expense.edit", row.id);
      }
    },
    create: function(){
      this.transitionToRoute("expenses.new");
    },
    remove: function(){
      let row = this.get("selectedRow");
      if (row && confirm("Вы подтверждаете удаление расхода?")) {
        row.deleteRecord();
        row.save();
	    }
    }
  },

  // Ember Table
  columns: function(){
    let expenseDateColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Дата расхода",
      contentPath: "date"
    });
    let amountColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Сумма, р",
      contentPath: "amount"
    });
    let subjectColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Статья расхода",
      contentPath: "subject"
    });
    let commentColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Примечания",
      contentPath: "comment"
    });
    return [expenseDateColumn, amountColumn, subjectColumn, commentColumn];
  }.property(),
  expenses: function(){
    return this;
  }.property("@each")
});
