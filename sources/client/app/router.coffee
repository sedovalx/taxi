`import Ember from 'ember'`
`import config from './config/environment'`

Router = Ember.Router.extend(
  location: config.locationType
)

Router.map ->
  @resource("users",  ->
    @route "new"
  )
  @resource("user", { path: "/users/:user_id" }, ->
    @route "edit"
    @route "view"
  )
  @resource("cars", ->
    @route "new"
  )

`export default Router`

