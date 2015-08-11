import OperationController from 'client/controllers/base/operation-controller';
import TimeFormatMixin from 'client/mixins/time-format';
import AuthMixin from 'client/mixins/auth';

export default OperationController.extend(TimeFormatMixin, AuthMixin, {
  operationTime: function(){
    let time = this.get('changeTime');
    return this.formatDateTime(time);
  }.property('changeTime'),

  readOnlyMode: function(){
    return !this.get('isAdmin');
  }.property('isAdmin')
});
