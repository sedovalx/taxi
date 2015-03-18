/* global require, module */

var EmberApp = require('ember-cli/lib/broccoli/ember-app');

var app = new EmberApp({
  emberCliFontAwesome: { includeFontAwesomeAssets: false }
});

// Use `app.import` to add additional libraries to the generated
// output files.
//
// If you need to use different assets in different
// environments, specify an object as the first parameter. That
// object's keys should be the environment name and the values
// should be the asset to use in that environment.
//
// If the library that you are including contains AMD or ES6
// modules that you would like to import into your application
// please specify an object with the list of modules as keys
// along with the exports of each module as its value.

// Twitter Bootstrap
app.import('bower_components/bootstrap/dist/js/bootstrap.js');
app.import('bower_components/bootstrap/dist/css/bootstrap.css');
app.import('bower_components/bootstrap/dist/css/bootstrap.css.map', {
  destDir: 'assets'
});

// Ember Table
app.import('bower_components/jquery-ui/ui/jquery.ui.core.js');
app.import('bower_components/jquery-ui/ui/jquery.ui.widget.js');
app.import('bower_components/jquery-ui/ui/jquery.ui.mouse.js');
app.import('bower_components/jquery-ui/ui/jquery.ui.resizable.js');
app.import('bower_components/jquery-ui/ui/jquery.ui.sortable.js');
app.import('bower_components/jquery-mousewheel/jquery.mousewheel.js');
app.import('bower_components/antiscroll/antiscroll.js');
app.import('bower_components/antiscroll/antiscroll.css');
app.import('bower_components/ember-table/dist/ember-table.js');
app.import('bower_components/ember-table/dist/ember-table.css');

// Font Awesome
app.import("bower_components/font-awesome/css/font-awesome.css");
app.import("bower_components/font-awesome/fonts/fontawesome-webfont.eot", { destDir: "fonts" });
app.import("bower_components/font-awesome/fonts/fontawesome-webfont.svg", { destDir: "fonts" });
app.import("bower_components/font-awesome/fonts/fontawesome-webfont.ttf", { destDir: "fonts" });
app.import("bower_components/font-awesome/fonts/fontawesome-webfont.woff", { destDir: "fonts" });
app.import("bower_components/font-awesome/fonts/fontawesome-webfont.woff2", { destDir: "fonts" });
app.import("bower_components/font-awesome/fonts/FontAwesome.otf", { destDir: "fonts" });

module.exports = app.toTree();
