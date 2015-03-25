/**
 * Created by Кирилл on 25.03.2015.
 */
import DS from "ember-data";

let attr = DS.attr;
var driversmodel = DS.Model.extend({
  lastName: attr("string"),
  firstName: attr("string"),
  middleName: attr("string"),
  passport: attr("string"),
  creationDate: attr("date"),
  editDate: attr("date"),
  creator: DS.belongsTo("user", {inverse: null, async: true}),
  editor: DS.belongsTo("user", {inverse: null, async: true})
});


export default driversmodel;
