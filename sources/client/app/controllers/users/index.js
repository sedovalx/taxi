import Ember from "ember";

export default Ember.ArrayController.extend({
  columns: function(){
    let loginColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      headerCellName: "Логин",
      contentPath: "login"
    });
    let lastNameColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      headerCellName: "Фамилия",
      contentPath: "lastName"
    });
    let firstNameColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      headerCellName: "Имя",
      contentPath: "firstName"
    });
    let middleNameColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      headerCellName: "Отчество",
      contentPath: "middleName"
    });
    let roleColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      headerCellName: "Роль",
      contentPath: "role"
    });
    return [loginColumn, lastNameColumn, firstNameColumn, middleNameColumn, roleColumn];
  }.property(),
  users: function(){
    return this;
  }.property("@each")
});
