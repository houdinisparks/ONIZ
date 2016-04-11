package com.oniz.Game;
;
import com.badlogic.gdx.Gdx;
import com.oniz.Mobs.ChildZombie;
import com.oniz.Mobs.GestureRock;
import com.oniz.Screens.MainScreen;

import java.util.ArrayList;
import java.util.Iterator;
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
    static final int[] zombiePaths = {40, 78, 115, 153, 190, 228, 265, 303, 340};
    private int state;
    private int score = 0;

    GameRenderer gameRenderer;
    Random random = new Random();
    ArrayList<ChildZombie> childZombies = new ArrayList<ChildZombie>();


    ZGame zgame;

    public GameWorld() {
        this.state = GAME_RUNNING;
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
        for (Iterator<ChildZombie> iterator = childZombies.iterator(); iterator.hasNext();){
            ChildZombie zombie = iterator.next();

            // if one of the zombies reaches the roof
            if (zombie.getY() > 580) {
                if (score > AssetLoader.getHighScore()) {
                    AssetLoader.setHighScore(score);
                }
                this.state = GAME_OVER;
            }

            // update living zombies and remove dead zombies
            if (zombie.isAlive()) {
                zombie.update(deltaTime);
            } else {
                score += 1;
//                Gdx.app.log("Zombie status", "killed");
                iterator.remove();


                //send zombie to other player, if multiplayer mode
                if(zgame.isMultiplayerMode()) {
                    zgame.playServices.broadcastMessage("SPAWN:ZOMBIE");
                }


            }
        }
        // spawn zombies at random time intervals
        if (random.nextInt(100) == 77) {
            spawnZombie(false);
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

    private void spawnZombie(boolean isEnemy) {
        ChildZombie childZombie = new ChildZombie(zombiePaths[random.nextInt(9)], -130, isEnemy);
        childZombies.add(childZombie);
    }

    public void weakenZombie(GestureRock.GestureType gestureType) {
        for (ChildZombie zombie: childZombies) {
            if (zombie.getGestureRock().getGestureType().equals(gestureType)) {
//                Gdx.app.log("Zombie status", "weakened");
                zombie.getGestureRock().decrementStage();
            }
        }
    }

    public ArrayList<ChildZombie> getChildZombies() {
        return childZombies;
    }

    public void restartGame() {
        // reset to initial state
        childZombies.clear();
        score = 0;
        state = GAME_RUNNING;
        gameRenderer.prepareTransition(0, 0, 0, 1f);
    }

    public void goHome() {
        this.zgame.switchScreen(ZGame.ScreenState.START);
    }

    public void setGame(ZGame zgame) {
        this.zgame = zgame;
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

    public int getScore() {
        return score;
    }

    public void realTimeUpdate(String msg) {
        //this method will be called by ONIZGameHelper when a message is received
        //we can decide what to do here
        //for starters it should spawn an additional zombie
        if(msg.startsWith("SPAWN")) {
            spawnZombie(true);
        }
//        Gdx.app.log("REALTIMEUPDATE", msg);
    }
}

