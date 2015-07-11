import Ember from 'ember';
import statuses from 'client/models/rent-statuses';

export default Ember.Handlebars.makeBoundHelper(status => {
  let found = statuses.filter(s => s.id === status)[0];
  return found ? found.label : status;
});
