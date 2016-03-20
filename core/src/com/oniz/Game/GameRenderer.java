package com.oniz.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.oniz.Mobs.ChildZombie;
import com.oniz.TweenAccessors.Value;
import com.oniz.TweenAccessors.ValueAccessor;
import com.oniz.UI.MenuButton;

import java.util.ArrayList;
import java.util.Hashtable;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

/**
 * GameRenderer class to handle graphics rendering.
 */
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
    private Hashtable<String, MenuButton> menuButtons;

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

    /**
     * Draw zombies freeze in time when game is paused or game is over.
     */
    private void drawZombiesFreezeFrame() {
        for(int i = 0; i < childZombies.size(); i++){
            // draw zombies climbing (freeze frame)
            batcher.draw(zombieClimbingAnimation.getKeyFrame(freezeFrameTime + i), childZombies.get(i).getX(),
                    childZombies.get(i).getY(), childZombies.get(i).getWidth(), childZombies.get(i).getHeight());
            // draw corresponding gesture hints
            batcher.draw(gestureHints.get(childZombies.get(i).getGestureType()), childZombies.get(i).getX()+16, childZombies.get(i).getY()+45, 30, 30);
        }
    }

    /**
     * Rendering all the graphics - this is where the magic happens.
     * @param deltaTime - time since the game started
     */
    public void render(float deltaTime) {

        // set black to prevent flicker. rgba format.
        Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batcher.begin();
        batcher.disableBlending();

        // draw the background building
        batcher.draw(background, 0, 0, 450, 800);

        batcher.enableBlending();

        // when game is ready, draw the play button
        if (gameWorld.isReady()) {
            menuButtons.get("playButton").draw(batcher);

        // when game is running, animate the zombies and gesture hints, and also draw the PAUSE button
        } else if (gameWorld.isRunning()) {
            for(int i = 0; i < childZombies.size(); i++){
                // draw zombies climbing with animation
                batcher.draw(zombieClimbingAnimation.getKeyFrame(deltaTime + i), childZombies.get(i).getX(),
                        childZombies.get(i).getY(), childZombies.get(i).getWidth(), childZombies.get(i).getHeight());
                // draw corresponding gesture hints
                batcher.draw(gestureHints.get(childZombies.get(i).getGestureType()), childZombies.get(i).getX()+16, childZombies.get(i).getY()+45, 30, 30);
            }
            menuButtons.get("pauseButton").draw(batcher);

            // refresh freezeFrameTime with the current running time
            freezeFrameTime = deltaTime;

        // when game is paused, freeze the animating zombies, and display the pause menu
        } else if (gameWorld.isPaused()) {
            drawZombiesFreezeFrame();
            drawPauseMenu();

        // when game is over, freeze the animating zombies, and draw the PLAY AGAIN button
        } else if (gameWorld.isGameOver()) {
            drawZombiesFreezeFrame();
            menuButtons.get("playAgainButton").draw(batcher);
        }

        batcher.end();
    }
}
