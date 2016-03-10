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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.oniz.Mobs.ChildZombie;
import com.oniz.Mobs.EvilRectangle;

import java.util.ArrayList;


public class GameRenderer {
    private GameWorld gameWorld;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batcher;

    // Game Objects
    private ArrayList<ChildZombie> childZombies;

    // Game Assets
    private TextureRegion background;
    private Animation zombieClimbingAnimation;


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

    private void initGameObjects() {
        childZombies = gameWorld.getChildZombies();
    }

    private void initAssets() {
        background = AssetLoader.getInstance().sprites.get("background");
        zombieClimbingAnimation = AssetLoader.getInstance().zombieClimbingAnimation;
    }

    public void render(float deltaTime) {

        //set black to prevent flicker. rgba format.
        Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


//        for( EvilRectangle rect : rectangles) {
//            if(rect.isAlive()) {
//                //draw stuff
//                shapeRenderer.begin(ShapeType.Filled);
//                // Chooses RGB Color of 87, 109, 120 at full opacity
//                shapeRenderer.setColor(255, 100, 100, 1);
//                // Draws the rectangle from myWorld (Using ShapeType.Filled)
//                shapeRenderer.rect(rect.x, rect.y,
//                        rect.width, rect.height);
//                // Tells the shapeRenderer to finish rendering
//                // We MUST do this every time.
//                shapeRenderer.end();
//            }
//        }

        batcher.begin();
        batcher.disableBlending();

        batcher.draw(background, 0, 0, 450, 800);

        batcher.enableBlending();

        for(int i = 0; i < childZombies.size(); i++){
            batcher.draw(zombieClimbingAnimation.getKeyFrame(deltaTime + i), childZombies.get(i).getX(),
                    childZombies.get(i).getY(), childZombies.get(i).getWidth(), childZombies.get(i).getHeight());
        }

        batcher.end();
    }
}
