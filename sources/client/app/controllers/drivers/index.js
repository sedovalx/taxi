import Ember from "ember";

export default Ember.ArrayController.extend({
  sortProperties: ["lastName", "firstName", "middleName", "pass"],
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
	  if (row && confirm("Вы подтверждаете удаление водителя?")) {
		//row.deleteRecord();
		row.save().then(() => row.deleteRecord(), error => console.error(error));
	  }
    }
  },

  // Ember Table
  columns: function(){
    let passColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Паспорт",
      contentPath: "pass"
    });
    let licenseColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Водительские права",
      contentPath: "license"
    });
    let addressColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Адрес",
      contentPath: "address"
    });
    let phoneColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Основной телефон",
      contentPath: "phone"
    });
    let secPhoneColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Дополнительный телефон",
      contentPath: "secPhone"
    });
    let commentColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Примечания",
      contentPath: "comment"
    });
    //let lastNameColumn = Ember.Table.ColumnDefinition.create({
    //  savedWidth: 200,
    //  canAutoResize: true,
    //  headerCellName: "Фамилия",
    //  contentPath: "lastName"
    //});
    //let firstNameColumn = Ember.Table.ColumnDefinition.create({
    //  savedWidth: 200,
    //  canAutoResize: true,
    //  headerCellName: "Имя",
    //  contentPath: "firstName"
    //});
    //let middleNameColumn = Ember.Table.ColumnDefinition.create({
    //  savedWidth: 200,
    //  canAutoResize: true,
    //  headerCellName: "Отчество",
    //  contentPath: "middleName"
    //});
    let fioColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "ФИО",
      contentPath: "fio"
    });
    return [fioColumn, passColumn, licenseColumn, phoneColumn, secPhoneColumn, addressColumn, commentColumn];
  }.property(),
  drivers: function(){
    return this;
  }.property("@each")
});
