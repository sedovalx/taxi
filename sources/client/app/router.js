import Ember from 'ember';
import config from './config/environment';

let Router = Ember.Router.extend({
  location: config.locationType
});

Router.map(function() {
  let dsl = this;
  dsl.resource("users", () => {
    dsl.route("new");
  });
  dsl.resource("user", { path: "/users/:user_id" }, () => {
    dsl.route("edit");
    dsl.route("view");
  });
  dsl.resource("cars", () => {
    dsl.route("new");
  });
});


export default Router;

