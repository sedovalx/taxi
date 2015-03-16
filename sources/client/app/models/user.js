import DS from "ember-data";

var attr = DS.attr;
var User = DS.Model.extend({
  lastName: attr("string"),
  firstName: attr("string"),
  middleName: attr("string"),
  login: attr("string"),
  password: attr("string"),
  role: attr("string"),
  creationDate: attr("date"),
  editDate: attr("date"),
  creator: DS.belongsTo("user", { inverse: null }),
  editor: DS.belongsTo("user", { inverse: null })
});

export default User;
