package com.oniz;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import com.google.example.games.basegameutils.GameHelper;

import com.oniz.Game.ZGame;
import com.oniz.Network.PlayServices;

import java.util.ArrayList;
import java.util.List;

public class AndroidLauncher extends AndroidApplication implements PlayServices {

    private ONIZGameHelper gameHelper;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
            gameHelper = new ONIZGameHelper(this, GameHelper.CLIENT_GAMES);
            gameHelper.enableDebugLog(false);

            GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
                @Override
                public void onSignInFailed() {
                    Gdx.app.log("LOGIN FAILED", "Sign in FAILED!");
                }

                @Override
                public void onSignInSucceeded() {
                    Gdx.app.log("LOGIN SUCCESS", "Sign in success!");
                }

            };

            gameHelper.setup(gameHelperListener);

            AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

            initialize(new ZGame(this), config);
        }

    }

    private boolean checkAndRequestPermissions() {
        int permissionInternet = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        int permissionNetworkState = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        int permissionWriteExternal = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionNetworkState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (permissionInternet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }

        if (permissionWriteExternal != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
//        gameHelper.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameHelper.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gameHelper.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void signIn() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameHelper.beginUserInitiatedSignIn();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void signOut() {

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameHelper.signOut();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
        }

    }

    @Override
    public void startQuickGame() {
        Gdx.app.log("QUICKGAME", "initializing... quick game!");
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameHelper.startQuickGame();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("MainActivity", "Quick game failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void leaveGame() {
        Gdx.app.log("LEAVEGAME", "LEAVING ROOM!");
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameHelper.leaveGame();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("MainActivity", "leave room failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void setGame(ZGame zgame) {
        try {
            gameHelper.setGame(zgame);
        } catch (Exception e) {
            Gdx.app.log("MainActivity", "Quick game failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void broadcastMessage(String message) {
        final String msg = message;
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameHelper.broadcastMessage(msg);
                }
            });
        } catch (Exception e) {
            Gdx.app.log("MainActivity", "Quick game failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void rateGame() {

        String str = "Your PlayStore Link";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));

    }

    @Override
    public void unlockAchievement() {
//        Games.Achievements.unlock(gameHelper.getApiClient(),
//                getString(R.string.achievement_dum_dum));
    }

    @Override
    public void submitScore(int highScore) {
        if (isSignedIn() == true) {
//            Games.Leaderboards.submitScore(gameHelper.getApiClient(),
//                    getString(R.string.leaderboard_highest), highScore);
        }
    }

    @Override
    public void showAchievement() {
        if (isSignedIn() == true) {
//            startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient(),
//                    getString(R.string.achievement_dum_dum)), requestCode);
        } else {
            signIn();
        }
    }

    @Override
    public void showScore() {
        if (isSignedIn() == true) {
//            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
//                    getString(R.string.leaderboard_highest)), requestCode);
        } else {
//            signIn();
        }
    }

    @Override
    public boolean isSignedIn() {
        return gameHelper.isSignedIn();
    }

    @Override
    public boolean canLeaveRoom() {
        return gameHelper.canLeaveRoom();
    }
}
