/**
 * Created by Кирилл on 25.03.2015.
 */
import DS from "ember-data";

let attr = DS.attr;
var DriversModel = DS.Model.extend({
  lastName: attr("string"),
  firstName: attr("string"),
  middleName: attr("string"),
  fio: function() {
    var fio = this.get('lastName') + ' ' + this.get('firstName') + ' ' + this.get('middleName');
    return fio;
  }.property('lastName','firstName', 'middleName'),
  pass: attr("string"),
  address: attr("string"),
  license: attr("string"),
  phone: attr("string"),
  secPhone: attr("string"),
  comment: attr("string"),
  creationDate: attr("date"),
  editDate: attr("date"),
  creator_id: DS.belongsTo("user", {inverse: null, async: true}),
  editor_id: DS.belongsTo("user", {inverse: null, async: true})
});

export default DriversModel;
