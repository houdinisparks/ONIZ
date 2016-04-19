package com.oniz.Sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.oniz.Game.AssetLoader;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by yanyee on 4/19/2016.
 * SoundManager manages how the sound will be played.
 */
public class SoundManager {

    /*
    FIELDS
    */
    private static SoundManager instance;
    private static EnumMap<SoundFX, Sound> soundFXs;
    private static EnumMap<BGM, Music> soundBGMS;
    private static Callable<Sound> computeRockCrash;
    private static Callable<Sound> computeExplosion;
    private static Future<Sound> rockCrash;
    private static Future<Sound> explosion;
    private static ExecutorService poolOfThreads;


    private static Music intenselayerBGM;
    private static Music normallayerBGM;

    private boolean bgmIsPlaying;

    /*
    ENUMS
     */
    public enum SoundFX {
        EXPLODE1,
        EXPLODE2,
        EXPLODE3,
        EXPLODE4,
        ROCKCRACK1,
        ROCKCRACK2,
        ROCKCRACK3,
        ROCKCRACK4,
        UIBUTTONUP,
        UIBUTTONCLICKDOWN;

        private static final SoundFX[] EXPLODE_FXES = Arrays.copyOfRange(values(), 0, 4);
        private static final SoundFX[] ROCKCRASHES_FXES = Arrays.copyOfRange(values(), 4, 8);

        private static Random random = new Random();

        public static SoundFX generateRandomExplosionFX() {
            return EXPLODE_FXES[random.nextInt(EXPLODE_FXES.length - 1)];

        }

        public static SoundFX generateRandomRockCrashFX() {
            return ROCKCRASHES_FXES[random.nextInt(ROCKCRASHES_FXES.length - 1)];

        }
    }

    public enum BGM {
        BATTLEMUSICNORMALLAYER,
        BATTLEMUSICINTENSELAYER;

    }
    /*
    CONSTRUCTOR
     */

    private SoundManager() {

        poolOfThreads = Executors.newCachedThreadPool();
        soundFXs = AssetLoader.getInstance().soundFXs;
        soundBGMS = AssetLoader.getInstance().soundBGM;

        intenselayerBGM = soundBGMS.get(BGM.BATTLEMUSICINTENSELAYER);
        normallayerBGM = soundBGMS.get(BGM.BATTLEMUSICNORMALLAYER);

        computeRockCrash = new Callable<Sound>() {
            @Override
            public Sound call() throws Exception {
                Gdx.app.log("Future", "Called");
                return soundFXs.get(SoundFX.generateRandomRockCrashFX());
            }
        };

        computeExplosion = new Callable<Sound>() {
            @Override
            public Sound call() throws Exception {
                return soundFXs.get(SoundFX.generateRandomExplosionFX());
            }
        };

         rockCrash = poolOfThreads.submit(computeRockCrash);
         explosion = poolOfThreads.submit(computeExplosion);

        bgmIsPlaying = false;

    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void playExplosion() {
        try {
            explosion.get().play();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //soundFXs.get(SoundFX.generateRandomExplosionFX()).play();

        explosion = poolOfThreads.submit(computeExplosion);

    }

    public void playRockCrack() {
        try {
            rockCrash.get().play();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //soundFXs.get(SoundFX.generateRandomRockCrashFX()).play();
        rockCrash = poolOfThreads.submit(computeRockCrash);
    }

    public void playUIButtonUp() {
        soundFXs.get(SoundFX.UIBUTTONUP).play();
    }

    public void playUIButtonClickDown() {
        soundFXs.get(SoundFX.UIBUTTONCLICKDOWN).play();
    }

    public synchronized void playBattleMusic() {
        //this method must be atomic.
        //set a high prority thread to start the music at the same time
        if (!bgmIsPlaying) {
            bgmIsPlaying = true;
            intenselayerBGM.setVolume(0);
            normallayerBGM.play();
            intenselayerBGM.play();
            //intenselayerBGM.setPosition(normallayerBGM.getPosition());

        }

    }

    public synchronized void pauseBattleMusic() {
        if (bgmIsPlaying) {
            intenselayerBGM.pause();
            normallayerBGM.pause();
            bgmIsPlaying = false;
        }
    }

    public synchronized void stopBattleMusic() {
        if(bgmIsPlaying){
            normallayerBGM.stop();
            intenselayerBGM.stop();
            bgmIsPlaying = false;
        }

    }

    //used in update function of the gameworld
    public synchronized void fadeInProLayerBattleMusic(float delta, float fadeTimePerSecond) {
        Gdx.app.log("delta and volume: " , delta + " " + getProLayerVolume()) ;

        if (intenselayerBGM.isPlaying() || intenselayerBGM.isLooping()) {
            float volume = intenselayerBGM.getVolume();
            if (volume <= 1.0){
                volume += delta * fadeTimePerSecond;
                intenselayerBGM.setVolume(volume);
            }
        }
        Gdx.app.log("Fading In: " , intenselayerBGM.getVolume() + "");
    }

    public synchronized void fadeOutProLayerBattleMusic(float delta, float fadeTime) {

        if (intenselayerBGM.isPlaying() || intenselayerBGM.isLooping()) {
            float volume = intenselayerBGM.getVolume();
            if (volume <= 1.0){
                volume -= delta * fadeTime;
                intenselayerBGM.setVolume(volume);
            }
        }
    }

    public void checkMusicPosition() {

    }

    public float getProLayerVolume() {
        return intenselayerBGM.getVolume();
    }

}
