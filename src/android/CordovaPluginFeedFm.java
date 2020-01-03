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
    private CallbackContext cordovaCallback;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        applicationContext = this.cordova.getActivity().getApplicationContext();
        cordovaCallback = callbackContext;

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
        if (action.equals("pause")) {
            Log.i("CordovaPluginFeedFm", "Native pause call");
            this.pause();
            return true;
        }
        if (action.equals("skip")) {
            Log.i("CordovaPluginFeedFm", "Native skip call");
            this.skip();
            return true;
        }
        if (action.equals("stop")) {
            Log.i("CordovaPluginFeedFm", "Native stop call");
            this.stop();
            return true;
        }
        if (action.equals("createNewClientID")) {
            Log.i("CordovaPluginFeedFm", "Native createNewClientID call");
            this.createNewClientID();
            return true;
        }
        if (action.equals("requestClientID")) {
            Log.i("CordovaPluginFeedFm", "Native requestClientID call");
            this.requestClientID();
            return true;
        }
        if (action.equals("setClientID")) {
            Log.i("CordovaPluginFeedFm", "Native requestClientID call");
            String passedID = args.getString(0);
            this.setClientID(passedID);
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

    private void play() {
        mFeedAudioPlayer.play();
    }

    private void pause() {
        mFeedAudioPlayer.pause();
    }

    private void skip() {
        mFeedAudioPlayer.skip();
    }

    private void stop() {
        mFeedAudioPlayer.stop();
    }

    public void initializeWithToken(CallbackContext callbackContext, String token, String secret, boolean enableBackgroundMusic) {

        FeedAudioPlayer.AvailabilityListener listener = new FeedAudioPlayer.AvailabilityListener() {
            @Override
            public void onPlayerAvailable(FeedAudioPlayer feedAudioPlayer) {
                try {
                    Integer activeStationId = mFeedAudioPlayer.getActiveStation().getId();
                    Boolean available = true;
                    String stations = mFeedAudioPlayer.getStationList().toString();
                    String response = String.format("{type:INITIALIZE, payload: { available: %s, stations: %s, activeStationId: %d }}", available, stations, activeStationId);
                    sendCordovaCallback(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPlayerUnavailable(Exception e) {
                sendCordovaCallback("{type:INITIALIZE_FAIL, payload:{available: false}}");
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

    public void setClientID(String clientID) {
        mFeedAudioPlayer.setClientId(clientID);
    }

    public void requestClientID() {
        String clientId = mFeedAudioPlayer.getClientId();
        String response = String.format("{type: REQUEST_CLIENT_ID, payload:{ClientID: %s}}", clientId);
        sendCordovaCallback(response);
    }

    public void createNewClientID() {
        mFeedAudioPlayer.createNewClientId(new FeedAudioPlayer.ClientIdListener() {
            @Override
            public void onClientId(String s) {
                String response = String.format("{type: NEW_CLIENT_ID, payload: {ClientID: %s}}", s);
                sendCordovaCallback(response);
            }

            @Override
            public void onError() {
                sendCordovaCallback("{type: ERROR_CLIENT_ID, payload:\"\"}");
            }
        });
    }

    @Override
    public void onProgressUpdate(Play play, float v, float v1) {

    }

    @Override
    public void onPlayStarted(Play play) {
        String options = play.getAudioFile().getOptions().toString();
        String id = play.getAudioFile().getId();
        String title = play.getAudioFile().getTrack().getTitle();
        String artist = play.getAudioFile().getArtist().getName();
        String album = play.getAudioFile().getRelease().getTitle();
        Boolean canSkip = mFeedAudioPlayer.canSkip();
        Integer duration = (int) play.getAudioFile().getDurationInSeconds();

        String response = String.format("{type:PLAYBACK_STARTED, payload: {metadata: %s, id: %s, title: %s, artist: %s, album: %s, canSkip: %s, duration: %d}}", options, id, title, artist, album, canSkip, duration);

        sendCordovaCallback(response);
    }

    @Override
    public void onSkipStatusChanged(boolean b) {

    }

    @Override
    public void onStateChanged(FeedAudioPlayer.State state) {
        switch (state) {
            case PAUSED:
                sendCordovaCallback("{type: PAUSED, payload:\"\" }");
                break;
            case PLAYING:
                sendCordovaCallback("{type: PLAYING, payload:\"\" }");
                break;
            case STALLED:
                sendCordovaCallback("{type: STALLED, payload:\"\" }");
                break;
            case UNAVAILABLE:
                sendCordovaCallback("{type: UNAVAILABLE, payload:\"\" }");
                break;
            case READY_TO_PLAY:
                sendCordovaCallback("{type: READY_TO_PLAY, payload:\"\" }");
                break;
            case UNINITIALIZED:
                sendCordovaCallback("{type: UNINITIALIZED, payload:\"\" }");
                break;
            case WAITING_FOR_ITEM:
                sendCordovaCallback("{type: WAITING_FOR_ITEM, payload:\"\"}");
                break;
            case AVAILABLE_OFFLINE_ONLY:
                sendCordovaCallback("{type: OFFLINE_ONLY, payload:\"\"}");
                break;
            default:
                sendCordovaCallback("{type: UNKNOWN, payload:\"\"}");
                break;
        }
    }

    @Override
    public void onStationChanged(Station station) {
        String response = String.format("{type: STATION_CHANGE, payload: {activeStationId: %d}}", station.getId());
        sendCordovaCallback(response);
    }

    // Skip
    @Override
    public void requestCompleted(boolean b) {
        if (!b) {
            sendCordovaCallback("{type:SKIP_FAIL, payload:\"\"}");
        }
    }

    // helper method
    private void sendCordovaCallback(String response) {
        PluginResult result;

        result = new PluginResult(PluginResult.Status.ERROR, response);

        result.setKeepCallback(true);
        cordovaCallback.sendPluginResult(result);
    }

    // helper method
    private static Gson createDefaultGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }

    // helper method
    private String toJson(Object json) {
        return createDefaultGson().toJson(json);
    }

}