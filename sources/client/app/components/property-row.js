import Ember from "ember";
import $ from "jquery";

/**
 * @see http://guides.emberjs.com/v1.11.0/components/
 */
export default Ember.Component.extend({
  classNameBindings: ["errors:has-error"],
  labelCssWidth: "col-sm-2",
  inputCssWidth: "col-sm-10",
  errorCssWidth: "col-sm-10",
  readOnly: false,
  didInsertElement: function(){
    let $inputEl = $(this.element).find("input.ember-view, .selectize-input");
    $inputEl.addClass("form-control");
  }
});
