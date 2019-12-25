const gulp = require("gulp");
const cp = require("child_process");

// when example changes compile web files
function prepareCordova(cb) {
  cp.exec("cordova prepare", function(err, stdout, stderr) {
    console.log(stdout);
    console.log(stderr);
    cb(err);
  });
}

// when plugin changes redeploy it
function removePlugin(cb) {
  cp.exec("cordova plugin rm cordova-plugin-feed-fm", function(err, stdout, stderr) {
    console.log(stdout);
    console.log(stderr);
    cb(err);
  });
}

// when plugin changes redeploy it
function addBackPlugin(cb) {
  cp.exec("cordova plugin add ../plugin", function(err, stdout, stderr) {
    console.log(stdout);
    console.log(stderr);
    cb(err);
  });
}

exports.default = function() {
  gulp.watch(
    [
      "../plugin/**/*.js",
      "../plugin/**/*.json",
      "../plugin/**/*.xml",
      "../plugin/**/*.java",
      "../plugin/**/*.h",
      "../plugin/**/*.m",
      "../plugin/**/*.a",
      "../plugin/**/*.swift",
      "../plugin/**/*.kt"
    ],
    gulp.series(removePlugin, addBackPlugin)
  );
  gulp.watch(
    ["**/*.html", "**/*.js", "**/*.css", "**/*.xml"],
    { ignored: ["platforms/**/*.*", "node_modules/**/*.*", "plugins/**/*.*"] },
    prepareCordova
  );
};
