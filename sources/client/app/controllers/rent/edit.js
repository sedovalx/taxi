import RentController from "client/controllers/base/rent-controller";
/* global moment */
// No import for moment, it's a global called `moment`

export default RentController.extend({
  queryParams: ['tab', 'panel'],
  tab: "general",
  panel: "total",
  isGeneral: function(){
    return this.get('tab') === 'general';
  }.property('tab'),
  isTotal: function(){
    return !this.get('isGeneral') && this.get('panel') === 'total';
  }.property('panel'),
  minutesHumanize: function(){
    let minutes = this.get('rentTotal.minutes');
    let a = moment.duration(minutes, 'minutes');
    return Math.floor(a.asDays()) + ' дней ' + a.hours() + ' часов ' + a.minutes() + ' минут';
  }.property('rentTotal.minutes'),
  actions: {
    showOperation: function(operation){
      this.transitionToRoute('operations.edit', operation.operationId);
    },
    changeTab: function(tab) {
      this.set("tab", tab);
    },
    switchPanel: function(panel) {
      this.set('panel', panel);
    }
  }
});
