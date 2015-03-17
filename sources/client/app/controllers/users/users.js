//import Ember from "ember";
//
//export default Ember.ArrayController.extend({
//  columns: Ember.computed(() => {
//    let loginColumn = Ember.Table.ColumnDefinition.create({
//      columnWidth: 200,
//      textAlign: "text-align-left",
//      headerCellName: "Логин",
//      getCellContent: (row) => row["login"]
//    });
//    return [loginColumn];
//  }),
//  content: Ember.computed(() => {
//    return this.map((user) => user.login);
//  })
//});
