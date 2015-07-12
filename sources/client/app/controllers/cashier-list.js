import Ember from 'ember';
import ListController from "client/controllers/base/list-controller";

export default ListController.extend({
  queryParams: ['car', 'driver','date'],
  //queryParams defaults
  car: '',
  driver: '',
  date: '',

  // связанные элементы лучше группировать
  filter: {
    car: '',
    driver: '',
    date: ''
  },
  actions: {
    createRentOperation: function(){
      let selected = this.get("selectedRow");
      this.transitionToRoute("operations.new", selected.get("rentId"),"Rent");
    },
    createFineOperation: function(){
      let selected = this.get("selectedRow");
      this.transitionToRoute("operations.new", selected.get("rentId"),"Fine");
    },
    createRepairOperation: function(){
      let selected = this.get("selectedRow");
      this.transitionToRoute("operations.new", selected.get("rentId"),"Repair");
    },
    filterTable: function(){
      let filter = {
        car: this.get('filter.car'),
        driver: this.get('filter.driver'),
        date: this.get('filter.date')
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
      this.set('filter.date','');
      this.set('car','');
      this.set('driver','');
      this.set('date','');
      this.transitionToRoute("cashier-list", {queryParams: null});
    },
    gotoHistory: function(){
      let selected = this.get("selectedRow");
      let rentId = selected.get("rentId");
      if (rentId != null)
        this.transitionToRoute("rent.edit", rentId, { queryParams: { tab: "history" } })
    }
  },
  selectionRentIsEmpty: function(){
    let selected = this.get("selectedRow");
    return selected == null ? true : selected.get("rentId") == null;
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
