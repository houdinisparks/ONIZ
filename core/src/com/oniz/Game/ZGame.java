package com.oniz.Game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.oniz.Network.PlayServices;
import com.oniz.Screens.MainScreen;
import com.oniz.Screens.StartScreen;


public class ZGame extends Game {
    AssetLoader assets;

    public static PlayServices playServices;

    public ZGame(PlayServices androidLauncher) {
        this.playServices = androidLauncher;
    }

    public enum State
    {
        PAUSE,
        RUN,
        RESUME,
        STOPPED
    }

    @Override
    public void create() {
        Gdx.app.log("ONIZ", "created");
        assets = new AssetLoader();

        //Pass  and object of ZGame into the screen. Allows us tp
        //access the play services in the screen.
        setScreen(new StartScreen(this));
    }
}