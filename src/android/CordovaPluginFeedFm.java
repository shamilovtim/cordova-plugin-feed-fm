package shamilovtim;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fm.feed.android.playersdk.FeedAudioPlayer;
import fm.feed.android.playersdk.FeedPlayerService;
import fm.feed.android.playersdk.models.Play;
import fm.feed.android.playersdk.models.Station;
import fm.feed.android.playersdk.models.StationList;

import android.content.Context;


public class CordovaPluginFeedFm extends CordovaPlugin implements FeedAudioPlayer.StateListener,
        FeedAudioPlayer.StationChangedListener, FeedAudioPlayer.PlayListener, FeedAudioPlayer.SkipListener {

    private static FeedAudioPlayer mFeedAudioPlayer;
    private Context applicationContext;
    public CallbackContext cordovaCallback;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        applicationContext = this.cordova.getActivity().getApplicationContext();

        // callback was resetting on each execute. this way we preserve it.
        if (cordovaCallback == null) {
            cordovaCallback = callbackContext;
        }

        if (action.equals("echo")) {
            Log.i("CordovaPluginFeedFm", "Native echo call");
            String message = args.getString(0);
            this.echo(message, callbackContext);
            return true;
        }
        if (action.equals("play")) {
            // exoplayer calls for running on UI thread so I am using runOnUUThread everywhere.
            // this has ramifications for performance but should be ok.
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Log.i("CordovaPluginFeedFm", "Native play call");
                    CordovaPluginFeedFm.play();
                }
            });
            return true;
        }
        if (action.equals("pause")) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Log.i("CordovaPluginFeedFm", "Native pause call");
                    CordovaPluginFeedFm.pause();
                }
            });
            return true;
        }
        if (action.equals("skip")) {
            CordovaPluginFeedFm that = this;

            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Log.i("CordovaPluginFeedFm", "Native skip call");
                    that.skip();
                }
            });
            return true;
        }
        if (action.equals("stop")) {
            CordovaPluginFeedFm that = this;

            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Log.i("CordovaPluginFeedFm", "Native stop call");
                    that.stop();
                }
            });
            return true;
        }
        if (action.equals("createNewClientID")) {
            CordovaPluginFeedFm that = this;
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Log.i("CordovaPluginFeedFm", "Native createNewClientID call");
                    that.createNewClientID();
                }
            });
            return true;
        }
        if (action.equals("requestClientId")) {
            Log.i("CordovaPluginFeedFm", "Native requestClientID call");
            this.requestClientID(callbackContext);
            return true;
        }
        if (action.equals("setClientID")) {
            CordovaPluginFeedFm that = this;
            String passedID = args.getString(0);

            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Log.i("CordovaPluginFeedFm", "Native setClientID call");
                    that.setClientID(passedID);
                }
            });
            return true;
        }
        if (action.equals("setActiveStation")) {
            CordovaPluginFeedFm that = this;
            Integer stationID = args.getInt(0);

            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Log.i("CordovaPluginFeedFm", "Native setActiveStation call");
                    that.setActiveStation(stationID);
                }
            });
            return true;
        }
        if (action.equals("setVolume")) {
            Log.i("CordovaPluginFeedFm", "Native setVolume call");
            double volume = args.getDouble(0);
            this.setVolume((float) volume);
            return true;
        }
        if (action.equals("initializeWithToken")) {
            CordovaPluginFeedFm that = this;
            String token = args.getString(0);
            String secret = args.getString(1);
            Boolean backgroundAudio = args.getBoolean(2);
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Log.i("CordovaPluginFeedFm", "Native initializeWithToken call");
                    that.initializeWithToken(token, secret, backgroundAudio);
                }
            });
            return true;
        }

        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Log.i("CordovaPluginFeedFm", "did reach end");
                PluginResult result;
                result = new PluginResult(PluginResult.Status.NO_RESULT);
                result.setKeepCallback(true);
                cordovaCallback.sendPluginResult(result);
            }
        });
        return true;
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

    public static void play() {
        mFeedAudioPlayer.play();
    }

    private static void pause() {
        mFeedAudioPlayer.pause();
    }

    private void skip() {
        mFeedAudioPlayer.skip();
    }

    private void stop() {
        mFeedAudioPlayer.stop();
    }

    public void initializeWithToken(String token, String secret, boolean enableBackgroundMusic) {

        FeedAudioPlayer.AvailabilityListener listener = new FeedAudioPlayer.AvailabilityListener() {
            @Override
            public void onPlayerAvailable(FeedAudioPlayer feedAudioPlayer) {
                try {
                    Integer activeStationId = mFeedAudioPlayer.getActiveStation().getId();
                    Boolean available = true;
                    StationList stations = mFeedAudioPlayer.getStationList();
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    String stationString = gson.toJson(stations);
                    String response = String.format("{\"type\":\"INITIALIZE\", \"payload\": { \"available\": \"%s\", \"stations\": %s, \"activeStationId\": \"%d\" }}", available, stationString, activeStationId);

                    sendCordovaCallback(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPlayerUnavailable(Exception e) {
                try {
                    sendCordovaCallback("{\"type\":\"INITIALIZE_FAIL\", \"payload\":{\"available\": \"false\"}}");
                } catch (Exception ej) {

                }
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

    public void requestClientID(CallbackContext callbackContext) {
        try {
            String clientId = mFeedAudioPlayer.getClientId();
            String responseString = String.format("{\"type\": \"REQUEST_CLIENT_ID\", \"payload\":{\"ClientID\": \"%s\"}}", clientId);
            JSONObject response = new JSONObject(responseString);

            PluginResult result;
            result = new PluginResult(PluginResult.Status.OK, response);

            callbackContext.sendPluginResult(result);
        } catch (Exception e) {
        }
    }

    public void createNewClientID() {
        mFeedAudioPlayer.createNewClientId(new FeedAudioPlayer.ClientIdListener() {
            @Override
            public void onClientId(String s) {
                try {
                    String response = String.format("{\"type\": \"NEW_CLIENT_ID\", \"payload\": {\"ClientID\": \"%s\"}}", s);
                    sendCordovaCallback(response);
                } catch (Exception e) {

                }
            }

            @Override
            public void onError() {
                try {
                    sendCordovaCallback("{\"type\": \"ERROR_CLIENT_ID\", \"payload\":\"\"}");
                } catch (Exception e) {

                }
            }
        });
    }

    public void setVolume(float volume) {
        mFeedAudioPlayer.setVolume(volume);
    }

    public void setActiveStation(Integer station) {

        try {
            boolean flag = false;
            for (Station st : mFeedAudioPlayer.getStationList()) {

                if (st.getId().toString().equals(station.toString())) {
                    mFeedAudioPlayer.setActiveStation(st);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                sendCordovaCallback("{\"type\":\"SET_ACTIVE_STATION_FAIL\", \"payload\":\"\"}");
                Log.e("CordovaPluginFeedFm", "Cannot set active station to " + station + " because no station found with that id");
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onProgressUpdate(Play play, float v, float v1) {

    }

    @Override
    public void onPlayStarted(Play play) {
        try {
            Log.i("CordovaPluginFeedFm", "on play started");
            String options = play.getAudioFile().getOptions().toString();
            String id = play.getAudioFile().getId();
            String title = play.getAudioFile().getTrack().getTitle();
            String artist = play.getAudioFile().getArtist().getName();
            String album = play.getAudioFile().getRelease().getTitle();
            Boolean canSkip = mFeedAudioPlayer.canSkip();
            Integer duration = (int) play.getAudioFile().getDurationInSeconds();

            String response = String.format("{\"type\":\"PLAYBACK_STARTED\", \"payload\": {\"id\": \"%s\", \"title\": \"%s\", \"artist\": \"%s\", \"album\": \"%s\", \"metadata\": \"%s\", \"canSkip\": \"%s\", \"duration\": \"%d\"}}", id, title, artist, album, options, canSkip, duration);

            sendCordovaCallback(response);
        } catch (Exception e) {

        }
    }

    @Override
    public void onSkipStatusChanged(boolean b) {

    }

    @Override
    public void onStateChanged(FeedAudioPlayer.State state) {
        try {
            Log.i("CordovaPluginFeedFm", "state change");
            switch (state) {
                case PAUSED:
                    sendCordovaCallback("{\"type\": \"PAUSED\", \"payload\":\"\" }");
                    break;
                case PLAYING:
                    sendCordovaCallback("{\"type\": \"PLAYING\", \"payload\":\"\" }");
                    break;
                case STALLED:
                    sendCordovaCallback("{\"type\": \"STALLED\", \"payload\":\"\" }");
                    break;
                case UNAVAILABLE:
                    sendCordovaCallback("{\"type\": \"UNAVAILABLE\", \"payload\":\"\" }");
                    break;
                case READY_TO_PLAY:
                    sendCordovaCallback("{\"type\": \"READY_TO_PLAY\", \"payload\":\"\"}");
                    break;
                case UNINITIALIZED:
                    sendCordovaCallback("{\"type\": \"UNINITIALIZED\", \"payload\":\"\" }");
                    break;
                case WAITING_FOR_ITEM:
                    sendCordovaCallback("{\"type\": \"WAITING_FOR_ITEM\", \"payload\":\"\"}");
                    break;
                case AVAILABLE_OFFLINE_ONLY:
                    sendCordovaCallback("{\"type\": \"OFFLINE_ONLY\", \"payload\":\"\"}");
                    break;
                default:
                    sendCordovaCallback("{\"type\": \"UNKNOWN\", \"payload\":\"\"}");
                    break;
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onStationChanged(Station station) {
        try {
            String response = String.format("{\"type\": \"SET_ACTIVE_STATION_SUCCESS\", \"payload\": {\"activeStationId\": \"%d\"}}", station.getId());
            sendCordovaCallback(response);
        } catch (Exception e) {

        }
    }

    // Skip
    @Override
    public void requestCompleted(boolean b) {
        if (!b) {
            try {
                sendCordovaCallback("{\"type\":\"SKIP_FAIL\", payload:\"\"}");
            } catch (Exception e) {

            }
        }
    }

    // helper method
    private void sendCordovaCallback(String responseString) throws JSONException {
        JSONObject response = new JSONObject(responseString);

        PluginResult result;
        result = new PluginResult(PluginResult.Status.NO_RESULT, response);
        result.setKeepCallback(true);
        cordovaCallback.sendPluginResult(result);

        PluginResult realResult;
        realResult = new PluginResult(PluginResult.Status.OK, response);
        realResult.setKeepCallback(true);
        cordovaCallback.sendPluginResult(realResult);
    }
}