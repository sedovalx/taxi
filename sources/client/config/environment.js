/* jshint node: true */

module.exports = function(environment) {
  var ENV = {
    modulePrefix: 'client',
    environment: environment,
    baseURL: '/',
    locationType: 'auto',
    EmberENV: {
      FEATURES: {
        // Here you can enable experimental features on an ember canary build
        // e.g. 'with-controller': true
      }
    },

    APP: {
      // Here you can pass flags/options to your application instance
      // when it is created
    },
    'simple-auth': {
      authorizer: 'simple-auth-authorizer:token',
      store: 'simple-auth-session-store:local-storage'
    },
    'simple-auth-token': {
      serverTokenEndpoint: "/api/auth/login",
      identificationField: "identifier",
      passwordField: 'password',
      authorizationHeaderName: 'X-Auth-Token',
      authorizationPrefix: null,
      refreshAccessTokens: true,
      // The server returns time in seconds. It will be multiplied by the timeFactor to get milliseconds for the Date type construction
      timeFactor: 1000,
      refreshLeeway: 180,
      serverTokenRefreshEndpoint: '/api/auth/renew'
    }
  };

  if (environment === 'development') {
    // ENV.APP.LOG_RESOLVER = true;
    // ENV.APP.LOG_ACTIVE_GENERATION = true;
    // ENV.APP.LOG_TRANSITIONS = true;
    // ENV.APP.LOG_TRANSITIONS_INTERNAL = true;
    // ENV.APP.LOG_VIEW_LOOKUPS = true;
  }

  if (environment === 'test') {
    // Testem prefers this...
    ENV.baseURL = '/';
    ENV.locationType = 'none';

    // keep test console output quieter
    ENV.APP.LOG_ACTIVE_GENERATION = false;
    ENV.APP.LOG_VIEW_LOOKUPS = false;

    ENV.APP.rootElement = '#ember-testing';
  }

  if (environment === 'production') {

  }

  return ENV;
};
