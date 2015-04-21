import Ember from "ember";
import $ from "jquery";

/**
 * @see http://guides.emberjs.com/v1.11.0/components/
 */
export default Ember.Component.extend({
  classNameBindings: ["errors:has-error"],
  didInsertElement: function(){
    let $inputEl = $(this.element).find("input.ember-view, .selectize-input");
    $inputEl.addClass("form-control");
  }
});
