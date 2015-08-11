import Ember from 'ember';
/* global moment */
// No import for moment, it's a global called `moment`

export default Ember.Mixin.create({
  formatDateTime: function(dateTime){
    return moment(dateTime).format('DD.MM.YYYY HH:mm');
  }
});
