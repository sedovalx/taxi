export default {
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
};
