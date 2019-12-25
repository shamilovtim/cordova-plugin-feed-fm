package shamilovtim;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fm.feed.android.playersdk.FeedAudioPlayer;
import fm.feed.android.playersdk.FeedPlayerService;
import fm.feed.android.playersdk.models.Play;
import fm.feed.android.playersdk.models.Station;

import android.content.Context;

public class CordovaPluginFeedFm extends CordovaPlugin implements FeedAudioPlayer.StateListener,
        FeedAudioPlayer.StationChangedListener, FeedAudioPlayer.PlayListener, FeedAudioPlayer.SkipListener {

    private FeedAudioPlayer mFeedAudioPlayer;
    private Context applicationContext;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        applicationContext = this.cordova.getActivity().getApplicationContext();

        if (action.equals("echo")) {
            Log.i("CordovaPluginFeedFm", "Native echo call");
            String message = args.getString(0);
            this.echo(message, callbackContext);
            return true;
        }
        if (action.equals("play")) {
            Log.i("CordovaPluginFeedFm", "Native play call");
            this.play();
            return true;
        }
        if (action.equals("initializeWithToken")) {
            Log.i("CordovaPluginFeedFm", "Native initializeWithToken call");
            String token = args.getString(0);
            String secret = args.getString(1);
            Boolean backgroundAudio = args.getBoolean(2);
            this.initializeWithToken(callbackContext, token, secret, backgroundAudio);
            return true;
        }
        return false;
    }

    private void echo(String message, CallbackContext callbackContext) {
        PluginResult result = new PluginResult(PluginResult.Status.OK, message);
        result.setKeepCallback(true);

        if (message != null && message.length() > 0) {
            callbackContext.sendPluginResult(result);
        } else {
            callbackContext.error("Expected a string argument.");
        }
    }

    public void play() {
        mFeedAudioPlayer.play();
    }

    public void initializeWithToken(CallbackContext callbackContext, String token, String secret, boolean enableBackgroundMusic) {

        FeedAudioPlayer.AvailabilityListener listener = new FeedAudioPlayer.AvailabilityListener() {
            @Override
            public void onPlayerAvailable(FeedAudioPlayer feedAudioPlayer) {
//                WritableMap params = Arguments.createMap();
//                callbackContext.success("available:true");
//                callbackContext.success("available:true");
                String strStations = toJson(mFeedAudioPlayer.getStationList());

                try {
//                    JSONArray jsArray = new JSONArray(strStations);
//                    WritableArray wArray = convertJsonToArray(jsArray);
//                    params.putArray("stations", wArray);
//                    params.putInt("activeStationId", mFeedAudioPlayer.getActiveStation().getId());
//                    sendEvent(reactContext, "availability", params);
                    callbackContext.success(strStations);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPlayerUnavailable(Exception e) {
//                WritableMap params = Arguments.createMap();
//                params.putBoolean("available", false);
//                sendEvent(reactContext, "availability", params);
            }
        };

        if (enableBackgroundMusic) {
            FeedPlayerService.initialize(applicationContext, token, secret);

            mFeedAudioPlayer = FeedPlayerService.getInstance();
            FeedPlayerService.getInstance(listener);

        } else {
            mFeedAudioPlayer = new FeedAudioPlayer.Builder()
                    .setToken(token)
                    .setSecret(secret)
                    .setContext(applicationContext)
                    .setAvailabilityListener(listener)
                    .build();
        }

        mFeedAudioPlayer.addPlayListener(CordovaPluginFeedFm.this);
        mFeedAudioPlayer.addSkipListener(CordovaPluginFeedFm.this);
        mFeedAudioPlayer.addStationChangedListener(CordovaPluginFeedFm.this);
        mFeedAudioPlayer.addStateListener(CordovaPluginFeedFm.this);
    }

    @Override
    public void onProgressUpdate(Play play, float v, float v1) {

    }

    @Override
    public void onPlayStarted(Play play) {
//
//        String str  = toJson(play.getAudioFile().getOptions());
//
//        try {
//            JSONObject object = new JSONObject(str);
//            WritableMap options  = convertJsonToMap(object);
//            WritableMap playParams = Arguments.createMap();
//            playParams.putMap("metadata",options);
//            playParams.putString("id", play.getAudioFile().getId());
//            playParams.putString("title", play.getAudioFile().getTrack().getTitle());
//            playParams.putString("artist", play.getAudioFile().getArtist().getName());
//            playParams.putString("album", play.getAudioFile().getRelease().getTitle());
//            playParams.putString("artist", play.getAudioFile().getArtist().getName());
//            playParams.putBoolean("canSkip", mFeedAudioPlayer.canSkip());
//            playParams.putInt("duration", (int)play.getAudioFile().getDurationInSeconds());
//            WritableMap params = Arguments.createMap();
//            params.putMap("play", playParams);
//            sendEvent(reactContext, "play-started", params);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void onSkipStatusChanged(boolean b) {

    }

    @Override
    public void onStateChanged(FeedAudioPlayer.State state) {
//        WritableMap params = Arguments.createMap();
//        switch (state)
//        {
//            case PAUSED:                 params.putInt("state",FeedAudioPlayer.State.PAUSED.ordinal()); break;
//            case PLAYING:                params.putInt("state",FeedAudioPlayer.State.PLAYING.ordinal()); break;
//            case STALLED:                params.putInt("state",FeedAudioPlayer.State.STALLED.ordinal()); break;
//            case UNAVAILABLE:            params.putInt("state",FeedAudioPlayer.State.UNAVAILABLE.ordinal()); break;
//            case READY_TO_PLAY:          params.putInt("state",FeedAudioPlayer.State.READY_TO_PLAY.ordinal()); break;
//            case UNINITIALIZED:          params.putInt("state",FeedAudioPlayer.State.UNINITIALIZED.ordinal()); break;
//            case WAITING_FOR_ITEM:       params.putInt("state",FeedAudioPlayer.State.WAITING_FOR_ITEM.ordinal()); break;
//            case AVAILABLE_OFFLINE_ONLY: params.putInt("state",FeedAudioPlayer.State.AVAILABLE_OFFLINE_ONLY.ordinal()); break;
//        }
//
//        sendEvent(reactContext, "state-change", params);
    }

    @Override
    public void onStationChanged(Station station) {
//        callbackContext.success(message);
//        WritableMap params = Arguments.createMap();
//        params.putInt("activeStationId", station.getId());
//        sendEvent(reactContext, "station-change", params);
    }

    // Skip
    @Override
    public void requestCompleted(boolean b) {
//        if(!b) {
//            WritableMap params = Arguments.createMap();
//            sendEvent(reactContext, "skip-failed", params);
//        }
    }

    private static Gson createDefaultGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }

    private String toJson(Object json) {
        return createDefaultGson().toJson(json);
    }

}