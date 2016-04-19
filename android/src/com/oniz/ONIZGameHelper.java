package com.oniz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.google.example.games.basegameutils.GameHelper;
import com.oniz.Game.ZGame;
import com.oniz.Network.LoginListener;
import com.oniz.Network.PlayEventListener;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by robin on 19/3/16.
 */
public class ONIZGameHelper extends GameHelper implements RealTimeMessageReceivedListener, RoomStatusUpdateListener, RoomUpdateListener {

    final static String TAG = "ONIZGameHelper";

    Activity activity;

    ZGame zGame;

    //List of objects that subscribe to events
    private List<PlayEventListener> playEventListeners = Collections.synchronizedList(new ArrayList<PlayEventListener>());

    // Request codes for the UIs that we show with startActivityForResult:
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_INVITATION_INBOX = 10001;
    final static int RC_WAITING_ROOM = 10002;

    // The participants in the currently active game
    ArrayList<Participant> mParticipants = null;

    // Room ID where the currently active game is taking place; null if we're
    // not playing.
    String mRoomId = null;

    // My participant ID in the currently active game
    String mMyId = null;

    final static int MIN_PLAYERS = 2;


    public ONIZGameHelper(Activity activity, int clientsToUse) {
        super(activity, clientsToUse);
        this.activity = activity;
    }

    public void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);
        Gdx.app.log(TAG, request + "");
    }

    public void addPlayEventListener(PlayEventListener listener) {
        playEventListeners.add(listener);
    }

    public void removePlayEventListener(PlayEventListener listener) {
        playEventListeners.remove(listener);
    }

    public void setGame(ZGame zGame) {
        this.zGame = zGame;
    }

    public void startQuickGame() {
        // auto-match criteria to invite one random automatch opponent.
        // You can also specify more opponents (up to 3).
        Bundle am = RoomConfig.createAutoMatchCriteria(1, 1, 0);

        // build the room config:
        RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
        roomConfigBuilder.setAutoMatchCriteria(am);
        RoomConfig roomConfig = roomConfigBuilder.build();
        Gdx.app.log(TAG, "ROOM BUILDER DONE");
        // create room:
        try {
            Gdx.app.log(TAG, " TRYING TO CREATING RTM....");
            Games.RealTimeMultiplayer.create(getApiClient(), roomConfig);
        } catch (Exception ex) {
            Gdx.app.log(TAG, ex.getMessage());
        }

        // prevent screen from sleeping during handshake
        keepScreenOn();

        // go to game screen? nah. we will check if there are enuh players first
    }

    public boolean leaveGame() {
        try {
            if (mRoomId != null) {
                //use "this" as this class implements RoomUpdateListener
                Games.RealTimeMultiplayer.leave(getApiClient(), this, mRoomId);
                stopKeepingScreenOn();
                return true;
            } else {
                Gdx.app.log(TAG, "There wasn't a room in the first place.");
            }
        } catch (Exception ex) {
            Gdx.app.log(TAG, ex.getMessage());
        }
        return false;
    }

    public boolean canLeaveRoom() {
        if (mRoomId != null) return true;
        return false;
    }

    private RoomConfig.Builder makeBasicRoomConfigBuilder() {
        return RoomConfig.builder(this)
                .setMessageReceivedListener(this)
                .setRoomStatusUpdateListener(this);
    }

    @Override
    public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {
        byte[] buf = realTimeMessage.getMessageData();
        String sender = realTimeMessage.getSenderParticipantId();
        try {
            final String t = new String(buf, "ISO-8859-1");
            if (zGame.isGameWorldReady()) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        zGame.getGameWorld().realTimeUpdate(t);
                    }
                });
            }

            Gdx.app.log(TAG, "Received message: " + t + sender);
