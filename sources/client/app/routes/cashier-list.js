import ProtectedRoute from "client/routes/base/protected";

export default ProtectedRoute.extend({
  queryParams: {
    car:{
      refreshModel: true
    },
    driver:{
      refreshModel: true
    },
    date:{
      refreshModel: true
    }
  },
  model: function(params) {
    // TODO: это тут что такое делается?
    // TODO: здесь удалаяются пустые queryParams, чтобы их не было в адресной строке
    for(var prop in params){
      if (!params[prop]){
        delete params[prop];
      }
    }
    // This gets called upon entering 'articles' route
    // for the first time, and we opt into refiring it upon
    // query param changes by setting `refreshModel:true` above.

    // params has format of { category: "someValueOrJustNull" },
    // which we can just forward to the server.
    return this.store.find('cashier-list', params);
  },
  resetController: function (controller, isExiting) {
    if (isExiting) {
      if (isExiting) {
        var queryParams = controller.get('queryParams');
        for (var i = 0; i < queryParams.length; i++) {
          controller.set(queryParams[i], '');
        }
      }
    }
  },
  actions: {
    invalidateModel: function() {
      Ember.Logger.log('Route is now refreshing...');
      this.refresh();
    }
  }
  /*model: function(){
    return this.store.find("cashier-list");
  }*/
});
