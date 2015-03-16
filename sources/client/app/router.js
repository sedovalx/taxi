import Ember from 'ember';
import config from './config/environment';

var Router = Ember.Router.extend({
  location: config.locationType
});

Router.map(function() {
  //users/
  this.resource("users", function(){
    //users/new
    this.route("new");
  });
  //users/123
  this.resource("user", { path: "/users/:user_id" }, function(){
    // url: users/123/edit
    // route name: user.edit
    // controller: UserEditController
    // route: UserEditRoute
    // template: user/edit
    this.route("edit");
    //users/123/view
    this.route("view");
  });
  this.resource("cars", function(){
    this.route("new");
  })
});

export default Router;
