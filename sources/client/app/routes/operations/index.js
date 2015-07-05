import ProtectedRoute from "client/routes/base/protected";
//import $ from "jquery";

export default ProtectedRoute.extend({

  queryParams: {
    accountType:{
      refreshModel: true
    }
  },
  model: function(params, transition) {
    //$.getJSON("/api/reports/q-operation-list", { rent: transition.params.rent.rent_id, accountType: 'Fine' }).then(function(data){
    //  console.log(data);
    return this.store.find("operation", { rent: transition.params.rent.rent_id, accountType: params.accountType});
  }
});