//            Toast.makeText(activity, t, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Gdx.app.log(TAG, ex.getMessage());
        }
    }

    //send out a message
    public void broadcastMessage(String message) {
        Gdx.app.log(TAG, "Broadcasting message");
        boolean test = true;
//        byte[] mMsgBuf = new byte[2];
//        mMsgBuf[0] = (byte) 'A';
//        mMsgBuf[1] = (byte) 'B';

        byte[] mMsgBuf = message.getBytes(Charset.forName("UTF-8"));

        // Send to every other participant.
        for (Participant p : mParticipants) {
            if (p.getParticipantId().equals(mMyId))
                continue;
            if (p.getStatus() != Participant.STATUS_JOINED)
                continue;
            if (test) {
                // final score notification must be sent via reliable message
                Games.RealTimeMultiplayer.sendReliableMessage(getApiClient(), null, mMsgBuf,
                        mRoomId, p.getParticipantId());
            } else {
                // it's an interim score notification, so we can use unreliable
                Games.RealTimeMultiplayer.sendUnreliableMessage(getApiClient(), mMsgBuf, mRoomId,
                        p.getParticipantId());
            }
        }
    }

    @Override
    public void onRoomConnecting(Room room) {
        Gdx.app.log(TAG, "Connecting to room.");
    }

    @Override
    public void onRoomAutoMatching(Room room) {
        Gdx.app.log(TAG, "Automatching...");
    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> list) {
        Gdx.app.log(TAG, "peer invited");
    }

    @Override
    public void onPeerDeclined(Room room, List<String> list) {
        Gdx.app.log(TAG, "peer declined");
    }

    @Override
    public void onPeerJoined(Room room, List<String> list) {
        Gdx.app.log(TAG, "peer joined");
    }

    @Override
    public void onPeerLeft(Room room, List<String> list) {
        Gdx.app.log(TAG, "peer left");
    }

    @Override
    public void onConnectedToRoom(Room room) {
        Log.d(TAG, "onConnectedToRoom.");

        //get participants and my ID:
        mParticipants = room.getParticipants();
        mMyId = room.getParticipantId(Games.Players.getCurrentPlayerId(getApiClient()));

        // save room ID if its not initialized in onRoomCreated() so we can leave cleanly before the game starts.
        if (mRoomId == null)
            mRoomId = room.getRoomId();

        // print out the list of participants (for debug purposes)
        Log.d(TAG, "Room ID: " + mRoomId);
        Log.d(TAG, "My ID " + mMyId);
        Log.d(TAG, "<< CONNECTED TO ROOM>>");
    }

    @Override
    public void onDisconnectedFromRoom(Room room) {
        Gdx.app.log(TAG, "You dced from room");

    }

    @Override
    public void onPeersConnected(Room room, List<String> list) {
        Gdx.app.log(TAG, "Peers connected. Can we start?");
        if (shouldStartGame(room)) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    zGame.switchScreen(ZGame.ScreenState.MAIN);
                }
            });
        }
    }

    @Override
    public void onPeersDisconnected(Room room, List<String> list) {
        Gdx.app.log(TAG, "Peers disconnected");
        synchronized (playEventListeners) {
            for (final PlayEventListener listener : playEventListeners) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        listener.disconnected();
                    }
                });
            }
        }
    }

    @Override
    public void onP2PConnected(String s) {
        Gdx.app.log(TAG, "P2P connected");
    }

    @Override
    public void onP2PDisconnected(String s) {
        Gdx.app.log(TAG, "P2P disconnected");
    }

    @Override
    public void onRoomCreated(int statusCode, Room room) {
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Gdx.app.log(TAG, "Room creation failed");
            if(room != null) {
                Gdx.app.log(TAG, "STATUS:" + statusCode + "  " + "room:" + room.getRoomId());
            }
            // let screen go to sleep
            stopKeepingScreenOn();
            // show error message, return to main screen.
            zGame.switchScreen(ZGame.ScreenState.START);
        } else {
            Gdx.app.log(TAG, "STATUS:" + statusCode + "  " + "room:" + room.getRoomId());
            mRoomId = room.getRoomId();
        }
    }

    @Override
    public void onJoinedRoom(int statusCode, Room room) {
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Gdx.app.log(TAG, "Joined room");
            // let screen go to sleep
            stopKeepingScreenOn();
            // show error message, return to main screen.
        }
    }

    @Override
    public void onLeftRoom(int i, String s) {
        mRoomId = null;
        Gdx.app.log(TAG, "Left room." + i + ":" + s);

        synchronized (playEventListeners) {
            for (PlayEventListener listener : playEventListeners) {
                listener.leftRoom();
            }
        }
    }

    @Override
    public void onRoomConnected(int statusCode, Room room) {
//        To be notified when all players are connected, your game can use the RoomUpdateListener.onRoomConnected() callback.
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Gdx.app.log(TAG, "Room connected");
            // let screen go to sleep
            stopKeepingScreenOn();

            // show error message, return to main screen.
        }
    }

    // returns whether there are enough players to start the game
    boolean shouldStartGame(Room room) {
        int connectedPlayers = 0;
        for (Participant p : room.getParticipants()) {
            if (p.isConnectedToRoom()) ++connectedPlayers;
        }
        return connectedPlayers >= MIN_PLAYERS;
    }

    /*
     * MISC SECTION. Miscellaneous methods.
     */

    // Sets the flag to keep this screen on. It's recommended to do that during
    // the
    // handshake when setting up a game, because if the screen turns off, the
    // game will be
    // cancelled.
    void keepScreenOn() {
        this.activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    // Clears the flag that keeps the screen on.
    void stopKeepingScreenOn() {
        this.activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
