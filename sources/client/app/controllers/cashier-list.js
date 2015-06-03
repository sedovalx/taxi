import Ember from 'ember';

export default Ember.ArrayController.extend({
  selectedRow: null,
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
        headerCellName: "Явка",
        contentPath: "presence"
      }),
      Ember.Table.ColumnDefinition.create({
        savedWidth: 200,
        canAutoResize: true,
        headerCellName: "Текущий баланс арендный",
        contentPath: "balance"
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
