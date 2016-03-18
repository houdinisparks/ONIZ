package com.oniz.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.oniz.Game.AssetLoader;
import com.oniz.Game.ZGame;
import com.oniz.UI.SimpleButton;

/**
 * Created by yanyee on 3/18/2016.
 */
public class StartScreen implements Screen {

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private SimpleButton buttonASignIn;
    public static ZGame zGame;
    private Stage stage;

    public StartScreen(final ZGame zGame) {
        this.zGame = zGame;

        buttonASignIn = new SimpleButton(new Image(AssetLoader.getInstance().textures.get("buttonAUp")).getDrawable(), new Image(AssetLoader.getInstance().textures.get("buttonADown")).getDrawable());
        stage = new Stage(new FitViewport(450, 800));
        Gdx.input.setInputProcessor(stage);

        buttonASignIn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Button A Pressed");
                signIn();
            }
        });

        stage.addActor(buttonASignIn);

    }

    public void signIn() {
        zGame.playServices.signIn();
    }




}
