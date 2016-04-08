package com.oniz.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.oniz.Mobs.GestureRock;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Singleton holding all assets.
 */
public final class AssetLoader {
    /*
    volatile keyword helps as concurrency control tool in a multithreaded environment
    and provides the latest update in a most accurate manner.
    However please note that double check locking might not work before Java 5.
     */
    private static volatile AssetLoader instance = null;

    public Hashtable<String, Texture> textures;
    public Hashtable<String, TextureRegion> sprites;
    public Hashtable<String, Sound> sounds;
    public Hashtable<String, BitmapFont> fonts;
    public Hashtable<GestureRock.GestureType, TextureRegion> gestureHints;
    public Hashtable<GestureRock.Stage, TextureRegion> gestureStages;
    public Animation zombieClimbingAnimation, enemyZombieClimbingAnimation, explosionAnimation;
    private static Preferences prefs;
    public Skin skin;

    public static AssetLoader getInstance() {
        if (instance == null) {
            synchronized (AssetLoader.class) {
                if (instance == null) {
                    instance = new AssetLoader();
                }
            }
        }
        return instance;
    }

    public AssetLoader() {
        textures = new Hashtable<String, Texture>();
        sprites = new Hashtable<String, TextureRegion>();
        sounds = new Hashtable<String, Sound>();
        fonts = new Hashtable<String, BitmapFont>();
        gestureHints = new Hashtable<GestureRock.GestureType, TextureRegion>();
        gestureStages = new Hashtable<GestureRock.Stage, TextureRegion>();
        prefs = Gdx.app.getPreferences("ONIZ");
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        load();
    }

    public void load() {

        // store high score
        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }

        // splash screen logo
        textures.put("logoTexture", new Texture(Gdx.files.internal("data/logo.png")));
        textures.get("logoTexture").setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        sprites.put("logo", new TextureRegion(textures.get("logoTexture"), 0, 0, 512, 114));

        // fonts
        fonts.put("scoreText", new BitmapFont(Gdx.files.internal("data/carterOne.fnt"), true));
        fonts.get("scoreText").getData().setScale(1f, -1f);
        fonts.put("menuText", new BitmapFont(Gdx.files.internal("data/carterOne.fnt"), true));
        fonts.get("menuText").getData().setScale(0.5f, -0.5f);

        // menu icons V2
        textures.put("menuTexture", new Texture(Gdx.files.internal("data/menuIcons.png")));
        sprites.put("mainPlayBtnUp", new TextureRegion(textures.get("menuTexture"), 0, 0, 200, 120));
        sprites.put("mainPlayBtnDown", new TextureRegion(textures.get("menuTexture"), 0, 120, 200, 120));
        sprites.put("loginBtnUp", new TextureRegion(textures.get("menuTexture"), 0, 240, 200, 80));
        sprites.put("loginBtnDown", new TextureRegion(textures.get("menuTexture"), 0, 320, 200, 80));
        sprites.put("logoutBtnUp", new TextureRegion(textures.get("menuTexture"), 0, 400, 200, 80));
        sprites.put("logoutBtnDown", new TextureRegion(textures.get("menuTexture"), 0, 480, 200, 80));
        sprites.put("settingsBtnUp", new TextureRegion(textures.get("menuTexture"), 0, 560, 80, 80));
        sprites.put("settingsBtnDown", new TextureRegion(textures.get("menuTexture"), 80, 560, 80, 80));
        sprites.put("singlePlayerBtnUp", new TextureRegion(textures.get("menuTexture"), 0, 640, 80, 80));
        sprites.put("singlePlayerBtnDown", new TextureRegion(textures.get("menuTexture"), 80, 640, 80, 80));
        sprites.put("pauseBtnUp", new TextureRegion(textures.get("menuTexture"), 0, 720, 80, 80));
        sprites.put("pauseBtnDown", new TextureRegion(textures.get("menuTexture"), 80, 720, 80, 80));
        sprites.put("resumeBtnUp", new TextureRegion(textures.get("menuTexture"), 160, 560, 80, 80));
        sprites.put("resumeBtnDown", new TextureRegion(textures.get("menuTexture"), 240, 560, 80, 80));
        sprites.put("menuBtnUp", new TextureRegion(textures.get("menuTexture"), 160, 640, 80, 80));
        sprites.put("menuBtnDown", new TextureRegion(textures.get("menuTexture"), 240, 640, 80, 80));
        sprites.put("restartBtnUp", new TextureRegion(textures.get("menuTexture"), 160, 720, 80, 80));
        sprites.put("restartBtnDown", new TextureRegion(textures.get("menuTexture"), 240, 720, 80, 80));
        sprites.put("pauseMenu", new TextureRegion(textures.get("menuTexture"), 0, 800, 400, 200));
        sprites.put("gameOverMenu", new TextureRegion(textures.get("menuTexture"), 200, 0, 440, 320));

