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
 * Game world holds all the models.
 * It also has the overall game state.
 */
public class GameWorld {
    static final int GAME_READY = 0;
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER = 4;
    static final int[] zombiePaths = {40, 115, 190, 265, 340};
    int state;

    GameRenderer gameRenderer;
    Random random = new Random();
    ArrayList<ChildZombie> childZombies = new ArrayList<ChildZombie>();

    public GameWorld() {
        this.state = GAME_READY;
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
        for (int i = 0; i < childZombies.size(); i++){
            childZombies.get(i).update(deltaTime);

            // if one of the zombies reaches the roof
            if (childZombies.get(i).getY() > 550) {
                this.state = GAME_OVER;
            }
        }
        // spawn zombies at random time intervals
        if (random.nextInt(100) == 77) {
            spawnZombie();
        }
    }

    private void updatePaused() {
        //just don't pass delta to objects
    }

    private void updateGameOver() {
        System.out.println("GAME OVER!!!");
    }

    public void setState(int state) {
        this.state = state;
    }

    private void spawnZombie() {
        ChildZombie childZombie = new ChildZombie(zombiePaths[random.nextInt(5)], 0);
        childZombies.add(childZombie);
    }

    public void killZombie(int gestureType) {
        for (ChildZombie zombie: childZombies) {
            if (zombie.getGestureType() == gestureType) {
                childZombies.remove(zombie);
                break;
            }
        }
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

    public boolean isReady() {
        return state == GAME_READY;
    }

    public boolean isGameOver() {
        return state == GAME_OVER;
    }

    public boolean isPaused() {
        return state == GAME_PAUSED;
    }

    public boolean isRunning() {
        return state == GAME_RUNNING;
    }
}
