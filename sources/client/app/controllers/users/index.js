import Ember from "ember";
import roles from "client/models/roles";

export default Ember.ArrayController.extend({
  queryParams: ['login', 'role','lastName','firstName','middleName'],
  sortProperties: ["lastName", "firstName", "middleName", "login"],
  selectedRow: null,

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

  //variables for filter forms on templates
  filterLastName: '',
  filterFirstName: '',
  filterMiddleName: '',
  filterRole: '',/*{ id: 0, label: "Все" },*/
  filterLogin: '',

  selectionEmpty: function(){
    return this.get("selectedRow") == null;
  }.property("selectedRow"),
  actions: {
    edit: function(){
      let row = this.get("selectedRow");
      if (row) {
        this.transitionToRoute("user.edit", row.id);
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
      let filterLastName = this.get('filterLastName');
      let filterFirstName = this.get('filterFirstName');
      let filterMiddleName = this.get('filterMiddleName');
      let filterRole = this.get('filterRole.id');
      let filterLogin = this.get('filterLogin');
      let filter = {};
      filter.lastName = filterLastName;
      filter.firstName = filterFirstName;
      filter.middleName = filterMiddleName;
      filter.login = filterLogin;
      filter.role = filterRole;

      for (var prop in filter){
        if (!filter[prop]){
          filter[prop] = '';
          this.set(prop, '');
        }
      }
      this.transitionToRoute("users", {queryParams: filter});
    },
    clearFilter: function(){
      this.set('filterLastName','');
      this.set('filterFirstName','');
      this.set('filterMiddleName','');
      this.set('filterRole','');
      this.set('filterLogin','');
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
