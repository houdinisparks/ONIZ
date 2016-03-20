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

    // temporary buttons to simulate gestures
    private SimpleButton buttonA, buttonB, buttonX, buttonY;

    private SimpleButton pauseButton, playButton,restartButton;
    private Stage stage;
    private Table table;

    InputMultiplexer im;
    GestureRecognizerInputProcessor gestureRecognizer;
    GestureDetector gd;

    public static ZGame zGame;

    public MainScreen(ZGame zGame) {

        this.zGame = zGame;

        // temporary
//        buttonA = new SimpleButton(new Image(AssetLoader.getInstance().textures.get("buttonAUp")).getDrawable(), new Image(AssetLoader.getInstance().textures.get("buttonADown")).getDrawable());
//        buttonB = new SimpleButton(new Image(AssetLoader.getInstance().textures.get("buttonBUp")).getDrawable(), new Image(AssetLoader.getInstance().textures.get("buttonBDown")).getDrawable());
//        buttonX = new SimpleButton(new Image(AssetLoader.getInstance().textures.get("buttonXUp")).getDrawable(), new Image(AssetLoader.getInstance().textures.get("buttonXDown")).getDrawable());
//        buttonY = new SimpleButton(new Image(AssetLoader.getInstance().textures.get("buttonYUp")).getDrawable(), new Image(AssetLoader.getInstance().textures.get("buttonYDown")).getDrawable());

        stage = new Stage(new FitViewport(450, 800));

        gameWorld = new GameWorld();
        gameRenderer = new GameRenderer(gameWorld);
        gameWorld.setRenderer(gameRenderer);

        im = new InputMultiplexer();
        gd = new GestureDetector(new ZGestureListener(gameWorld, gameRenderer));
        gestureRecognizer = new GestureRecognizerInputProcessor();
        im.addProcessor(gestureRecognizer);
        im.addProcessor(gd);
        im.addProcessor(stage);

//        // temporary
//        buttonA.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                System.out.println("Button A Pressed");
//                gameWorld.killZombie(0);
//            }
//        });
//
//        buttonB.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                System.out.println("Button B Pressed");
//                gameWorld.killZombie(1);
//            }
//        });
//
//        buttonX.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                System.out.println("Button X Pressed");
//                gameWorld.killZombie(2);
//            }
//        });
//
//        buttonY.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                System.out.println("Button Y Pressed");
//                gameWorld.killZombie(3);
//            }
//        });

        table = new Table();
        table.setFillParent(true);
        table.setDebug(true);

        table.align(Align.top | Align.right);
        table.pad(5);

        // temporary
//        table.add(buttonA);
//        table.add(buttonB);
//        table.add(buttonX);
//        table.add(buttonY);
//
//        table.add(pauseButton);
//        table.add(playButton);
//        table.add(restartButton);
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
