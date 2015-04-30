import Ember from "ember";

export default Ember.ArrayController.extend({
  sortProperties: ["repairDate", "rent.displayName"],
  selectedRow: null,
  selectionEmpty: function(){
    return this.get("selectedRow") == null;
  }.property("selectedRow"),

  actions: {
    edit: function(){
      let row = this.get("selectedRow");
      if (row) {
        this.transitionToRoute("repair.edit", row.id);
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
      contentPath: "repairDate"
    });
    let costColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Сумма, р",
      contentPath: "cost"
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
    return [repairDateColumn, costColumn, rentColumn, descriptionColumn];
  }.property(),
  repairs: function(){
    return this;
  }.property("@each")
});
