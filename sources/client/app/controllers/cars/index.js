import Ember from "ember";

export default Ember.ArrayController.extend({
  sortProperties: ["regNumber", "make", "model", "mileage"],
  selectedRow: null,
  selectionEmpty: function(){
    return this.get("selectedRow") == null;
  }.property("selectedRow"),

  actions: {
    edit: function(){
      let row = this.get("selectedRow");
      if (row) {
        this.transitionToRoute("car.edit", row.id);
      }
    },
    create: function(){
      this.transitionToRoute("cars.new");
    },
    remove: function(){
	  let row = this.get("selectedRow");
	  if (row && confirm("Вы подтверждаете удаление автомобиля?")) {
		row.deleteRecord();
		row.save();
	  }
    }
  },

  // Ember Table
  columns: function(){
    let regNumberColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Номерной знак",
      contentPath: "regNumber"
    });
    let modelColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Модель",
      contentPath: "cmodel"
    });
    let makeColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Марка",
      contentPath: "make"
    });
    let mileageColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Пробег, км",
      contentPath: "mileage"
    });
    let serviceColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Следующий ТО, км",
      contentPath: "service"
    });
    let classIDColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Класс автомобиля",
      contentPath: "classID"
    });
    let commentColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Примечания",
      contentPath: "comment"
    });
    return [regNumberColumn, makeColumn, modelColumn, classIDColumn, mileageColumn, serviceColumn, commentColumn];
  }.property(),
  cars: function(){
    return this;
  }.property("@each")
});
