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
    return this.get('lastName') + ' ' + this.get('firstName') + ' ' + this.get('middleName');
  }.property('lastName','firstName', 'middleName'),
  pass: attr("string"),
  address: attr("string"),
  license: attr("string"),
  phone: attr("string"),
  secPhone: attr("string"),
  comment: attr("string"),
  creationDate: attr("date"),
  editDate: attr("date"),
  creator: DS.belongsTo("user", {inverse: null, async: true}),
  editor: DS.belongsTo("user", {inverse: null, async: true}),
  displayName: function(){
    return this.get('fio') + ' ' + this.get('pass');
  }.property('fio','pass')
});

export default DriversModel;
