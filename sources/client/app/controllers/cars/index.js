import Ember from "ember";
import ListController from "client/controllers/base/list-controller";

export default ListController.extend({
  sortProperties: ["regNumber", "make", "model", "mileage"],
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
    let carModelColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Модель",
      contentPath: "carModel"
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
    let rateColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Тариф",
      contentPath: "rate"
    });
    let commentColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Примечания",
      contentPath: "comment"
    });
    return [regNumberColumn, makeColumn, carModelColumn, rateColumn, mileageColumn, serviceColumn, commentColumn];
  }.property(),
  cars: function(){
    return this;
  }.property("@each")
});
