package com.oniz.Game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.oniz.Screens.MainScreen;
import com.oniz.Screens.SplashScreen;


public class ZGame extends Game {
    AssetLoader assets;

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
        setScreen(new SplashScreen(this));
//        setScreen(new MainScreen());
    }

    @Override
    public void dispose() {
        super.dispose();
        assets.dispose();
    }
}