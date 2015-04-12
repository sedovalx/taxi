import Ember from "ember";

export default Ember.ArrayController.extend({
  sortProperties: ["name", "rate", "comment"],
  selectedRow: null,
  selectionEmpty: function(){
    return this.get("selectedRow") == null;
  }.property("selectedRow"),

  actions: {
    edit: function(){
      let row = this.get("selectedRow");
      if (row) {
        this.transitionToRoute("car-class.edit", row.id);
      }
    },
    create: function(){
      this.transitionToRoute("car-classes.new");
    },
    remove: function(){
	  let row = this.get("selectedRow");
	  if (row && confirm("Вы подтверждаете удаление класса автомобиля?")) {
		row.deleteRecord();
		row.save();
	  }
    }
  },

  // Ember Table
  columns: function(){
    let nameColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Наименование",
      contentPath: "name"
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
    return [nameColumn, rateColumn, commentColumn];
  }.property(),
  carClasses: function(){
    return this;
  }.property("@each")
});