        // gesture stages
        sprites.put("brownRock", new TextureRegion(textures.get("menuTexture"), 1900, 0, 100, 100));
        sprites.put("redRock", new TextureRegion(textures.get("menuTexture"), 1900, 100, 100, 100));
        sprites.put("yellowRock", new TextureRegion(textures.get("menuTexture"), 1900, 200, 100, 100));
        sprites.put("greenRock", new TextureRegion(textures.get("menuTexture"), 1900, 300, 100, 100));
        gestureStages.put(GestureRock.Stage.BROWN, sprites.get("brownRock"));
        gestureStages.put(GestureRock.Stage.RED, sprites.get("redRock"));
        gestureStages.put(GestureRock.Stage.YELLOW, sprites.get("yellowRock"));
        gestureStages.put(GestureRock.Stage.GREEN, sprites.get("greenRock"));

        // gesture hints
        sprites.put("horizontalLine", new TextureRegion(textures.get("menuTexture"), 1900, 400, 100, 100));
        sprites.put("verticalLine", new TextureRegion(textures.get("menuTexture"), 1900, 500, 100, 100));
        sprites.put("Vshape", new TextureRegion(textures.get("menuTexture"), 1900, 600, 100, 100));
        sprites.put("invertedVshape", new TextureRegion(textures.get("menuTexture"), 1900, 700, 100, 100));
        sprites.put("alpha", new TextureRegion(textures.get("menuTexture"), 1900, 800, 100, 100));
        sprites.put("gamma", new TextureRegion(textures.get("menuTexture"), 1900, 900, 100, 100));
        sprites.put("Zshape", new TextureRegion(textures.get("menuTexture"), 1800, 0, 100, 100));
        sprites.put("invertedZshape", new TextureRegion(textures.get("menuTexture"), 1800, 100, 100, 100));
        sprites.put("Mshape", new TextureRegion(textures.get("menuTexture"), 1800, 200, 100, 100));
        sprites.put("sigma", new TextureRegion(textures.get("menuTexture"), 1800, 300, 100, 100));
        sprites.put("triangle", new TextureRegion(textures.get("menuTexture"), 1800, 400, 100, 100));
        sprites.put("invertedCshape", new TextureRegion(textures.get("menuTexture"), 1800, 500, 100, 100));
        gestureHints.put(GestureRock.GestureType.VERTICAL_LINE, sprites.get("verticalLine"));
        gestureHints.put(GestureRock.GestureType.V_SHAPE, sprites.get("Vshape"));
        gestureHints.put(GestureRock.GestureType.INVERTED_V_SHAPE, sprites.get("invertedVshape"));
        gestureHints.put(GestureRock.GestureType.ALPHA, sprites.get("alpha"));
        gestureHints.put(GestureRock.GestureType.Z_SHAPE, sprites.get("Zshape"));
        gestureHints.put(GestureRock.GestureType.INVERTED_Z_SHAPE, sprites.get("invertedZshape"));
        gestureHints.put(GestureRock.GestureType.GAMMA, sprites.get("gamma"));

        // play, pause, restart buttons (obsolete)
        textures.put("pauseUp", new Texture(Gdx.files.internal("lineLight/lineLight12.png")));
        textures.put("pauseDown", new Texture(Gdx.files.internal("shadedDark/shadedDark14.png")));
        textures.put("playUp", new Texture(Gdx.files.internal("lineLight/lineLight14.png")));
        textures.put("playDown", new Texture(Gdx.files.internal("shadedDark/shadedDark16.png")));
        textures.put("restartUp", new Texture(Gdx.files.internal("lineLight/lineLight10.png")));
        textures.put("restartDown", new Texture(Gdx.files.internal("shadedDark/shadedDark12.png")));

