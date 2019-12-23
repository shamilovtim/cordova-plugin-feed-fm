var exec = require("cordova/exec");

exports.echo = function(success, error, arg0) {
  exec(success, error, "CordovaPluginFeedFm", "echo", [arg0]);
};
exports.initializeWithToken = function(success, error, token, secret, enableBackgroundMusic) {
  exec(success, error, "CordovaPluginFeedFm", "initializeWithToken", [
    token,
    secret,
    enableBackgroundMusic
  ]);
};
exports.play = function(success, error, arg0) {
  exec(success, error, "CordovaPluginFeedFm", "play", [arg0]);
};
exports.pause = function(success, error, arg0) {
  exec(success, error, "CordovaPluginFeedFm", "pause", [arg0]);
};
exports.skip = function(success, error, arg0) {
  exec(success, error, "CordovaPluginFeedFm", "skip", [arg0]);
};
exports.stop = function(success, error, arg0) {
  exec(success, error, "CordovaPluginFeedFm", "stop", [arg0]);
};
exports.setVolume = function(success, error, arg0) {
  exec(success, error, "CordovaPluginFeedFm", "setVolume", [arg0]);
};
exports.echojs = function(success, error, arg0) {
  if (arg0 && typeof arg0 === "string" && arg0.length > 0) {
    success(arg0);
  } else {
    error("Empty message!");
  }
};
