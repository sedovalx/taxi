import Ember from "ember";
import JWTAuthenticator from 'simple-auth-token/authenticators/jwt';
import config from "../config/environment";
import $ from "jquery";

let authorizationHeaderName = config["simple-auth-token"].authorizationHeaderName;

export default JWTAuthenticator.extend({

  makeRequest: function(url, data, options) {
    let that = this;
    options = options || {};
    return Ember.$.ajax({
      url: url,
      type: 'POST',
      data: JSON.stringify(data),
      dataType: 'json',
      contentType: 'application/json',
      beforeSend: function(xhr, settings) {
        xhr.setRequestHeader('Accept', settings.accepts.json);
      },
      success: function(data, status, xhr){
        // todo: хак, чтобы получить токен из заголовка
        if (options.addToken) {
          data[that.tokenPropertyName] = xhr.getResponseHeader(authorizationHeaderName);
        }
      },
      headers: $.extend(options.headers, this.headers)
    });
  },
  refreshAccessToken: function(token) {
    var _this = this,
      data = {
        token: token
      };

    return new Ember.RSVP.Promise(function(resolve, reject) {
      let options = {
        addToken: true,
        headers: {}
      };
      options.headers[authorizationHeaderName] = token;
      _this.makeRequest(_this.serverTokenRefreshEndpoint, data, options).then(function(response) {
        Ember.run(function () {
          var token = response[_this.tokenPropertyName],
            tokenData = _this.getTokenData(token),
            expiresAt = tokenData[_this.tokenExpireName],
            tokenExpireData = {};

          tokenExpireData[_this.tokenExpireName] = expiresAt;

          data = Ember.merge(response, tokenExpireData);

          _this.scheduleAccessTokenRefresh(expiresAt, response.token);
          _this.trigger('sessionDataUpdated', data);

          resolve(response);
        });
      }, function(xhr, status, error) {
        Ember.Logger.warn('Access token could not be refreshed - server responded with ' + error + '.');
        reject();
      });
    });
  }
});
