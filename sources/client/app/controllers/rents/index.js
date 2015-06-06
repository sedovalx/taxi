import Ember from "ember";
import ListController from "client/controllers/base/list-controller";

export default ListController.extend({
  sortProperties: ["status","driver", "car", "deposit"],
  actions: {
    edit: function(){
      let row = this.get("selectedRow");
      if (row) {
        this.transitionToRoute("rents.edit", row.id);
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
      contentPath: "driverDisplayName"
    });
    let carColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Автомобиль",
      contentPath: "carDisplayName"
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
      contentPath: "displayStatus"
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
