import RentController from "client/controllers/base/rent-controller";
import TimeFormatMixin from 'client/mixins/time-format';
/* global moment */
// No import for moment, it's a global called `moment`

export default RentController.extend(TimeFormatMixin, {
  queryParams: ['panel'],
  panel: "total",
  isTotal: function(){
    return this.get('panel') === 'total';
  }.property('panel', 'tab'),
  minutesHumanize: function(){
    let minutes = this.get('rentTotal.minutes');
    let a = moment.duration(minutes, 'minutes');
    return Math.floor(a.asDays()) + ' дней ' + a.hours() + ' часов ' + a.minutes() + ' минут';
  }.property('rentTotal.minutes'),

  isTotalPositive: function(){
    return this.get('rentTotal.total') >= 0;
  }.property('rentTotal.total'),

  actions: {
    showOperation: function(operation){
      this.transitionToRoute('operations.edit', operation.operationId);
    },
    switchPanel: function(panel) {
      this.set('panel', panel);
    },
  }
});
