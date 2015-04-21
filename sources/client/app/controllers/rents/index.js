import Ember from "ember";

export default Ember.ArrayController.extend({
  sortProperties: ["status","driver", "car", "deposit"],
  selectedRow: null,
  selectionEmpty: function(){
    return this.get("selectedRow") == null;
  }.property("selectedRow"),

  actions: {
    edit: function(){
      let row = this.get("selectedRow");
      if (row) {
        this.transitionToRoute("rent.edit", row.id);
      }
    },
    create: function(){
      this.transitionToRoute("rents.new");
    },
    remove: function(){
      let row = this.get("selectedRow");
      if (row && confirm("Вы подтверждаете удаление Аренды?")) {
        row.deleteRecord();
        row.save();
	    }
    }
  },

  // Ember Table
  columns: function(){
    let driverColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Водитель",
      contentPath: "driver.displayName"
    });
    let carColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Автомобиль",
      contentPath: "car.displayName"
    });
    let depositColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Залог",
      contentPath: "deposit"
    });
    let statusColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Статус",
      contentPath: "status"
    });
    let commentColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Примечания",
      contentPath: "comment"
    });
    return [statusColumn, driverColumn, carColumn, depositColumn,commentColumn];
  }.property(),
  rents: function(){
    return this;
  }.property("@each")
});
