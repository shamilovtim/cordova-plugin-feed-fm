package shamilovtim;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import fm.feed.android.playersdk.FeedAudioPlayer;
import fm.feed.android.playersdk.FeedPlayerService;
import fm.feed.android.playersdk.models.Play;
import fm.feed.android.playersdk.models.Station;

public class CordovaPluginFeedFm extends CordovaPlugin implements FeedAudioPlayer.StateListener,
        FeedAudioPlayer.StationChangedListener, FeedAudioPlayer.PlayListener, FeedAudioPlayer.SkipListener {

    private FeedAudioPlayer mFeedAudioPlayer;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
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
        return false;
    }

    private void echo(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    public void play() {
        mFeedAudioPlayer.play();
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


}