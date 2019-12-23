var app = {
  // Application Constructor
  initialize: function() {
    document.addEventListener(
      "deviceready",
      this.onDeviceReady.bind(this),
      false
    );
  },

  onDeviceReady: function() {
    app.receivedEvent("deviceready");

    // using native code, fire the echo function
    CordovaPluginFeedFm.echo(
      "FeedFM native echo function working!",
      function(msg) {
        document
          .getElementById("deviceready")
          .querySelector(".received").innerHTML = msg;
      },
      function(err) {
        document.getElementById("deviceready").innerHTML =
          '<p class="event received">' + err + "</p>";
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
  }
};

app.initialize();
