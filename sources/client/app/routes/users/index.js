import ProtectedRoute from "client/routes/base/protected";

export default ProtectedRoute.extend({
  queryParams: {
    firstName:{
      refreshModel: true
    },
    lastName:{
      refreshModel: true
    },
    middleName:{
      refreshModel: true
    },
    login: {
      refreshModel: true
    },
    role:{
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
    return this.store.find('user', params);
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
  }
});
