import Ember from 'ember';
/* global moment */
// No import for moment, it's a global called `moment`

export default Ember.Handlebars.makeBoundHelper(function(d){
  try{
    return moment(d).format('DD.MM.YYYY hh:mm');
  } catch (err) {
    return '';
  }
});
