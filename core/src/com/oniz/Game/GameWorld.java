package com.oniz.Game;


import com.badlogic.gdx.Gdx;
import com.oniz.Mobs.ChildZombie;
import com.oniz.Mobs.GestureRock;
import com.oniz.Network.PlayEventListener;
import com.oniz.Sound.SoundManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * GameWorld class hold all the models and game states.
 * It updates everything.
 */
public class GameWorld implements PlayEventListener{
    public static final int BACKGROUND1 = 0;
    public static final int BACKGROUND2 = 1;
    public static final int BACKGROUND3 = 2;

    public static final int GAME_READY = 0;
    public static final int GAME_RUNNING = 1;
    public static final int GAME_PAUSED = 2;
    public static final int GAME_LEVEL_END = 3;
    public static final int GAME_OVER = 4;
    public static final int GAME_WINNER = 5;
    public static final int GAME_DISCONNECTED = 6;
    static final int[] zombiePaths = {40, 78, 115, 153, 190, 228, 265, 303, 340};
    private int state;
    private int score = 0;
    private int backgroundOption = BACKGROUND1;

    GameRenderer gameRenderer;
    Random random = new Random();
    ArrayList<ChildZombie> childZombies = new ArrayList<ChildZombie>();
    ZGame zgame;

    private boolean isMultiPlayerQuitted = false;
    public AtomicBoolean isMissed = new AtomicBoolean(false);

    //soundFXs
    private SoundManager soundManager;
    boolean cueProBGMLayer;
    boolean bgmAlreadyPlaying;


    public GameWorld() {
        this.state = GAME_RUNNING;
        soundManager = SoundManager.getInstance();
        bgmAlreadyPlaying = false;
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
                updateBattleMusic(deltaTime);
                break;
            case GAME_PAUSED:
                updatePaused();
                pauseBattleMusic();
                break;
//            case GAME_LEVEL_END:
//                updateLevelEnd();
//                break;
            case GAME_OVER:
                updateGameOver();
                stopBattleMusic();
                break;
            case GAME_WINNER:
                stopBattleMusic();
                break;
            case GAME_DISCONNECTED:
                stopBattleMusic();
                break;
        }
    }


    private void updateReady() {

    }

    private void pauseBattleMusic() {
        if (bgmAlreadyPlaying) {
            soundManager.pauseBattleMusic();
            bgmAlreadyPlaying = false;
        }
    }

    private void stopBattleMusic() {
        if (bgmAlreadyPlaying) {
            soundManager.stopBattleMusic();
            bgmAlreadyPlaying = false;
        }
    }

    private void updateBattleMusic(float delta) {
        //soundManager.checkMusicPosition();
        if (!bgmAlreadyPlaying) {
            soundManager.playBattleMusic();
            bgmAlreadyPlaying = true;
        }
        if (bgmAlreadyPlaying && cueProBGMLayer) {
            soundManager.fadeInProLayerBattleMusic(delta, 0.3f);
        }


    }

    private void updateRunning(float deltaTime) {
        // update zombie position
        cueProBGMLayer = false;
        for (Iterator<ChildZombie> iterator = childZombies.iterator(); iterator.hasNext(); ) {
            ChildZombie zombie = iterator.next();

            // if one of the zombies reaches the roof
            if (zombie.getY() > 580 && !zombie.isExploding()) {
                if (score > AssetLoader.getHighScore()) {
                    AssetLoader.setHighScore(score);
                }
                this.state = GAME_OVER;
            }

            //check if the zombie has reach a certiain limit (plays intenst music)

            if (zombie.getY() > 200 && !cueProBGMLayer) {
                cueProBGMLayer = true;
            }

            // update living zombies and remove dead zombies
            if (zombie.isAlive()) {
                zombie.update(deltaTime);
            } else {
                score += 1;
//                Gdx.app.log("Zombie status", "killed");
                iterator.remove();


                //send zombie to other player, if multiplayer mode
                if (zgame.isMultiplayerMode()) {
                    zgame.playServices.broadcastMessage("SPAWN:ZOMBIE");
                }
            }
        }
        // spawn zombies at random time intervals
        if (random.nextInt(100) == 77) {
            spawnZombie(false);
        }

        //send message before going into game_over state
        if(zgame.isMultiplayerMode() && (this.state == GAME_OVER)) {
            zgame.playServices.broadcastMessage("DEFEATED:PLAYERID");
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
        boolean SFXPlayed = false;
        for (ChildZombie zombie : childZombies) {
            if (zombie.getGestureRock().getGestureType().equals(gestureType)) {
//
                if (!SFXPlayed) {
                    soundManager.playRockCrack();
                    SFXPlayed = true;
                }
                zombie.getGestureRock().decrementStage();
            }
        }

    }

    public ArrayList<ChildZombie> getChildZombies() {
        return childZombies;
    }

    public void restartGame() {
        // reset to initial state
        zgame.setOpponentDefeated(false);
        childZombies.clear();
        score = 0;
        state = GAME_RUNNING;
        this.isMultiPlayerQuitted = false;
        gameRenderer.prepareTransition(0, 0, 0, 1f);
    }

    public void goHome() {
        stopBattleMusic();
        this.zgame.switchScreen(ZGame.ScreenState.START);
        this.isMultiPlayerQuitted = false;
        zgame.playServices.leaveGame();
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

    public boolean isGameWinner() { return state == GAME_WINNER; }

    public boolean isGameDisconnected() { return state == GAME_DISCONNECTED; }

    public boolean isMultiPlayerQuitted() {
        return isMultiPlayerQuitted;
    }

    public void setIsMultiPlayerQuitted(boolean isMultiPlayerQuitted) {
        this.isMultiPlayerQuitted = isMultiPlayerQuitted;
    }

    public int getScore() {
        return score;
    }

    public int getBackgroundOption() {
        return backgroundOption;
    }

    public void setBackgroundOption(int backgroundOption) {
        this.backgroundOption = backgroundOption;
        gameRenderer.setBackground(backgroundOption);
        zgame.getMatchMakingScreen().setBackground(backgroundOption);
    }

    public void realTimeUpdate(String msg) {
        //this method will be called by ONIZGameHelper when a message is received
        //we can decide what to do here
        //for starters it should spawn an additional zombie
        if (msg.startsWith("SPAWN")) {
            spawnZombie(true);
        } else if (msg.startsWith("DEFEATED") && !this.isGameOver()) {
            zgame.setOpponentDefeated(true);
            this.setState(GAME_WINNER);
        }
//        Gdx.app.log("REALTIMEUPDATE", msg);
    }

    @Override
    public void leftRoom() {
        //doesnt care
    }

    @Override
    public void disconnected() {
        Gdx.app.log("DCED", "disconnected event");
        //more important than leftRoom()
        //goto pause state then do stuff
        zgame.setMultiplayerMode(false);
        stopBattleMusic();
        //show splash?
        this.setState(GAME_DISCONNECTED);
    }
}

