package com.oniz.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.oniz.Mobs.ChildZombie;
import com.oniz.TweenAccessors.Value;
import com.oniz.TweenAccessors.ValueAccessor;

import java.util.ArrayList;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;


public class GameRenderer {
    private GameWorld gameWorld;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batcher;

    // Game Objects
    private ArrayList<ChildZombie> childZombies;
    private ArrayList<TextureRegion> gestureHints;

    // Game Assets
    private TextureRegion background;
    private Animation zombieClimbingAnimation;
    private TextureRegion play, restart, home, pauseTitle, pause;

    // Tween stuff
    private TweenManager manager;
    private Value alpha = new Value();


    public GameRenderer(GameWorld gameWorld) {
        this.gameWorld = gameWorld;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, 450, 800); //false for y upwards

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

        // Call helper methods to initialize instance variables
        initGameObjects();
        initAssets();
    }

    public OrthographicCamera getCam (){
        return this.cam;
    }

    private void setupTweens() {
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        Tween.to(alpha, -1, .5f).target(0).ease(TweenEquations.easeOutQuad)
                .start(manager);
    }

    private void initGameObjects() {
        childZombies = gameWorld.getChildZombies();
        gestureHints = AssetLoader.getInstance().gestureHints;
    }

    private void initAssets() {
        background = AssetLoader.getInstance().sprites.get("background");
        zombieClimbingAnimation = AssetLoader.getInstance().zombieClimbingAnimation;
        play = AssetLoader.getInstance().sprites.get("playUp");
        restart = AssetLoader.getInstance().sprites.get("restartUp");
        home = AssetLoader.getInstance().sprites.get("homeUp");
        pauseTitle = AssetLoader.getInstance().sprites.get("pauseTitle");
        pause = AssetLoader.getInstance().sprites.get("pause");
    }

    public void render(float deltaTime) {

        //set black to prevent flicker. rgba format.
        Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batcher.begin();
        batcher.disableBlending();

        batcher.draw(background, 0, 0, 450, 800);

        batcher.enableBlending();

        if (gameWorld.isRunning()) {
            for(int i = 0; i < childZombies.size(); i++){
                // draw zombies climbing
                batcher.draw(zombieClimbingAnimation.getKeyFrame(deltaTime + i), childZombies.get(i).getX(),
                        childZombies.get(i).getY(), childZombies.get(i).getWidth(), childZombies.get(i).getHeight());
                // draw corresponding gesture hints
                batcher.draw(gestureHints.get(childZombies.get(i).getGestureType()), childZombies.get(i).getX()+16, childZombies.get(i).getY()+45, 30, 30);
            }
        } else if (gameWorld.isGameOver()) {
            // draw "PAUSE" button
            batcher.draw(pause, 0, 0, 74, 62);
            // draw "PAUSE" title
            batcher.draw(pauseTitle, 80, 550, 293, 86);
            // draw "PLAY" icon
            batcher.draw(play, 140, 400, 160, 80);
            // draw "RESTART" icon
            batcher.draw(restart, 140, 300, 160, 80);
            // draw "HOME" icon
            batcher.draw(home, 140, 200, 160, 80);
        }

        batcher.end();
    }
}
