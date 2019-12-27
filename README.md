<!-- PROJECT SHIELDS -->

[![code style: prettier](https://img.shields.io/badge/code_style-prettier-ff69b4.svg?style=flat-square)](https://github.com/prettier/prettier)

<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="#">
    <img src="https://feed.fm/images/feedfm-logo-greyred.png" alt="Logo">
  </a>
  <h3 align="center">Cordova plugin for Feed.FM FeedMedia native SDK</h2>
  <h4 align="center">By: Timothy Shamilov</h4>
  <p align="center">Still in development</p>
  <p align="center">
    Uses the FeedMedia CocoaPod on iOS and Feed Gradle package
  </p>
    <p align="center">
    Objective-C & Java
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

initialize the plugin and handle all state with a switch statement. 
plugin will respond with the appropriate callback when native state changes.

```typescript
CordovaPluginFeedFm.initializeWithToken(
  successCallback, 
  errorCallback, 
  token, 
  secret, 
  enableBackgroundMusic
);

// repeatedly returns callbacks to Cordova when state changes with the following Object shape
{type: "NAME_OF_STATE_OR_EVENT", payload:{...}}
```

Full example of every state that initialize will respond with:
```typescript
CordovaPluginFeedFm.initializeWithToken(
  function(msg) {
    switch (msg.type) {
      case "REQUESTING_SKIP":
        console.log(msg.payload);
        // your logic here
        break;
      case "PLAYBACK_STARTED":
        console.log(msg.payload);
        // your logic here
        break;
      case "PLAYBACK_STARTED":
        console.log(msg.payload);
        // your logic here
        break;
      case "NEW_CLIENT_ID":
        console.log(msg.payload);
        // your logic here
        break;
      case "SET_ACTIVE_STATION_SUCCESS":
        console.log(msg.payload);
        // your logic here
        break;
      case "SET_ACTIVE_STATION_FAIL":
        console.log(msg.payload);
        // your logic here
        break;
      case "SKIP_FAIL":
        console.log(msg.payload);
        // your logic here
        break;
      case "INITIALIZE":
        console.log(msg.payload);
        // your logic here
        break;
      case "WAITING_FOR_ITEM":
        console.log(msg.payload);
        // your logic here
        break;
      case "READY_TO_PLAY":
        console.log(msg.payload);
        // your logic here
        break;
      case "PLAYING":
        console.log(msg.payload);
        // your logic here
        break;
      case "STALLED":
        console.log(msg.payload);
        // your logic here
        break;
      case "COMPLETE":
        console.log(msg.payload);
        // your logic here
        break;
      case "UNKNOWN":
        console.log(msg.payload);
        // your logic here
        break;
      case "OFFLINE_ONLY":
        console.log(msg.payload);
        // your logic here
        break;
      case "UNINITIALIZED":
        console.log(msg.payload);
        // your logic here
        break;
      case "UNAVAILABLE":
        console.log(msg.payload);
        // your logic here
        break;
      case "STATION_CHANGE":
        console.log(msg.payload);
        // your logic here
        break;
      case "READY_TO_PLAY":
        console.log(msg.payload);
        // your logic here
        break;
      case "PAUSED":
        console.log(msg.payload);
        // your logic here
        break;
      default:
        break;
    }
  },
  function(err) {},
  "token",
  "secret",
  false // background audio
);
```

set active station

```typescript
CordovaPluginFeedFm.setActiveStation(
  successCallback,
  errorCallback, 
  stationId
);
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

## Key Commands

- `cordova prepare` in /example/ this compiles non-native assets (html/css/js)
- `cordova plugin rm cordova-plugin-feed-fm && cordova plugin add cordova-plugin-feed-fm --searchpath=../ --noregistry` in /example/ to reinstall plugin, useful for js interface changes

<!-- MARKDOWN LINKS & IMAGES -->

[product-screenshot]: https://raw.githubusercontent.com/othneildrew/Best-README-Template/master/screenshot.png
