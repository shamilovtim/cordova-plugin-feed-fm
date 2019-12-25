var app = {
  // Application Constructor
  initialize: function() {
    logger().log("App initialized");
    app.setupListeners();

    document.addEventListener("deviceready", this.onDeviceReady.bind(this), false);
  },

  setupListeners: function() {
    // listen for initializeWithToken button
    var initializeWithToken = document.getElementById("initializeWithToken");
    initializeWithToken.addEventListener("click", app.initializeWithToken, false);

    // listen for setActiveStation button
    var setActiveStation = document.getElementById("setActiveStation");
    setActiveStation.addEventListener("click", app.setActiveStation, false);

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

    // listen for echo button
    var echo = document.getElementById("echo");
    echo.addEventListener("click", app.echo, false);
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
        console.log("initializeWithToken success returned:", msg);
        logger().log(JSON.stringify(msg, null, 4));
        logger().log("initializeWithToken success returned:");
      },
      function(err) {
        console.log("initializeWithToken fail ", err);
        logger().log("initializeWithToken fail " + JSON.stringify(err), "e");
      },
      "cc1783f3b77faefebc3b4065665cf316f1bc34d2",
      "711ac508971f37972eeef5a153239587812fc7dd",
      false
    );
  },
  echo: function() {
    console.log("echo method called");
    CordovaPluginFeedFm.echo(
      function(msg) {
        console.log("echo success", msg);
        logger().log(JSON.stringify(msg, null, 4));
        logger().log("echo success returned::");
      },
      function(err) {
        console.log("echo fail ", err);
        logger().log("echo fail " + JSON.stringify(err), "e");
      },
      "simple echo"
    );
  },
  play: function() {
    console.log("play method called");
    CordovaPluginFeedFm.play(
      function(msg) {
        console.log("play success", msg);
        logger().log(JSON.stringify(msg, null, 4));
        logger().log("play success returned::");
      },
      function(err) {
        console.log("play fail ", err);
        logger().log("play fail " + JSON.stringify(err), "e");
      },
      "FeedFM play function"
    );
  },
  pause: function() {
    console.log("pause method called");
    CordovaPluginFeedFm.pause(
      function(msg) {
        console.log("pause success ", msg);
        logger().log(JSON.stringify(msg, null, 4));
        logger().log("pause success returned:");
      },
      function(err) {
        console.log("pause fail ", err);
        logger().log("pause fail " + JSON.stringify(err), "e");
      },
      "FeedFM pause function"
    );
  },
  skip: function() {
    console.log("skip method called");
    CordovaPluginFeedFm.skip(
      function(msg) {
        console.log("skip success ", msg);
        logger().log(JSON.stringify(msg, null, 4));
        logger().log("skip success returned::");
      },
      function(err) {
        console.log("skip function fail ", err);
        logger().log("pause fail " + JSON.stringify(err), "e");
      },
      "FeedFM skip function "
    );
  },
  stop: function() {
    console.log("stop method called");
    CordovaPluginFeedFm.stop(
      function(msg) {
        console.log("stop success ", msg);
        logger().log(JSON.stringify(msg, null, 4));
        logger().log("stop success returned:");
      },
      function(err) {
        console.log("stop fail ", err);
        logger().log("stop fail " + JSON.stringify(err), "e");
      },
      "FeedFM stop function "
    );
  },
  setVolume: function() {
    console.log("setVolume method called");
    CordovaPluginFeedFm.setVolume(
      function(msg) {
        console.log("setVolume success ", msg);
        logger().log(JSON.stringify(msg, null, 4));
        logger().log("setVolume success returned:");
      },
      function(err) {
        console.log("setVolume fail ", err);
        logger().log("setVolume fail " + JSON.stringify(err), "e");
      },
      5
    );
  },
  setActiveStation: function() {
    console.log("setActiveStation method called");
    CordovaPluginFeedFm.setActiveStation(
      function(msg) {
        console.log("setActiveStation success ", msg);
        logger().log(JSON.stringify(msg, null, 4));
        logger().log("setActiveStation success returned:");
      },
      function(err) {
        console.log("setActiveStation fail ", err);
        logger().log("setActiveStation fail " + JSON.stringify(err), "e");
      },
      "175692"
    );
  }
};

app.initialize();
