/**
 * Created by Кирилл on 18.03.2015.
 */
import Ember from "ember";
import AuthMixin from 'client/mixins/auth';

export default Ember.Component.extend(AuthMixin, {
  displayName: function(){
    let user = this.get('currentUser');
    if (user){
      return user.get('fullName');
    } else {
      return 'Вход не выполнен';
    }
  }.property("session.currentUser.lastName", "session.currentUser.firstName"),
  actions: {
    invalidateSession: function(){
      Ember.$.ajax("/api/auth/logout");
      this.get("session").invalidate();
    }
  }
});
