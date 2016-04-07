package com.oniz.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.oniz.Gestures.DrawPathGraphics.mesh.SwipeTriStrip;
import com.oniz.Gestures.GestureRecognizerInputProcessor;
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

    // Game Assets
    private TextureRegion background;
    private Animation zombieClimbingAnimation, enemyZombieClimbingAnimation, explosionAnimation;
    private TextureRegion zombie;
    private TextureRegion pauseTitle;
    private BitmapFont font;

    // Buttons
    private Hashtable<String, MenuButton> menuButtons;

    // Tween stuff
    private TweenManager manager;
    private Value alpha = new Value();

    // temporary
    private volatile float freezeFrameTime = 0;

    /*
    GESTURE-ONLY RELATED ASSETS
     */
    private SwipeTriStrip swipeTriStrip;
    private GestureRecognizerInputProcessor gestureRecognizerInputProcessor;
    private Texture gesturePathTexture;



    public GameRenderer(GameWorld gameWorld , InputMultiplexer inputMultiplexer) {
        this.gameWorld = gameWorld;
        this.menuButtons = ((InputHandler) inputMultiplexer.getProcessors().get(0)).getMenuButtons();


        //Viewport - Aspect Radio
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 450, 800); //false for y upwards

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

        /*-------------------------------------------
        GESTURE-ONLY RELATED SET-UP
         */
        swipeTriStrip = new SwipeTriStrip();
        gestureRecognizerInputProcessor = (GestureRecognizerInputProcessor)inputMultiplexer.getProcessors().get(1);
        gestureRecognizerInputProcessor.minDistance = 10;
        gesturePathTexture = new Texture(Gdx.files.internal("gestures/gesturepath/gradient.png"));
        gesturePathTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        /*
        ----------------------------------------------
         */
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
    }

    private void initAssets() {
        background = AssetLoader.getInstance().sprites.get("background");
        zombieClimbingAnimation = AssetLoader.getInstance().zombieClimbingAnimation;
        enemyZombieClimbingAnimation = AssetLoader.getInstance().enemyZombieClimbingAnimation;
        explosionAnimation = AssetLoader.getInstance().explosionAnimation;
        zombie = AssetLoader.getInstance().sprites.get("zombieClimb3");
        pauseTitle = AssetLoader.getInstance().sprites.get("pauseTitle");
        font = AssetLoader.getInstance().fonts.get("badaboom");
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
            // draw zombies
            if (childZombies.get(i).isEnemy()) {
                batcher.draw(enemyZombieClimbingAnimation.getKeyFrame(freezeFrameTime + i), childZombies.get(i).getX(),
                        childZombies.get(i).getY(), childZombies.get(i).getWidth(), childZombies.get(i).getHeight());
            } else {
                batcher.draw(zombieClimbingAnimation.getKeyFrame(freezeFrameTime + i), childZombies.get(i).getX(),
                        childZombies.get(i).getY(), childZombies.get(i).getWidth(), childZombies.get(i).getHeight());
            }

            // draw corresponding gesture rocks
            if (!childZombies.get(i).isExploding()) {
                childZombies.get(i).getGestureRock().draw(batcher);
            }
        }
    }

    /**
     * Rendering all the graphics - this is where the magic happens.
     * @param runTime - time since the game started
     * @param deltaTime - incremental increase in time
     */
    public void render(float runTime, float deltaTime) {

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
                // draw zombies
                if (childZombies.get(i).isEnemy()) {
                    batcher.draw(enemyZombieClimbingAnimation.getKeyFrame(runTime + i), childZombies.get(i).getX(),
                            childZombies.get(i).getY(), childZombies.get(i).getWidth(), childZombies.get(i).getHeight());
                } else {
                    batcher.draw(zombieClimbingAnimation.getKeyFrame(runTime + i), childZombies.get(i).getX(),
                            childZombies.get(i).getY(), childZombies.get(i).getWidth(), childZombies.get(i).getHeight());
                }

                // if not detonated, draw corresponding gesture rocks
                if (!childZombies.get(i).isExploding()) {
                    childZombies.get(i).getGestureRock().draw(batcher);
                }

                // draw explosion animation
                childZombies.get(i).drawExplosion(batcher, explosionAnimation, deltaTime);
            }
            menuButtons.get("pauseButton").draw(batcher);

            // refresh freezeFrameTime with the current running time
            freezeFrameTime = runTime;

        // when game is paused, freeze the animating zombies, and display the pause menu
        } else if (gameWorld.isPaused()) {
            drawZombiesFreezeFrame();
            drawPauseMenu();

        // when game is over, freeze the animating zombies, and draw the PLAY AGAIN button
        } else if (gameWorld.isGameOver()) {
            drawZombiesFreezeFrame();
            menuButtons.get("playAgainButton").draw(batcher);
        }

        // Convert integer into String
        String score = Integer.toString(gameWorld.getScore());
        // Draw score
        font.draw(batcher, score, (450/2) - (18*score.length()), 750);

        batcher.end();

        /*
        GESTURE-ONLY RELATED RENDER
         */
        gesturePathTexture.bind();
        swipeTriStrip.endcap = 5f;
        swipeTriStrip.thickness = 30f;
        swipeTriStrip.update(gestureRecognizerInputProcessor.path());
        swipeTriStrip.color = Color.WHITE;
        swipeTriStrip.draw(cam);
    }
}
