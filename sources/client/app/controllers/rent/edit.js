import RentController from "client/controllers/base/rent-controller";
import TimeFormatMixin from 'client/mixins/time-format';
/* global moment */
// No import for moment, it's a global called `moment`

export default RentController.extend(TimeFormatMixin, {
  queryParams: ['tab', 'panel'],
  tab: "general",
  panel: "total",
  isGeneral: function(){
    return this.get('tab') === 'general';
  }.property('tab'),
  isTotal: function(){
    return !this.get('isGeneral') && this.get('panel') === 'total';
  }.property('panel', 'tab'),
  minutesHumanize: function(){
    let minutes = this.get('rentTotal.minutes');
    let a = moment.duration(minutes, 'minutes');
    return Math.floor(a.asDays()) + ' дней ' + a.hours() + ' часов ' + a.minutes() + ' минут';
  }.property('rentTotal.minutes'),
  creationDateFormatted: function(){
    return this.formatDateTime(this.get('creationDate'));
  }.property('creationDate'),

  isTotalPositive: function(){
    return this.get('rentTotal.total') >= 0;
  }.property('rentTotal.total'),

  disabledSetActive: function(){
    // активизировать нельзя, если уже активна
    return this.get("rentIsActive");
  }.property("status"),
  disabledSetSuspended: function(){
    // приостановить нельзя, если неактивна
    return !this.get("rentIsActive");
  }.property("status"),
  disabledSetSettlingUp: function(){
    // нельзя отправить под расчет, если уже под расчтом или закрыта
    return this.get("rentIsSettlingUp") || this.get("rentIsClosed");
  }.property("status"),
  disabledSetClosed: function(){
    // нельзя закрыть, если не под расчетом
    return !this.get("rentIsSettlingUp");
  }.property("status"),

  rentIsActive: function(){
    return this.get("status") === "Active";
  }.property("status"),
  rentIsSuspended: function(){
    return this.get("status") === "Suspended";
  }.property("status"),
  rentIsSettlingUp: function(){
    return this.get("status") === "SettlingUp";
  }.property("status"),
  rentIsClosed: function(){
    return this.get("status") === "Closed";
  }.property("status"),

  actions: {
    showOperation: function(operation){
      this.transitionToRoute('operations.edit', operation.operationId);
    },
    changeTab: function(tab) {
      this.set("tab", tab);
    },
    switchPanel: function(panel) {
      this.set('panel', panel);
    },
    createRefund: function(){
      this.transitionToRoute("refunds.new", this.get("id"));
    },
    createPaymentOperation: function(){
      this.transitionToRoute("operations.new", this.get("id"),"payment");
    },
    createChargeOperation: function(){
      this.transitionToRoute("operations.new", this.get("id"),"charge");
    },
    suspendRent:function(){
      let rentId = this.get("id");
      this.store.find("rent",rentId).then(rent => {
        rent.set("status", "Suspended");
        rent.save();
        this.set("status", "Suspended");
      });
    },
    settleUpRent:function(){
      let rentId = this.get("id");
      this.store.find("rent",rentId).then(rent => {
        rent.set("status", "SettlingUp");
        rent.save();
        this.set("status", "SettlingUp");
      });
    },
    closeRent:function(){
      let rentId = this.get("id");
      this.store.find("rent",rentId).then(rent => {
        rent.set("status", "Closed");
        rent.save();
        this.set("status", "Closed");
      });
    },
    resumeRent:function(){
      let rentId = this.get("id");
      this.store.find("rent",rentId).then(rent => {
        rent.set("status", "Active");
        rent.save();
        this.set("status", "Active");
      });
    }
  }
});
