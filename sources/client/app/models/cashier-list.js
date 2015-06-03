import DS from 'ember-data';
import statuses from 'client/models/rent-statuses';

let attr = DS.attr;
var CashierListModel = DS.Model.extend({
  car: attr('String'),
  rentId: attr('Number'),
  driver: attr('String'),
  presence: attr('Boolean'),
  repairs: attr('Number'),
  fines: attr('Number'),
  balance: attr('Number'),
  mileage: attr('Number'),
  service: attr('Number'),
  status: attr('String'),

  displayStatus: function(){
    let status = statuses.filter(r => r.id === this.get('status'))[0];
    return status ? status.label : this.get('status');
  }.property('status')
});

export default CashierListModel;
