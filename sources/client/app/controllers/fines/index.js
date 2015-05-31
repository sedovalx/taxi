import Ember from "ember";

export default Ember.ArrayController.extend({
  sortProperties: ["changeTime", "rent.displayName"],
  selectedRow: null,
  selectionEmpty: function(){
    return this.get("selectedRow") == null;
  }.property("selectedRow"),

  actions: {
    edit: function(){
      let row = this.get("selectedRow");
      if (row) {
        this.transitionToRoute("fine.edit", row.id);
      }
    },
    create: function(){
      this.transitionToRoute("fines.new");
    },
    remove: function(){
      let row = this.get("selectedRow");
      if (row && confirm("Вы подтверждаете удаление штрафа?")) {
        row.deleteRecord();
        row.save();
	    }
    }
  },

  // Ember Table
  columns: function(){
    let changeTimeColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Дата штрафа",
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
    return [changeTimeColumn, amountColumn, rentColumn, descriptionColumn];
  }.property(),
  fines: function(){
    return this;
  }.property("@each")
});
