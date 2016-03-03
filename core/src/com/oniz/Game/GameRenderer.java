package com.oniz.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.oniz.Mobs.EvilRectangle;

import java.util.ArrayList;

/**
 * Created by robin on 1/3/16.
 */
public class GameRenderer {
    private GameWorld gameWorld;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    
    private ArrayList<EvilRectangle> rectangles;


    public GameRenderer(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 480, 800); //false for y upwards
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
    }

    public OrthographicCamera getCam (){
        return this.cam;
    }

    public void render(float deltaTime) {
        
        rectangles = gameWorld.getRectangles();

        //set black to prevent flicker. rgba format.
        Gdx.graphics.getGL20().glClearColor( 0, 0, 0, 1 );
        Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );



        for( EvilRectangle rect : rectangles) {
            if(rect.isAlive()) {
                //draw stuff
                shapeRenderer.begin(ShapeType.Filled);
                // Chooses RGB Color of 87, 109, 120 at full opacity
                shapeRenderer.setColor(255, 100, 100, 1);
                // Draws the rectangle from myWorld (Using ShapeType.Filled)
                shapeRenderer.rect(rect.x, rect.y,
                        rect.width, rect.height);
                // Tells the shapeRenderer to finish rendering
                // We MUST do this every time.
                shapeRenderer.end();
            }
        }
        
    }
}
