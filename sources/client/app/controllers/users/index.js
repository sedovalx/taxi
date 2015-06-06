import Ember from "ember";
import roles from "client/models/roles";
import ListController from "client/controllers/base/list-controller";

export default ListController.extend({
  queryParams: ['login', 'role','lastName','firstName','middleName'],
  sortProperties: ["lastName", "firstName", "middleName", "login"],

  //roles model
  roles: roles,
  //let allRoles = roles;
  //allRoles.push({id: '', label: 'Все'});
  //  return roles;
  //},
  allRoles: [ { id: 0, label: "Все" },
    { id: "Administrator", label: "Администратор" },
    { id: "Accountant", label: "Бухгалтер" },
    { id: "Repairman", label: "Ремонтник" },
    { id: "Cashier", label: "Кассир" }
  ],
  /*function(){
   let allRolesAr = roles*//*this.get('roles')*//*;
   //allRolesAr.push({id: 0, label: "Все"});
   return allRolesAr;
   },*/

  //queryParams defaults
  login: '',
  role: '',
  lastName: '',
  firstName: '',
  middleName: '',

  // связанные элементы лучше группировать
  filter: {
    lastName: '',
    firstName: '',
    middleName: '',
    login: '',
    role: ''
  },

  actions: {
    edit: function(){
      let row = this.get("selectedRow");
      if (row) {
        this.transitionToRoute("users.edit", row.id);
      }
    },
    create: function(){
      this.transitionToRoute("users.new");
    },
    remove: function(){
      let row = this.get("selectedRow");
      if (row && confirm("Вы подтверждаете удаление пользователя?")) {
        row.deleteRecord();
        row.save();
      }
    },
    filterTable: function(){
      let filter = {
        lastName: this.get('filter.lastName'),
        firstName: this.get('filter.firstName'),
        middleName: this.get('filter.middleName'),
        login: this.get('filter.login'),
        role: this.get('filter.role.id')
      };

      for (var prop in filter) {
        if (filter.hasOwnProperty(prop) && !filter[prop]){
          filter[prop] = '';
          this.set(prop, '');
        }
      }
      this.transitionToRoute("users", {queryParams: filter});
    },
    clearFilter: function(){
      this.set('filter.lastName','');
      this.set('filter.firstName','');
      this.set('filter.middleName','');
      this.set('filter.role','');
      this.set('filter.login','');
      this.set('lastName','');
      this.set('firstName','');
      this.set('middleName','');
      this.set('role', '');
      this.set('login','');
      this.transitionToRoute("users", {queryParams: null});
    }
  },

  // Ember Table
  columns: function(){
    let loginColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Логин",
      contentPath: "login"
    });
    let lastNameColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Фамилия",
      contentPath: "lastName"
    });
    let firstNameColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Имя",
      contentPath: "firstName"
    });
    let middleNameColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Отчество",
      contentPath: "middleName"
    });
    let roleColumn = Ember.Table.ColumnDefinition.create({
      savedWidth: 200,
      canAutoResize: true,
      headerCellName: "Роль",
      contentPath: "displayRole"
    });
    return [lastNameColumn, firstNameColumn, middleNameColumn, roleColumn, loginColumn];
  }.property(),
  users: function(){
    return this;
  }.property("@each")
});
