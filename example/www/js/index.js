var app = {
  // Application Constructor
  initialize: function() {
    app.setupListeners();

    document.addEventListener("deviceready", this.onDeviceReady.bind(this), false);
  },

  setupListeners: function() {
    // listen for initializeWithToken button
    var initializeWithToken = document.getElementById("initializeWithToken");
    initializeWithToken.addEventListener("click", app.initializeWithToken, false);

    // listen for play button
    var play = document.getElementById("play");
    play.addEventListener("click", app.play, false);

    // listen for pause button
    var pause = document.getElementById("pause");
    pause.addEventListener("click", app.pause, false);

    // listen for skip button
    var skip = document.getElementById("skip");
    skip.addEventListener("click", app.skip, false);

    // listen for stop button
    var stop = document.getElementById("stop");
    stop.addEventListener("click", app.stop, false);

    // listen for setVolume button
    var setVolume = document.getElementById("setVolume");
    setVolume.addEventListener("click", app.setVolume, false);
  },

  onDeviceReady: function() {
    app.receivedEvent("deviceready");

    // using native code, fire the echo function
    CordovaPluginFeedFm.echo(
      "FeedFM native echo function working!",
      function(msg) {
        document.getElementById("deviceready").querySelector(".received").innerHTML = msg;
      },
      function(err) {
        document.getElementById("deviceready").innerHTML = '<p class="event received">' + err + "</p>";
      }
    );

    // using js only code, fire the echojs function
    CordovaPluginFeedFm.echojs(
      "FeedFM js only echojs function working",
      function(msg) {
        document.getElementsByTagName("h1")[0].innerHTML = msg;
      },
      function(err) {
        document.getElementsByTagName("h1")[0].innerHTML = err;
      }
    );
  },

  // Update DOM on a Received Event
  receivedEvent: function(id) {
    var parentElement = document.getElementById(id);
    var listeningElement = parentElement.querySelector(".listening");
    var receivedElement = parentElement.querySelector(".received");

    listeningElement.setAttribute("style", "display:none;");
    receivedElement.setAttribute("style", "display:block;");

    console.log("Received Event: " + id);
  },
  initializeWithToken: function() {
    console.log("initializeWithToken method called");
    CordovaPluginFeedFm.initializeWithToken(
      function(msg) {
        console.log("initializeWithToken function success ", msg);
      },
      function(err) {
        console.log("initializeWithToken function fail ", err);
      },
      "cc1783f3b77faefebc3b4065665cf316f1bc34d2",
      "711ac508971f37972eeef5a153239587812fc7dd"
    );
  },
  play: function() {
    console.log("play method called");
    CordovaPluginFeedFm.play(
      function(msg) {
        console.log("play function success", msg);
      },
      function(err) {
        console.log("play function fail", err);
      },
      "FeedFM play function"
    );
  },
  pause: function() {
    console.log("pause method called");
    CordovaPluginFeedFm.pause(
      function(msg) {
        console.log("pause function success ", msg);
      },
      function(err) {
        console.log("pause function fail ", err);
      },
      "FeedFM pause function"
    );
  },
  skip: function() {
    console.log("skip method called");
    CordovaPluginFeedFm.skip(
      function(msg) {
        console.log("skip function success", msg);
      },
      function(err) {
        console.log("skip function fail", err);
      },
      "FeedFM skip function "
    );
  },
  stop: function() {
    console.log("stop method called");
    CordovaPluginFeedFm.stop(
      function(msg) {
        console.log("stop function success ", msg);
      },
      function(err) {
        console.log("stop function fail ", err);
      },
      "FeedFM stop function "
    );
  },
  setVolume: function() {
    console.log("setVolume method called");
    CordovaPluginFeedFm.setVolume(
      function(msg) {
        console.log("setVolume function success ", msg);
      },
      function(err) {
        console.log("setVolume function fail ", err);
      },
      5
    );
  }
};

app.initialize();
