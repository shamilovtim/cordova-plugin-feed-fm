var exec = require("cordova/exec");

exports.echo = function(success, error) {
  exec(success, error, "CordovaPluginFeedFm", "echo");
};
exports.initializeWithToken = function(success, error, token, secret, enableBackgroundMusic) {
  exec(success, error, "CordovaPluginFeedFm", "initializeWithToken", [
    token,
    secret,
    enableBackgroundMusic
  ]);
};
exports.requestClientId = function(success, error) {
  exec(success, error, "CordovaPluginFeedFm", "requestClientId");
};
exports.setActiveStation = function(id) {
  exec(
    function() {},
    function() {},
    "CordovaPluginFeedFm",
    "setActiveStation",
    [id]
  );
};
exports.setClientId = function(clientId) {
  exec(
    function() {},
    function() {},
    "CordovaPluginFeedFm",
    "setClientId",
    [clientId]
  );
};
exports.setVolume = function(volume) {
  exec(
    function() {},
    function() {},
    "CordovaPluginFeedFm",
    "setVolume",
    [volume]
  );
};
exports.play = function() {
  exec(
    function() {},
    function() {},
    "CordovaPluginFeedFm",
    "play"
  );
};
exports.pause = function() {
  exec(
    function() {},
    function() {},
    "CordovaPluginFeedFm",
    "pause"
  );
};
exports.skip = function() {
  exec(
    function() {},
    function() {},
    "CordovaPluginFeedFm",
    "skip"
  );
};
exports.stop = function() {
  exec(
    function() {},
    function() {},
    "CordovaPluginFeedFm",
    "stop"
  );
};
exports.createNewClientID = function() {
  exec(
    function() {},
    function() {},
    "CordovaPluginFeedFm",
    "createNewClientID"
  );
};
