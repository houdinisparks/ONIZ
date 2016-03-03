package com.oniz.Game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.oniz.Screens.MainScreen;
import com.oniz.UI.SimpleButton;


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
        setScreen(new MainScreen());
    }
}