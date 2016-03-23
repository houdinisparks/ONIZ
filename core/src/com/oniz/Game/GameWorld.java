package com.oniz.Game;
;
import com.badlogic.gdx.Gdx;
import com.oniz.Mobs.ChildZombie;
import com.oniz.Mobs.ChildZombie.GestureType;
import java.util.ArrayList;
import java.util.Random;

/**
 * GameWorld class hold all the models and game states.
 */
public class GameWorld {
    public static final int GAME_READY = 0;
    public static final int GAME_RUNNING = 1;
    public static final int GAME_PAUSED = 2;
    public static final int GAME_LEVEL_END = 3;
    public static final int GAME_OVER = 4;
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
            if (childZombies.get(i).getY() > 548) {
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
//        System.out.println("GAME OVER!!!");
    }

    public void setState(int state) {
        this.state = state;
    }

    private void spawnZombie() {
        ChildZombie childZombie = new ChildZombie(zombiePaths[random.nextInt(5)], 0);
        childZombies.add(childZombie);
    }

    public void killZombie(GestureType gestureType) {
        for (ChildZombie zombie: childZombies) {
            if (zombie.getGestureType().equals(gestureType)) {
                Gdx.app.log("Kill Zombie" , "Zombie Rmeoved");
                childZombies.remove(zombie);
                break;
            }
        }
    }

    public ArrayList<ChildZombie> getChildZombies() {
        return childZombies;
    }

    public void restartGame() {
        // reset to initial state
        childZombies.clear();
        state = GAME_RUNNING;
        gameRenderer.prepareTransition(0, 0, 0, 1f);
    }

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
