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

# Directory Structure

```sh
.
├── example       # cordova example project
├── plugin        # plugin source
```

# Usage

## Prerequisites

- Cordova-iOS 5.x.x or higher
- Cordova-Android 8.x.x or higher

## Library

initialize the plugin

```typescript
CordovaPluginFeedFm.initializeWithToken(success: Function, error: Function, token: string, secret: string, enableBackgroundMusic: boolean);
```

set active station

```typescript
CordovaPluginFeedFm.setActiveStation(success: Function, error: Function, stationId: string);

/// returns

{activeStationId: "175691", available: true, stations: [{id: "175691", name: "Top40", options: {}}, {id: "175692", name: "Electronic", options: {}}, {id: "175693", name: "Hip Hop", options: {}}, …]}
```

# Development

The dev environment used for this project was macOS. Instructions for any other OS aren't provided and will be unreliable.

You will need the following global dependencies:

- npm
- cordova
- gulp-cli

## Setting up local env

1. `cd example`
2. `cordova platform add platformname`
3. `gulp`
4. `cordova emulate platformname`

## Local workflow

The plugin development env features automatic compilation. To use run `gulp` in /example/.

This watches two locations:

- in /example/ it watches html, css, js. on change it compiles that code.
- in /plugin/ it watches js, xml, h, m, a, kt, swift, json. on change it removes and adds back the plugin.

## Key Commands

- `gulp` in ../example/ runs gulp to watch for changes
- `cordova prepare` in ../example/ this compiles non-native assets (html/css/js)
- `cordova plugin rm cordova-plugin-feed-fm && cordova plugin add --link ../plugin` in ../example/ reinstall plugin, useful for js changes

<!-- MARKDOWN LINKS & IMAGES -->

[contributors-shield]: https://img.shields.io/badge/contributors-1-orange.svg?style=flat-square
[product-screenshot]: https://raw.githubusercontent.com/othneildrew/Best-README-Template/master/screenshot.png
