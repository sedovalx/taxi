import Ember from 'ember';

export default Ember.Mixin.create({
  currentUser: function(){
    return this.get('session.currentUser');
  }.property('session.currentUser'),

  isAuthenticated: function(){
    return this.get('currentUser') != null;
  }.property('session.currentUser'),

  isAdmin: function(){
    return this.hasRole('admin');
  }.property('session.currentUser'),

  hasRole: function(role){
    return this.get('isAuthenticated') && this.get('currentUser').get('role') === role;
  }
});
