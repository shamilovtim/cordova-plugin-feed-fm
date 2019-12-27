var loggerElements = document.getElementsByClassName("logger");
var loggers = [];

var Logger = function(index) {
  var index = index;
  this.defaultColor = "#ecf0f1";
  this.log = function(s, type) {
    var p = document.createElement("p");

    if (type === "error" || type === "e") {
      p.style.color = "#e74c3c";
    } else {
      p.style.color = this.defaultColor;
    }

    if (loggerElements[index].innerHTML) {
      loggerElements[index].innerHTML = loggerElements[index].innerHTML + "<br/>";
    }

    p.appendChild(document.createTextNode(`${s}`));
    console.log(s);
    loggerElements[index].prepend(p);
  };

  this.clear = function() {
    loggerElements[index].innerHTML = "";
  };
};

for (i = 0; i < loggerElements.length; i++) {
  loggers.unshift(new Logger(i));
}

function logger(index) {
  if (index) {
    return loggers[index];
  } else {
    return loggers[0];
  }
}

exports.logger = logger();
