import Ember from "ember";
import DS from "ember-data";

export default Ember.ArrayController.extend({
  //sortProperties: ["regNumber", "make", "model", "mileage"],
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
    },
    createPayment:function(){
      //let row = this.get("selectedRow");
      this.transitionToRoute("payments.new");
    },
    createFine:function(){
      //let row = this.get("selectedRow");
      this.transitionToRoute("fines.new");
    },
    createRent:function(){
      //let row = this.get("selectedRow");
      this.transitionToRoute("rents.new");
    }
  },
  // Ember Table
  columns: function(){
    let displayNameColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Автомобиль",
      contentPath: "displayName"
    });
    let driverDisplayNameColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Водитель",
      contentPath: "driverDisplayName"
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
    let rentBalanceColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Баланс аренды",
      contentPath: "rentBalance"
    });
    let fineBalanceColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Баланс штрафов",
      contentPath: "fineBalance"
    });
    let repairBalanceColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Баланс ремонта",
      contentPath: "repairBalance"
    });
    let rentStatusColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Статус",
      contentPath: "rentStatus"
    });
    return [displayNameColumn, driverDisplayNameColumn, rentBalanceColumn,repairBalanceColumn, fineBalanceColumn, mileageColumn, serviceColumn,rentStatusColumn];
  }.property(),
  cars: function(){
    return this.model.cars;
  }.property("@each"),
  data: function() {
    var data = DS.PromiseArray.create({
      promise: Ember.RSVP.all([
        this.store.find("driver"),
        this.store.find("car"),
        this.store.find("rent"),
        this.store.find("payment"),
        this.store.find("fine"),
        this.store.find("repair")
      ]).then(arrays => {
        //let drivers = arrays[0];
        let cars = arrays[1];
        let rents = arrays[2];
        //let payments = arrays[3];
        //let fines = arrays[4];
        //let repairs = arrays[5];
        // добавляем в список машин данные из других списков
        let cashierArray = cars.map(function(item){
          //находим активную аренду для этой машины
          item.rent = rents.filter(r => ((r.get("status") !== "Closed") && (r.get("car").get("id") === item.get("id"))))[0];
          if (item.rent == null){
            //добавляем имя Водителя
            item.driverDisplayName = "-";
            //добавляем состояние аренды
            item.rentStatus = "Закрыта";
            //здесь надо расчитать баланс по аренде, по штрафам и по ремонтам
          } else {
            //добавляем имя Водителя
            item.driverDisplayName = item.rent.get("driverDisplayName");
            //добавляем состояние аренды
            item.rentStatus = item.rent.get("status");
            //здесь надо расчитать баланс по аренде, по штрафам и по ремонтам
          }
          return item;
        });
        return cashierArray;
      })
    });
    return data;
  }.property("@each")
});
