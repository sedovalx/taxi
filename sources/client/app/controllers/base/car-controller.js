import Ember from "ember";
import DirtyControllerMixin from "client/controllers/base/dirty-controller-mixin";

export default Ember.ObjectController.extend(DirtyControllerMixin, {
  //getter and setter for classID attribute
  selectedCarClass: function(key, value){
    let model = this.get("model");
    if (!model) {
      return;
    }

    if (arguments.length > 1){
      var carClassObj = this.get("carClasses").filter(r => r.id === value.id)[0];
      model.set("classID", value != null ? carClassObj : null);
      return this.get("carClasses").filter(r => r.id === value.id)[0];
    }
    return this.get("carClasses").filter(r => r.id === model.get("classID").get("id"))[0];
  }.property("model"),
  actions: {
    save: function(){
      let that = this;
      let model = this.get("model");
      model
        .save()
        .then(() => that.transitionToRoute("cars"))
        .catch(error => {
          alert(error);
        });
    },
    cancel: function(){
      this.transitionToRoute("cars");
    }
  },
  hasErrors: function(){
    let model = this.get("model");
    return model && !(model.get("regNumber") && model.get("make") && model.get("cmodel") &&
            model.get("mileage") && model.get("service") && this.get("selectedCarClass") );
  }.property("model.regNumber","model.make","model.cmodel","model.mileage","model.service","selectedCarClass"),
  ////array with car-classes
  //carClassesMapped: function(){
  //  var rObj = {};
  //  this.get("carClasses").forEach(function(element, index, array){
  //        newArr[index] = {label: element.name, id: element.id};
  //      });
  //  rObj['id'] = obj.id;
  //  rObj['label'] = obj.name;
  //  rObj['name'] = "test";
  //  return rObj;
  //  //return this.store.find("car-class").then(items => items.map(function(obj){}));
  //}.property("carClasses","@each"),
  //carClassesMapped: function(){
  //  var newArr = {};
  //  this.get("carClasses").forEach(function(element, index, array){
  //    newArr[index] = {label: element.name, id: element.id};
  //  });
  //  return newArr;
  //  //return this.store.find("car-class").then(items => items.map(item => {
  //  //      var newArr = {};
  //  //      newArr['id'] = item.id;
  //  //      newArr['label'] = item.name;
  //  //      return newArr;
  //  //    }
  //  //)
  //  //);
  //}.property("@each","carClasses"),
  carClasses: function(){
    return this.store.find("car-class");
  }.property("@each")
  //carClassesTest: function(){
  //  this.store.find("car-class").then(items => items.map(item => { id: item.id, label: item.name }));
  //}.property("@each"),
});
