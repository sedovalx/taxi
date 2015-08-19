import DS from "ember-data";
import roles from "client/models/roles";

let attr = DS.attr;
export default DS.Model.extend({
  lastName: attr("string"),
  firstName: attr("string"),
  middleName: attr("string"),
  login: attr("string"),
  password: attr("string"),
  role: attr("string"),
  creationDate: attr("date"),
  editDate: attr("date"),
  creator: DS.belongsTo("user", {inverse: null, async: true}),
  editor: DS.belongsTo("user", {inverse: null, async: true}),
  displayRole: function(){
    let role = roles.filter(r => r.id === this.get("role"))[0];
    return role ? role.label : "";
  }.property("role"),
  fullName: function(){
    let name = `${this.get("lastName") || ''} ${this.get("firstName") || ''} ${this.get("middleName") || ''}`;
    return name.trim() || this.get('login');
  }.property("firstName", "lastName", "middleName")
});
