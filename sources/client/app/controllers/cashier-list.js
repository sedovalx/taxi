import Ember from 'ember';
import ListController from "client/controllers/base/list-controller";

/* global moment */
// No import for moment, it's a global called `moment`

export default ListController.extend({
  queryParams: ['car', 'driver','date'],
  //queryParams defaults
  car: '',
  driver: '',
  date: function(){
    let currentTime = new Date();
    let currentWeekDay = moment(currentTime).format('d');
    let daysToNextCashDay = 0;
    switch(currentWeekDay){
      case "0"://Sunday
        daysToNextCashDay = 1;
        break;
      case "1"://Monday
        daysToNextCashDay = 2;
        break;
      case "2"://Tuesday
        daysToNextCashDay = 1;
        break;
      case "3"://Wednesday
        daysToNextCashDay = 2;
        break;
      case "4"://Thursday
        daysToNextCashDay = 1;
        break;
      case "5"://Friday
        daysToNextCashDay = 3;
        break;
      case "6"://Saturday
        daysToNextCashDay = 2;
        break;
    }
    let nextCashDayTime = moment(currentTime).add(daysToNextCashDay,'days');
    nextCashDayTime = moment(nextCashDayTime).milliseconds(0);
    nextCashDayTime = moment(nextCashDayTime).seconds(0);
    nextCashDayTime = moment(nextCashDayTime).minutes(0);
    nextCashDayTime = moment(nextCashDayTime).hour(7);
    let test = moment(nextCashDayTime).toISOString();
    return test;
  },
  //date: '',

  // связанные элементы лучше группировать
  filter: {
    car: '',
    driver: '',
    date: function() {
      return this.get("date");
    }.property('date'),
    dateInput: function() {
      return this.get("date");
    }.property('date')
  },
  actions: {
    createRent: function(){
      let selected = this.get("selectedRow");
      this.transitionToRoute("cars-rents.new",selected.get("id"));
      //let routeString = "cars."+selected.get("driverId")+".rents.new";
      //this.transitionToRoute("rents.new"/*,selected.get("id")*/);

    },
    suspendRent:function(){
      let selected = this.get("selectedRow");
      let rentId = selected.get("rentId");
      this.store.find("rent",rentId).then(rent => {
        rent.set("status", "Suspended");
        rent.save();
        selected.set("status", "Suspended");
        this.set("selectedRow",null);
        this.set("selectedRow",selected);
      });
    },
    resumeRent:function(){
      let selected = this.get("selectedRow");
      let rentId = selected.get("rentId");
      this.store.find("rent",rentId).then(rent => {
        rent.set("status", "Active");
        rent.save();
        selected.set("status", "Active");
        this.set("selectedRow",null);
        this.set("selectedRow",selected);
      });
    },
    createPaymentOperation: function(){
      let selected = this.get("selectedRow");
      this.transitionToRoute("operations.new", selected.get("rentId"),"payment");
    },
    createChargeOperation: function(){
      let selected = this.get("selectedRow");
      this.transitionToRoute("operations.new", selected.get("rentId"),"charge");
    },
    createRefund: function(){
      let selected = this.get("selectedRow");
      this.transitionToRoute("refunds.new", selected.get("rentId"));
    },
    filterTable: function(){
      let dateInput = this.get('filter.dateInput');
      let dateFormatted = moment(dateInput).toISOString();
      let filter = {
        car: this.get('filter.car'),
        driver: this.get('filter.driver'),
        date: dateFormatted
      };

      for (var prop in filter) {
        if (filter.hasOwnProperty(prop) && !filter[prop]){
          filter[prop] = '';
          this.set(prop, '');
        }
      }
      this.transitionToRoute("cashier-list", {queryParams: filter});
    },
    clearFilter: function(){
      this.set('filter.car','');
      this.set('filter.driver','');
      this.set('car','');
      this.set('driver','');
      this.transitionToRoute("cashier-list", {queryParams: null});
    },
    gotoRentInfo: function(){
      let selected = this.get("selectedRow");
      let rentId = selected.get("rentId");
      if (rentId != null) {
        this.transitionToRoute("rent.edit", rentId, {queryParams: {tab: "info"}});
      }
    },
    gotoRentEdit: function(){
      let selected = this.get("selectedRow");
      let rentId = selected.get("rentId");
      if (rentId != null) {
        this.transitionToRoute("rent.edit", rentId, {queryParams: {tab: "general"}});
      }
    },
    gotoCash: function(){
      this.transitionToRoute("cash");
      }
  },
  selectionRentIsEmpty: function(){
    let selected = this.get("selectedRow");
    return selected == null ? true : selected.get("rentId") == null;
  }.property("selectedRow"),
  selectionRentIsNotEmpty: function(){
    return !this.get("selectionRentIsEmpty");
  }.property("selectionRentIsEmpty"),
  selectionRentIsSuspended: function(){
    let selected = this.get("selectedRow");
    if(selected){
      if(selected.get("status") === "Suspended"){
        return 1;
      }
    }
      return 0;
  }.property("selectedRow"),
  selectionRentIsNotActive: function(){
    let selected = this.get("selectedRow");
    if(selected) {
      if (selected.get("status") === "Active") {
        return 0;
      }
    }
      return 1;
  }.property("selectedRow"),
  columns: function() {
    return [
      Ember.Table.ColumnDefinition.create({
        savedWidth: 200,
        canAutoResize: true,
        headerCellName: "Машина",
        contentPath: "car"
      }),
      Ember.Table.ColumnDefinition.create({
        savedWidth: 200,
        canAutoResize: true,
        headerCellName: "ФИО водителя",
        contentPath: "driver"
      }),
      Ember.Table.ColumnDefinition.create({
        savedWidth: 200,
        canAutoResize: true,
        textAlign: "text-align-center",
        tableCellViewClass: "check-icon-cell",
        headerCellName: "Явка",
        contentPath: "presence"
      }),
      Ember.Table.ColumnDefinition.create({
        savedWidth: 200,
        canAutoResize: true,
        headerCellName: "Итого",
        contentPath: "total"
      }),
      Ember.Table.ColumnDefinition.create({
        savedWidth: 200,
        canAutoResize: true,
        headerCellName: "Текущий баланс арендный",
        contentPath: "payments"
      }),
      Ember.Table.ColumnDefinition.create({
        savedWidth: 200,
        canAutoResize: true,
        headerCellName: "Текущий баланс ремонтный",
        contentPath: "repairs"
      }),
      Ember.Table.ColumnDefinition.create({
        savedWidth: 200,
        canAutoResize: true,
        headerCellName: "Текущий баланс штрафной",
        contentPath: "fines"
      }),
      Ember.Table.ColumnDefinition.create({
        savedWidth: 200,
        canAutoResize: true,
        headerCellName: "Пробег",
        contentPath: "mileage"
      }),
      Ember.Table.ColumnDefinition.create({
        savedWidth: 200,
        canAutoResize: true,
        headerCellName: "ТО",
        contentPath: "service"
      }),
      Ember.Table.ColumnDefinition.create({
        savedWidth: 200,
        canAutoResize: true,
        headerCellName: "Статус",
        contentPath: "displayStatus"
      })
    ];
  }.property(),
  data: function(){ return this; }.property('@each')
});
