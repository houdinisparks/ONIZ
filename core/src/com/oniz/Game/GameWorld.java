package com.oniz.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.oniz.Mobs.ChildZombie;
import com.oniz.Mobs.EvilRectangle;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by robin on 1/3/16.
 * Game world holds all the models.
 * It also has the overall game state.
 */
public class GameWorld {
    static final int GAME_READY = 0;
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER = 4;

    int state;

    GameRenderer gameRenderer;

    //temp objects
    ArrayList<ChildZombie> childZombies = new ArrayList<ChildZombie>();
    int number = 3;

    public GameWorld() {
        this.state = 0;
        Random r = new Random();
        for(int i = 0; i < number; i++) {
            ChildZombie childZombie = new ChildZombie((i%3)*150 + 40, 20, 80, 125, r.nextInt(20)+10);
            childZombies.add(childZombie);
        }
    }

    public void setRenderer(GameRenderer gameRenderer) {
        this.gameRenderer = gameRenderer;
    }

    public void update(float deltaTime) {
        if (deltaTime > 0.1f) deltaTime = 0.1f;

        switch (state) {
            case GAME_READY:
                updateReady();
                break;
            case GAME_RUNNING:
                updateRunning(deltaTime);
                break;
            case GAME_PAUSED:
                updatePaused();
                break;
//            case GAME_LEVEL_END:
//                updateLevelEnd();
//                break;
            case GAME_OVER:
                updateGameOver();
                break;
        }
    }

    private void updateReady() {
    }

    private void updateRunning(float deltaTime) {
        // update zombie position
        for (ChildZombie zombie: childZombies){
            zombie.update(deltaTime);
        }
    }

    private void updatePaused() {
        //just dont pass delta to objects
    }

    private void updateGameOver() {
    }

    public void setState(int state) {
        this.state = state;
    }

    //give x, y within bounds providing top left corner
    public int[] getRandomPosition (int x, int y, int height, int width) {
        Random r = new Random();
        int randomX = r.nextInt(width + 1) + x;
        int randomY = r.nextInt(height + 1) + y;
        return new int[] {randomX, randomY};
    }

    public ArrayList<ChildZombie> getChildZombies() {
        return childZombies;
    }

//    public void reset() {
//        //method should reset all objects to initial state
//        for(EvilRectangle rect: rectangles) {
//            rect.setAlive(true);
//            int[] pos = getRandomPosition(0, 100, 50, 480);
//            rect.x = pos[0];
//            rect.y = pos[1];
//        }
//    }

//    public void checkCollision(float x, float y) {
//        /* concurrency issue should be done after position update in render */
//        for(EvilRectangle rect: rectangles) {
//            System.out.println(rect.isAlive());
//            if(rect.contains(x, y)) {
//                rect.setAlive(false);
//            }
//        }
//    }
}
