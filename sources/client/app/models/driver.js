/**
 * Created by Кирилл on 25.03.2015.
 */
import DS from "ember-data";

let attr = DS.attr;
var driversmodel = DS.Model.extend({
  lastName: attr("string"),
  firstName: attr("string"),
  middleName: attr("string"),
  fio: function() {
    var fio = this.get('lastName') + ' ' + this.get('firstName') + ' ' + this.get('middleName');
    return fio;
  }.property('lastName','firstName', 'middleName'),
  passport: attr("number"),
  drivingLicense: attr("number"),
  mainTel: attr("number"),
  addTel: attr("number"),
  notes: attr("string"),
  creationDate: attr("date"),
  editDate: attr("date"),
  creator: DS.belongsTo("user", {inverse: null, async: true}),
  editor: DS.belongsTo("user", {inverse: null, async: true})
});

driversmodel.FIXTURES = [
  {
    id: 1,
    lastName: 'Попов',
    firstName: 'Иван',
    middleName: 'Петрович',
    passport: 453463,
    drivingLicense: '4567567845',
    mainTel: 1234345,
    addTel: 45647,
    notes: 'test notes'
  }
];

export default driversmodel;
