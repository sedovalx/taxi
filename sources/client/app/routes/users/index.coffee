`import Ember from "ember"`

PostsRoute = Ember.Route.extend(
  model: -> @store.find("user")
)

`export default PostsRoute`
