import DS from 'ember-data';
import statuses from 'client/models/rent-statuses';

let attr = DS.attr;
var CashierListModel = DS.Model.extend({
  rentId: attr('Number'),
  rentCreationDate: attr('Date'),
  driverId: attr('Number'),
  car: attr('String'),
  driver: attr('String'),
  presence: attr('Boolean'),
  payments: attr('Number'),
  fines: attr('Number'),
  repairs: attr('Number'),
  total: attr('Number'),
  mileage: attr('Number'),
  service: attr('Number'),
  status: attr('String'),

  displayStatus: function(){
    let status = statuses.filter(r => r.id === this.get('status'))[0];
    return status ? status.label : this.get('status');
  }.property('status')
});

export default CashierListModel;
