var app = {
  init: function() {
    logger().log("App initialized");
    app.setupListeners();
  },
  initializeWithToken: function() {
    console.log("initializeWithToken method called");
    CordovaPluginFeedFm.initializeWithToken(
      function(msg) {
        switch (msg.type) {
          case "REQUESTING_SKIP":
            logger().log("did reach case REQUESTING_SKIP");
            logger().log(JSON.stringify(msg.payload, null, 4));
            break;
          case "PLAYBACK_STARTED":
            logger().log("did reach case PLAYBACK_STARTED");
            logger().log(JSON.stringify(msg.payload, null, 4));
            break;
          case "NEW_CLIENT_ID":
            logger().log("did reach case NEW_CLIENT_ID");
            logger().log(JSON.stringify(msg.payload, null, 4));
            break;
          case "SET_ACTIVE_STATION_SUCCESS":
            logger().log("did reach case SET_ACTIVE_STATION_SUCCESS");
            logger().log(JSON.stringify(msg.payload, null, 4));
            break;
          case "SET_ACTIVE_STATION_FAIL":
            logger().log("did reach case SET_ACTIVE_STATION_FAIL");
            logger().log(JSON.stringify(msg.payload, null, 4));
            break;
          case "SKIP_FAIL":
            logger().log("did reach case SKIP_FAIL");
            logger().log(JSON.stringify(msg.payload, null, 4));
            break;
          case "INITIALIZE":
            logger().log("did reach case INITIALIZE");
            logger().log(JSON.stringify(msg.payload, null, 4));
            break;
          case "WAITING_FOR_ITEM":
            logger().log("did reach case WAITING_FOR_ITEM");
            logger().log(JSON.stringify(msg.payload, null, 4));
            break;
          case "READY_TO_PLAY":
            logger().log("did reach case READY_TO_PLAY");
            logger().log(JSON.stringify(msg.payload, null, 4));
            break;
          case "PLAYING":
            logger().log("did reach case PLAYING");
            logger().log(JSON.stringify(msg.payload, null, 4));
            break;
          case "STALLED":
            logger().log("did reach case STALLED");
            logger().log(JSON.stringify(msg.payload, null, 4));
            break;
          case "COMPLETE":
            logger().log("did reach case COMPLETE");
            logger().log(JSON.stringify(msg.payload, null, 4));
            break;
          case "UNKNOWN":
            logger().log("did reach case UNKNOWN");
            logger().log(JSON.stringify(msg.payload, null, 4));
            break;
          case "OFFLINE_ONLY":
            logger().log("did reach case OFFLINE_ONLY");
            logger().log(JSON.stringify(msg.payload, null, 4));
            break;
          case "UNINITIALIZED":
            logger().log("did reach case UNINITIALIZED");
            logger().log(JSON.stringify(msg.payload, null, 4));
            break;
          case "UNAVAILABLE":
            logger().log("did reach case UNAVAILABLE");
            logger().log(JSON.stringify(msg.payload, null, 4));
            break;
          case "STATION_CHANGE":
            logger().log("did reach case STATION_CHANGE");
            logger().log(JSON.stringify(msg.payload, null, 4));
            break;
          case "READY_TO_PLAY":
            logger().log("did reach case READY_TO_PLAY");
            logger().log(JSON.stringify(msg.payload, null, 4));
            break;
          case "PAUSED":
            logger().log("did reach case PAUSED");
            logger().log(JSON.stringify(msg.payload, null, 4));
            break;
          default:
            break;
        }
      },
      function(err) {
        console.log("error1515");
      },
      "test",
      "test2",
      false
    );
  },
  echo: function() {
    console.log("echo method called");
    CordovaPluginFeedFm.echo(
      function(success) {
        console.log("echo success", success);
        logger().log(JSON.stringify(success, null, 4));
        logger().log("echo success returned:" + success);
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
    CordovaPluginFeedFm.play();
  },
  pause: function() {
    console.log("pause method called");
    CordovaPluginFeedFm.pause();
  },
  skip: function() {
    console.log("skip method called");
    CordovaPluginFeedFm.skip();
  },
  stop: function() {
    console.log("stop method called");
    CordovaPluginFeedFm.stop();
  },
  requestClientId: function() {
    console.log("requestClientId method called");
    CordovaPluginFeedFm.requestClientId(
      function(success) {
        logger().log(JSON.stringify(success.payload, null, 4));
        logger().log("requestClientId success returned:");
      },
      function(error) {}
    );
  },
  setVolume: function() {
    console.log("setVolume method called");
    CordovaPluginFeedFm.setVolume(6);
  },
  setActiveStation: function() {
    console.log("setActiveStation method called");
    CordovaPluginFeedFm.setActiveStation("190557");
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

    // listen for echo button
    var requestClientId = document.getElementById("requestClientId");
    requestClientId.addEventListener("click", app.requestClientId, false);
  }
};

app.init();
