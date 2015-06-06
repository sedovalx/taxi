import Ember from "ember";
import ListController from "client/controllers/base/list-controller";

export default ListController.extend({
  sortProperties: ["changeTime", "rent.displayName"],
  actions: {
    edit: function(){
      let row = this.get("selectedRow");
      if (row) {
        this.transitionToRoute("repairs.edit", row.id);
      }
    },
    create: function(){
      this.transitionToRoute("repairs.new");
    },
    remove: function(){
      let row = this.get("selectedRow");
      if (row && confirm("Вы подтверждаете удаление ремонта?")) {
        row.deleteRecord();
        row.save();
	    }
    }
  },

  // Ember Table
  columns: function(){
    let repairDateColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Дата ремонта",
      contentPath: "displayChangeTime"
    });
    let amountColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Сумма, р",
      contentPath: "amount"
    });
    let rentColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Аренда",
      contentPath: "rent.displayName"
    });
    let descriptionColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Описание",
      contentPath: "description"
    });
    return [repairDateColumn, amountColumn, rentColumn, descriptionColumn];
  }.property(),
  repairs: function(){
    return this;
  }.property("@each")
});
