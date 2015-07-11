import Ember from 'ember';

export default Ember.Handlebars.makeBoundHelper(tpe => {
  if (tpe === 'Rent') {
    return new Ember.Handlebars.SafeString('<i class="fa fa-rub" />');
  } else if (tpe === 'Fine') {
    return new Ember.Handlebars.SafeString('<i class="fa fa-taxi" />');
  } else if (tpe === 'Repair') {
    return new Ember.Handlebars.SafeString('<i class="fa fa-wrench" />');
  }
});
