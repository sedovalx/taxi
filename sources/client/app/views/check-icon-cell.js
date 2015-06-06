import Ember from 'ember';

export default Ember.Table.TableCell.extend({
  templateName: 'views/icon-cell',
  classNames: ['ember-table-cell', 'icon-cell'],
  iconClass: 'fa fa-check',

  showIcon: function(){
    var properties = this.getProperties('column', 'row');
    var column = properties.column;
    var row = properties.row;
    if (!(column && row)) {
      return 0;
    }
    return this.get('cellContent');
  }.property('column', 'row', 'cellContent')
});