        // temporary buttons: A, B, X, Y
        textures.put("buttonAUp", new Texture(Gdx.files.internal("lineLight/lineLight34.png")));
        textures.put("buttonADown", new Texture(Gdx.files.internal("shadedDark/shadedDark36.png")));
        textures.put("buttonBUp", new Texture(Gdx.files.internal("lineLight/lineLight35.png")));
        textures.put("buttonBDown", new Texture(Gdx.files.internal("shadedDark/shadedDark37.png")));
        textures.put("buttonXUp", new Texture(Gdx.files.internal("lineLight/lineLight36.png")));
        textures.put("buttonXDown", new Texture(Gdx.files.internal("shadedDark/shadedDark38.png")));
        textures.put("buttonYUp", new Texture(Gdx.files.internal("lineLight/lineLight37.png")));
        textures.put("buttonYDown", new Texture(Gdx.files.internal("shadedDark/shadedDark39.png")));

        // sprites of zombie climbing
        textures.put("zombies", new Texture(Gdx.files.internal("data/climbAnimation.png")));
        sprites.put("zombieClimb1", new TextureRegion(textures.get("zombies"), 17, 24, 60, 122));
        sprites.put("zombieClimb2", new TextureRegion(textures.get("zombies"), 89, 24, 60, 122));
        sprites.put("zombieClimb3", new TextureRegion(textures.get("zombies"), 161, 24, 60, 122));
        sprites.put("zombieClimb4", new TextureRegion(textures.get("zombies"), 233, 24, 60, 122));
        sprites.put("zombieClimb5", new TextureRegion(textures.get("zombies"), 305, 24, 60, 122));
        sprites.put("zombieClimb6", new TextureRegion(textures.get("zombies"), 377, 24, 60, 122));

        // zombie climbing animation
        TextureRegion[] zombieClimbing = {sprites.get("zombieClimb1"), sprites.get("zombieClimb2"), sprites.get("zombieClimb3"),
                                          sprites.get("zombieClimb4"), sprites.get("zombieClimb5"), sprites.get("zombieClimb6")};
        zombieClimbingAnimation = new Animation(0.2f, zombieClimbing);
        zombieClimbingAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // sprites of zombie climbing (ENEMY)
        textures.put("enemyZombies", new Texture(Gdx.files.internal("data/climbAnimationEnemy.png")));
        sprites.put("enemyZombieClimb1", new TextureRegion(textures.get("enemyZombies"), 17, 24, 60, 122));
        sprites.put("enemyZombieClimb2", new TextureRegion(textures.get("enemyZombies"), 89, 24, 60, 122));
        sprites.put("enemyZombieClimb3", new TextureRegion(textures.get("enemyZombies"), 161, 24, 60, 122));
        sprites.put("enemyZombieClimb4", new TextureRegion(textures.get("enemyZombies"), 233, 24, 60, 122));
        sprites.put("enemyZombieClimb5", new TextureRegion(textures.get("enemyZombies"), 305, 24, 60, 122));
        sprites.put("enemyZombieClimb6", new TextureRegion(textures.get("enemyZombies"), 377, 24, 60, 122));

        // zombie climbing animation (ENEMY)
        TextureRegion[] enemyZombieClimbing = {sprites.get("enemyZombieClimb1"), sprites.get("enemyZombieClimb2"), sprites.get("enemyZombieClimb3"),
                                               sprites.get("enemyZombieClimb4"), sprites.get("enemyZombieClimb5"), sprites.get("enemyZombieClimb6")};
        enemyZombieClimbingAnimation = new Animation(0.2f, enemyZombieClimbing);
        enemyZombieClimbingAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // sprites of explosion
        textures.put("explosion", new Texture(Gdx.files.internal("data/explosion.png")));
        sprites.put("explosion1", new TextureRegion(textures.get("explosion"), 0, 0, 120, 120));
        sprites.put("explosion2", new TextureRegion(textures.get("explosion"), 130, 0, 120, 120));
        sprites.put("explosion3", new TextureRegion(textures.get("explosion"), 260, 0, 120, 120));
        sprites.put("explosion4", new TextureRegion(textures.get("explosion"), 380, 0, 120, 120));
        sprites.put("explosion5", new TextureRegion(textures.get("explosion"), 0, 130, 120, 120));
        sprites.put("explosion6", new TextureRegion(textures.get("explosion"), 130, 130, 120, 120));
        sprites.put("explosion7", new TextureRegion(textures.get("explosion"), 260, 130, 120, 120));
        sprites.put("explosion8", new TextureRegion(textures.get("explosion"), 380, 130, 120, 120));
        sprites.put("explosion9", new TextureRegion(textures.get("explosion"), 0, 260, 120, 120));
        sprites.put("explosion10", new TextureRegion(textures.get("explosion"), 130, 260, 120, 120));
        sprites.put("explosion11", new TextureRegion(textures.get("explosion"), 260, 260, 120, 120));
        sprites.put("explosion12", new TextureRegion(textures.get("explosion"), 380, 260, 120, 120));
        sprites.put("explosion13", new TextureRegion(textures.get("explosion"), 0, 380, 120, 120));
        sprites.put("explosion14", new TextureRegion(textures.get("explosion"), 130, 380, 120, 120));

