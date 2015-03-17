import Ember from "ember";

export default Ember.ArrayController.extend({
  columns: function(){
    let loginColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Логин",
      contentPath: "login"
    });
    return [loginColumn];
  }.property(),
  users: function(){
    return this;
  }.property("@each")
});
