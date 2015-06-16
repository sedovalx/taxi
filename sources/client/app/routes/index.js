import Ember from "ember";

export default Ember.Route.extend({
  beforeModel: function(){
    // при переходе в корень сайта делаем редирект на данный список
    this.transitionTo("cashier-list");
  }
});
