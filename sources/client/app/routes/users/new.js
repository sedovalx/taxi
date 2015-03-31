import ProtectedRoute from "../protected";

export default ProtectedRoute.extend({
  model: function(){
    return this.store.createRecord("user");
  },
  actions: {
    willTransition: function(transition){
      if (this.controller.isDirty() && !confirm("Имеются несохраненные изменения. Вы действительно хотите выйти из редактора?")) {
        transition.abort();
      } else {
        this.controller.discardModel();
        return true;
      }
    }
  }
});
