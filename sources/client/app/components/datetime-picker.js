import Ember from 'ember';
import $ from 'jquery';
/* global moment */
// No import for moment, it's a global called `moment`

export default Ember.Component.extend({
  value: null,
  format: 'DD.MM.YYYY HH:mm:ss',
  locale: 'ru',
  allowInputToggle: true,
  icons: {
    time: 'fa fa-clock-o',
    date: 'fa fa-calendar',
    up: 'fa fa-chevron-up',
    down: 'fa fa-chevron-down',
    previous: 'fa fa-chevron-left',
    next: 'fa fa-chevron-right',
    today: 'fa fa-dot-circle-o',
    clear: 'fa fa-trash-o',
    close: 'fa fa-times'
  },
  init: function(){
    let value = this.get('value') || new Date();
    this.set('initValue', moment(value).format(this.get('format')));
    this._super();
  },
  didInsertElement: function(){
    let self = this;
    let elementId = this.get('elementId');
    let $picker = $('#' + elementId + ' .input-group');
    $picker.datetimepicker({
      format: this.get('format'),
      locale: this.get('locale'),
      icons: this.get('icons'),
      allowInputToggle: this.get('allowInputToggle')
    });
    $picker.on('dp.change', function(){
      let momentDate = $picker.data('DateTimePicker').date();
      self.set('value', momentDate.toDate());
    });
  }
});
