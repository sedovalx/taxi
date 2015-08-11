import OperationController from 'client/controllers/base/operation-controller';
import TimeFormatMixin from 'client/mixins/time-format';

export default OperationController.extend(TimeFormatMixin, {
  operationTime: function(){
    let time = this.get('changeTime');
    return this.formatDateTime(time);
  }.property('changeTime')
});
