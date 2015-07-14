import RentController from "client/controllers/base/rent-controller";
/* global moment */
// No import for moment, it's a global called `moment`

export default RentController.extend({
  queryParams: ['tab'],
  tab: "general",
  isGeneral: function(){
    return this.get('tab') !== 'total';
  }.property('tab'),
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
    }
  }
});
