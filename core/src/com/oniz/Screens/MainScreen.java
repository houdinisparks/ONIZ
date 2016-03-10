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
import com.oniz.Gestures.GestureRecognizerInputProcessor;
import com.oniz.Gestures.ZGestureListener;
import com.oniz.UI.SimpleButton;


/**
 * Created by robin on 1/3/16.
 * MainScreen contains the top bar menu which is drawn over everything else.
 */
public class MainScreen implements Screen {

    GameWorld gameWorld;
    GameRenderer gameRenderer;

    private float runTime;

    // temporary buttons to simulate gestures
    private SimpleButton buttonA;
    private SimpleButton buttonB;

    private SimpleButton pauseButton;
    private SimpleButton playButton;
    private SimpleButton restartButton;
    private Stage stage;
    private Table table;

    InputMultiplexer im;
    GestureRecognizerInputProcessor gestureRecognizer;
    GestureDetector gd;

    public MainScreen() {
        // temporary
        buttonA = new SimpleButton(new Image(AssetLoader.getInstance().textures.get("buttonAUp")).getDrawable(), new Image(AssetLoader.getInstance().textures.get("buttonADown")).getDrawable());
        buttonB = new SimpleButton(new Image(AssetLoader.getInstance().textures.get("buttonBUp")).getDrawable(), new Image(AssetLoader.getInstance().textures.get("buttonBDown")).getDrawable());

        pauseButton = new SimpleButton(new Image(AssetLoader.getInstance().textures.get("pauseUp")).getDrawable(), new Image(AssetLoader.getInstance().textures.get("pauseDown")).getDrawable());
        playButton = new SimpleButton(new Image(AssetLoader.getInstance().textures.get("playUp")).getDrawable(), new Image(AssetLoader.getInstance().textures.get("playDown")).getDrawable());
        restartButton = new SimpleButton(new Image(AssetLoader.getInstance().textures.get("restartUp")).getDrawable(), new Image(AssetLoader.getInstance().textures.get("restartDown")).getDrawable());
        stage = new Stage(new FitViewport(450, 800));

        //TODO: Refactor gameWorld, gameRenderer
        gameWorld = new GameWorld();
        gameRenderer = new GameRenderer(gameWorld);
        gameWorld.setRenderer(gameRenderer);

        im = new InputMultiplexer();
        gd = new GestureDetector(new ZGestureListener(gameWorld, gameRenderer));
        gestureRecognizer = new GestureRecognizerInputProcessor();
        im.addProcessor(gestureRecognizer);
        im.addProcessor(gd);
        im.addProcessor(stage);
        Gdx.input.setInputProcessor(im);

        // temporary
        buttonA.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Button A Pressed");
            }
        });

        buttonB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Button B Pressed");
            }
        });



        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("pauseButton Pressed");
                gameWorld.setState(2);
            }
        });

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("playButton Pressed");
                gameWorld.setState(1);
            }
        });

        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("restartButton Pressed");
                gameWorld.setState(0);
            }
        });

        table = new Table();
        table.setFillParent(true);
        table.setDebug(true);

        table.align(Align.top | Align.right);
        table.pad(5);

        // temporary
        table.add(buttonA);
        table.add(buttonB);

        table.add(pauseButton);
        table.add(playButton);
        table.add(restartButton);
        stage.addActor(table);

    }


    @Override
    public void show() {
        Gdx.app.log("MainScreen", "show called");
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
