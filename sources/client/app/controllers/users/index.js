import Ember from "ember";

export default Ember.ArrayController.extend({
  sortProperties: ["lastName", "firstName", "middleName", "login"],
  selectedRow: null,
  selectionEmpty: function(){
    return this.get("selectedRow") == null;
  }.property("selectedRow"),

  actions: {
    edit: function(){
      let row = this.get("selectedRow");
      if (row) {
        this.transitionToRoute("user.edit", row.id);
      }
    },
    create: function(){
      this.transitionToRoute("users.new");
    },
    remove: function(){
	  let row = this.get("selectedRow");
	  if (row && confirm("Вы подтверждаете удаление пользователя?")) {
		row.deleteRecord();
		row.save();
	  }
    }
  },

  // Ember Table
  columns: function(){
    let loginColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Логин",
      contentPath: "login"
    });
    let lastNameColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Фамилия",
      contentPath: "lastName"
    });
    let firstNameColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Имя",
      contentPath: "firstName"
    });
    let middleNameColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Отчество",
      contentPath: "middleName"
    });
    let roleColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Роль",
      contentPath: "displayRole"
    });
    return [lastNameColumn, firstNameColumn, middleNameColumn, roleColumn, loginColumn];
  }.property(),
  users: function(){
    return this;
  }.property("@each")
});
