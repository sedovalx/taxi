#`import Ember from "ember"`
#
#UsersController = Ember.ArrayController.extend
#  columns: Ember.computed ->
#    loginColumn = Ember.Table.ColumnDefinition.create
#      columnWidth: 200
#      textAlign: "text-align-left"
#      headerCellName: "Логин"
#      getCellContent: (row) -> row["login"]
#    [loginColumn]
#  content: Ember.computed ->
#    @.map (user) ->
#      login: user.login
#
#`export default UsersController`
