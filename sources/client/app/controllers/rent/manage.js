import RentController from "client/controllers/base/rent-controller";
import TimeFormatMixin from 'client/mixins/time-format';

export default RentController.extend(TimeFormatMixin, {
  creationDateFormatted: function(){
    return this.formatDateTime(this.get('creationDate'));
  }.property('creationDate'),

  disabledSetActive: function(){
    // активизировать если, что-угодно кроме приостановлена
    return !this.get("rentIsSuspended");
  }.property("status"),
  disabledSetSuspended: function(){
    // приостановить нельзя, если уже приостановлена
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
