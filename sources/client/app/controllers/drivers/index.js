import Ember from "ember";

export default Ember.ArrayController.extend({
  sortProperties: ["lastName", "firstName", "middleName", "passport"],
  selectedRow: null,
  selectionEmpty: function(){
    return this.get("selectedRow") == null;
  }.property("selectedRow"),

  actions: {
    edit: function(){
      let row = this.get("selectedRow");
      if (row) {
        this.transitionToRoute("driver.edit", row.id);
      }
    },
    create: function(){
      this.transitionToRoute("drivers.new");
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
    let passportColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Паспорт",
      contentPath: "passport"
    });
    let drivingLicenseColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Водительские права",
      contentPath: "drivingLicense"
    });
    let addressColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Адрес",
      contentPath: "address"
    });
    let mainPhoneColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Основной телефон",
      contentPath: "mainPhone"
    });
    let addPhoneColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Дополнительный телефон",
      contentPath: "addPhone"
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
    return [lastNameColumn, firstNameColumn, middleNameColumn, passportColumn, drivingLicenseColumn, addressColumn];
  }.property(),
  drivers: function(){
    return this;
  }.property("@each")
});
