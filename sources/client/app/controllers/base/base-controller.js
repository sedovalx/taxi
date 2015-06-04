import Ember from "ember";
import DirtyControllerMixin from "client/controllers/base/dirty-controller-mixin";

/* global history */
// No import for history, it's a global called `history`

export default Ember.ObjectController.extend(DirtyControllerMixin, {
  actions: {
    save: function(){
      let model = this.get("model");
      model
        .save()
        .then(() => history.back());
    },
    cancel: function(){
      history.back();
    }
  }
});
