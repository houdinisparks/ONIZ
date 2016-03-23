package com.oniz.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.oniz.Game.AssetLoader;
import com.oniz.Game.GameRenderer;
import com.oniz.Game.GameWorld;
import com.oniz.Game.ZGame;
import com.oniz.Gestures.GestureRecognizerInputProcessor;
import com.oniz.Gestures.ZGestureListener;
import com.oniz.Network.PlayServices;
import com.oniz.UI.SimpleButton;


/**
 * MainScreen contains the top bar menu which is drawn over everything else.
 */
public class MainScreen implements Screen {

    GameWorld gameWorld;
    GameRenderer gameRenderer;

    private float runTime;

    private Stage stage;
    private Table table;

    InputMultiplexer im;
    GestureRecognizerInputProcessor gestureRecognizer;
    GestureDetector gd;

    public static ZGame zGame;

    public MainScreen(ZGame zGame) {

        this.zGame = zGame;

        stage = new Stage(new FitViewport(450, 800));

        gameWorld = new GameWorld();
        gameRenderer = new GameRenderer(gameWorld,new InputMultiplexer());
        gameWorld.setRenderer(gameRenderer);

        im = new InputMultiplexer();
        gd = new GestureDetector(new ZGestureListener(gameWorld, gameRenderer));
        gestureRecognizer = new GestureRecognizerInputProcessor();
        im.addProcessor(gestureRecognizer);
        im.addProcessor(gd);
        im.addProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        table.setDebug(true);

        table.align(Align.top | Align.right);
        table.pad(5);

        stage.addActor(table);
    }


    @Override
    public void show() {
        Gdx.app.log("MainScreen", "show called");
        Gdx.input.setInputProcessor(im);
    }

    @Override
    public void render(float delta) {
        runTime += delta;
        gameWorld.update(delta);
        gameRenderer.render(runTime);

//      draw stage over everything else
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        Gdx.app.log("MainScreen", "pause called");
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
}
