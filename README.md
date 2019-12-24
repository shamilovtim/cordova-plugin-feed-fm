<!-- PROJECT SHIELDS -->

[![Contributors][contributors-shield]]()
[![code style: prettier](https://img.shields.io/badge/code_style-prettier-ff69b4.svg?style=flat-square)](https://github.com/prettier/prettier)

<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="#">
    <img src="https://feed.fm/images/feedfm-logo-greyred.png" alt="Logo">
  </a>

  <h3 align="center">Cordova plugin for Feed.FM FeedMedia native SDK</h2>
  <h4 align="center">By: Timothy Shamilov</h4>

  <p align="center">
    Uses the FeedMedia CocoaPod on iOS
  </p>
</p>

# Prerequisites

- Cordova-iOS 5.x.x or higher
- Cordova-Android 8.x.x or higher

### To do, future enhancements:

- Add ionic native wrapper

# Directory Structure

```sh
.
├── example       # cordova example project
├── plugin        # plugin source
```

# Plugin Development

The dev environment used for this project was macOS. Instructions for any other OS aren't provided and will be unreliable.

You will need the following global dependencies:

- npm
- cordova

## Setting up local env

1. terminal window 1: `cd example`
2. terminal window 1: `cordova platform add <yourplatform>`
3. terminal window 1: `npm run watch &`
4. new terminal window 2: `cordova emulate <yourplatform>`

## Local workflow

The plugin in `../example` is symlinked to `../plugin`. That means all changes in the .h and .m files in the former will appear in the latter and vice versa.

Work on iOS native code in xCode and Android native code in Android Studio.

The plugin development env features automatic compilation. To use:

```shell
npm run watch &
```

This watches two locations:

- in ../example/ it watches html, css, js and assets. upon updates it compiles that code.
- in ../plugin/ it watches js, xml and json. upon updates it re-adds the plugin.

## Key Commands

- `npm run watch &` in ../example/ persistently runs nodemon to watch for changes
- `cordova prepare` in ../example/ this compiles non-native assets (html/css/js)
- `cordova plugin rm cordova-plugin-feed-fm && cordova plugin add --link ../plugin` in ../example/ reinstall plugin, useful for js changes

<!-- MARKDOWN LINKS & IMAGES -->

[contributors-shield]: https://img.shields.io/badge/contributors-1-orange.svg?style=flat-square
[product-screenshot]: https://raw.githubusercontent.com/othneildrew/Best-README-Template/master/screenshot.png
