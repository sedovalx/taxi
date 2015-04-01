import $ from "jquery";

export default {
  discardModel: function(){
    let model = this.get("model");
    model.rollback();
  },
  isDirty: function(){
    let model = this.get("model");
    return (model && !$.isEmptyObject(model.changedAttributes()));
  }
};
