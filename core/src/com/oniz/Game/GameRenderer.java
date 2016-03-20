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
import com.oniz.UI.SimpleButton;

import java.util.ArrayList;
import java.util.Hashtable;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;


public class GameRenderer {
    private GameWorld gameWorld;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batcher;
    private Color transitionColor;

    // Game Objects
    private ArrayList<ChildZombie> childZombies;
    private ArrayList<TextureRegion> gestureHints;

    // Game Assets
    private TextureRegion background;
    private Animation zombieClimbingAnimation;
    private TextureRegion pauseTitle;

    // Buttons
    private Hashtable<String, SimpleButton> menuButtons;

    // Tween stuff
    private TweenManager manager;
    private Value alpha = new Value();

    // temporary
    private volatile float freezeFrameTime = 0;


    public GameRenderer(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.menuButtons = ((InputHandler) Gdx.input.getInputProcessor()).getMenuButtons();

        cam = new OrthographicCamera();
        cam.setToOrtho(false, 450, 800); //false for y upwards

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

        transitionColor = new Color();
        prepareTransition(255, 255, 255, .5f);

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

    public void prepareTransition(int r, int g, int b, float duration) {
        transitionColor.set(r / 255.0f, g / 255.0f, b / 255.0f, 1);
        alpha.setValue(1);
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        Tween.to(alpha, -1, duration).target(0)
                .ease(TweenEquations.easeOutQuad).start(manager);
    }

    private void initGameObjects() {
        childZombies = gameWorld.getChildZombies();
        gestureHints = AssetLoader.getInstance().gestureHints;
    }

    private void initAssets() {
        background = AssetLoader.getInstance().sprites.get("background");
        zombieClimbingAnimation = AssetLoader.getInstance().zombieClimbingAnimation;
        pauseTitle = AssetLoader.getInstance().sprites.get("pauseTitle");
    }

    private void drawPauseMenu() {
        batcher.draw(pauseTitle, 70, 550, 320, 100);
        menuButtons.get("resumeButton").draw(batcher);
        menuButtons.get("restartButton").draw(batcher);
        menuButtons.get("homeButton").draw(batcher);
    }

    private void drawZombiesFreezeFrame() {
        for(int i = 0; i < childZombies.size(); i++){
            // draw zombies climbing
            batcher.draw(zombieClimbingAnimation.getKeyFrame(freezeFrameTime + i), childZombies.get(i).getX(),
                    childZombies.get(i).getY(), childZombies.get(i).getWidth(), childZombies.get(i).getHeight());
            // draw corresponding gesture hints
            batcher.draw(gestureHints.get(childZombies.get(i).getGestureType()), childZombies.get(i).getX()+16, childZombies.get(i).getY()+45, 30, 30);
        }
    }

    public void render(float deltaTime) {

        // set black to prevent flicker. rgba format.
        Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batcher.begin();
        batcher.disableBlending();

        batcher.draw(background, 0, 0, 450, 800);

        batcher.enableBlending();

        if (gameWorld.isReady()) {
            menuButtons.get("playButton").draw(batcher);

        } else if (gameWorld.isRunning()) {
            for(int i = 0; i < childZombies.size(); i++){
                // draw zombies climbing with animation
                batcher.draw(zombieClimbingAnimation.getKeyFrame(deltaTime + i), childZombies.get(i).getX(),
                        childZombies.get(i).getY(), childZombies.get(i).getWidth(), childZombies.get(i).getHeight());
                // draw corresponding gesture hints
                batcher.draw(gestureHints.get(childZombies.get(i).getGestureType()), childZombies.get(i).getX()+16, childZombies.get(i).getY()+45, 30, 30);
            }
            menuButtons.get("pauseButton").draw(batcher);
            freezeFrameTime = deltaTime;

        } else if (gameWorld.isPaused()) {
            drawZombiesFreezeFrame();
            drawPauseMenu();

        } else if (gameWorld.isGameOver()) {
            drawZombiesFreezeFrame();
            menuButtons.get("playAgainButton").draw(batcher);
        }

        batcher.end();
    }
}
