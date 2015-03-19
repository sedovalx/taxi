import Ember from "ember";

export default Ember.ObjectController.extend({
  roles: [
    { id: "Administrator", label: "Администритор" },
    { id: "Accountant", label: "Бухгалтер" },
    { id: "Repairman", label: "Ремонтник" },
    { id: "Cashier", label: "Кассир" }
  ],
  selectedRole: null
});