        // explosion animation
        TextureRegion[] explosion = {sprites.get("explosion1"), sprites.get("explosion2"), sprites.get("explosion3"), sprites.get("explosion4"), sprites.get("explosion5"),
                                     sprites.get("explosion6"), sprites.get("explosion7"), sprites.get("explosion8"), sprites.get("explosion9"), sprites.get("explosion10"),
                                     sprites.get("explosion11"), sprites.get("explosion12"), sprites.get("explosion13"), sprites.get("explosion14")};
        explosionAnimation = new Animation(0.15f, explosion);
        explosionAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        // background
        textures.put("backgrounds", new Texture(Gdx.files.internal("data/backgrounds.png")));
        sprites.put("pizzaBuilding", new TextureRegion(textures.get("backgrounds"), 0, 0, 450, 800));
        sprites.put("tallBuilding", new TextureRegion(textures.get("backgrounds"), 450, 0, 450, 800));
        sprites.put("nightBuilding", new TextureRegion(textures.get("backgrounds"), 900, 0, 450, 800));
        sprites.put("cloudsBackground", new TextureRegion(textures.get("backgrounds"), 1350, 0, 450, 800));

        textures.put("waitingBg", new Texture(Gdx.files.internal("data/waitingScreen.png")));
        sprites.put("waitingBackground", new TextureRegion(textures.get("waitingBg"), 338, 1, 325, 758));

        /*
        // menu icons (UNUSED FOR NOW)
        textures.put("menuTexture", new Texture(Gdx.files.internal("data/menuIcons.png")));
        sprites.put("pauseTitle", new TextureRegion(textures.get("menuTexture"), 240, 0, 320, 100));
        sprites.put("playUp", new TextureRegion(textures.get("menuTexture"), 240, 580, 260, 160));
        sprites.put("playDown", new TextureRegion(textures.get("menuTexture"), 240, 740, 260, 160));
        sprites.put("playAgainUp", new TextureRegion(textures.get("menuTexture"), 240, 260, 280, 160));
        sprites.put("playAgainDown", new TextureRegion(textures.get("menuTexture"), 240, 420, 280, 160));
        sprites.put("resumeUp", new TextureRegion(textures.get("menuTexture"), 0, 0, 200, 100));
        sprites.put("resumeDown", new TextureRegion(textures.get("menuTexture"), 0, 100, 200, 100));
        sprites.put("restartUp", new TextureRegion(textures.get("menuTexture"), 0, 200, 200, 100));
        sprites.put("restartDown", new TextureRegion(textures.get("menuTexture"), 0, 300, 200, 100));
        sprites.put("homeUp", new TextureRegion(textures.get("menuTexture"), 0, 400, 200, 100));
        sprites.put("homeDown", new TextureRegion(textures.get("menuTexture"), 0, 500, 200, 100));
        sprites.put("pauseUp", new TextureRegion(textures.get("menuTexture"), 12, 814, 72, 62));
        sprites.put("pauseDown", new TextureRegion(textures.get("menuTexture"), 12, 894, 72, 62));
        */
    }

    // Receives an integer and maps it to the String highScore in prefs
    public static void setHighScore(int score) {
        prefs.putInteger("highScore", score);
        prefs.flush();
    }

    // Retrieves the current high score
    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }

    public void dispose() {
        for (Object o : textures.values().toArray()) {
            Texture temp = (Texture) o;
            textures.remove(o);
            temp.dispose();
        }
        for (Object o : fonts.values().toArray()) {
            BitmapFont temp = (BitmapFont) o;
            fonts.remove(o);
            temp.dispose();
        }
        for (Object o : sounds.values().toArray()) {
            Sound temp = (Sound) o;
            sounds.remove(o);
            temp.dispose();
        }
    }
}
