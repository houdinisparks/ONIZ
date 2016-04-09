package com.oniz.Network;

import com.badlogic.gdx.Gdx;
import com.oniz.Game.ZGame;

/**
 * Created by robin on 29/3/16.
 */
public class DesktopPlayServices implements PlayServices {
    ZGame zgame;
    @Override
    public void signIn() {
        Gdx.app.log("DesktopPlayServices", "Sign in");
    }

    @Override
    public void signOut() {
        Gdx.app.log("DesktopPlayServices", "Sign out");
    }

    @Override
    public void startQuickGame() {
        Gdx.app.log("DesktopPlayServices", "Start quick game");
    }

    @Override
    public void leaveGame() {
        Gdx.app.log("DesktopPlayServices", "Leave game");
    };

    @Override
    public void broadcastMessage(String message) {
        Gdx.app.log("DesktopPlayServices", "Broadcast message");
    }

    @Override
    public void setGame(ZGame zgame) {
        this.zgame = zgame;
    }

    @Override
    public void rateGame() {
        Gdx.app.log("DesktopPlayServices", "Rate game");
    }

    @Override
    public void unlockAchievement() {
        Gdx.app.log("DesktopPlayServices", "Unlock achievement");
    }

    @Override
    public void submitScore(int highScore) {
        Gdx.app.log("DesktopPlayServices", "Submit score");
    }

    @Override
    public void showAchievement() {
        Gdx.app.log("DesktopPlayServices", "Show achievement");
    }

    @Override
    public void showScore() {
        Gdx.app.log("DesktopPlayServices", "Show score");
    }

    @Override
    public boolean isSignedIn() {
        return false;
    }
}
