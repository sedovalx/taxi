import Ember from 'ember';
import types from 'client/models/account-types';

export default Ember.Handlebars.makeBoundHelper(tpe => {
  let found = types.filter(t => t.id === tpe)[0];
  return found ? found.label : tpe;
});
