package com.oniz.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.oniz.Gestures.ProtractorGestureRecognizer;
import com.oniz.Mobs.GestureRock;
import com.oniz.Sound.SoundManager.BGM;
import com.oniz.Sound.SoundManager.SoundFX;

import java.util.EnumMap;
import java.util.Hashtable;

import javax.xml.soap.Text;

/**
 * Singleton holding all assets.
 */
public final class AssetLoader {

    /*
    ENUMS
     */

    //Read only, no need thread safety and synchronisation.


    /*
    volatile keyword helps as concurrency control tool in a multithreaded environment
    and provides the latest update in a most accurate manner.
    However please note that double check locking might not work before Java 5.
     */

    private static volatile AssetLoader instance = null;

    public Hashtable<String, Texture> textures;
    public Hashtable<String, TextureRegion> sprites;
    public static EnumMap<SoundFX, Sound> soundFXs;
    public static EnumMap<BGM, Music> soundBGM;
    public Hashtable<String, BitmapFont> fonts;
    public Hashtable<GestureRock.GestureType, TextureRegion> gestureHints;
    public Hashtable<GestureRock.Stage, TextureRegion> gestureStages;
    public TextureRegion[] backgrounds;
    public Animation zombieClimbingAnimation, enemyZombieClimbingAnimation, explosionAnimation, spinnerAnimation, missAnimation;
    private static Preferences prefs;
    public Skin skin;

    /*gestures*/
    public Hashtable<String, FileHandle> gesturesList;
    public ProtractorGestureRecognizer protractorGestureRecognizer;

    public static final int BACKGROUND1 = 0;
    public static final int BACKGROUND2 = 1;
    public static final int BACKGROUND3 = 2;
    public static float soundVolume;

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
        soundFXs = new EnumMap<SoundFX, Sound>(SoundFX.class);
        soundBGM = new EnumMap<BGM, Music>(BGM.class);
        fonts = new Hashtable<String, BitmapFont>();
        gestureHints = new Hashtable<GestureRock.GestureType, TextureRegion>();
        gestureStages = new Hashtable<GestureRock.Stage, TextureRegion>();
        gesturesList = new Hashtable<String, FileHandle>();
        backgrounds = new TextureRegion[3];
        prefs = Gdx.app.getPreferences("ONIZ");
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        load();
    }

    public void loadGestures() {
        protractorGestureRecognizer = new ProtractorGestureRecognizer();

        Hashtable<String, FileHandle> gesturesList = AssetLoader.getInstance().gesturesList;

        for (String key :
                gesturesList.keySet()) {
            protractorGestureRecognizer.addGestureFromFile(gesturesList.get(key));
        }
    }

    private void loadGestureJSON() {
        FileHandle zShapeFileHandle;
        FileHandle invZShapeFileHandle;
        FileHandle horizontalFileHandle;
        FileHandle verticalFileHandle;
        FileHandle vShapeFileHandle;
        FileHandle invVShapeFileHandle;
        FileHandle alphaFileHandle;
        FileHandle gammaFileHandle;
        FileHandle sigmaFileHandle;
        FileHandle mShapeFileHandle;
        FileHandle revCShapeFileHandle;
        FileHandle triangleFileHandle;

        zShapeFileHandle = Gdx.files.internal("gestures/zshape/");
        invZShapeFileHandle = Gdx.files.internal("gestures/invertedzshape/");
        horizontalFileHandle = Gdx.files.internal("gestures/horizontalline/");
        verticalFileHandle = Gdx.files.internal("gestures/verticalline/");
        vShapeFileHandle = Gdx.files.internal("gestures/vshape/");
        invVShapeFileHandle = Gdx.files.internal("gestures/invertedvshape/");
        alphaFileHandle = Gdx.files.internal("gestures/alpha/");
        gammaFileHandle = Gdx.files.internal("gestures/gamma/");
        sigmaFileHandle = Gdx.files.internal("gestures/sigma/");
        mShapeFileHandle = Gdx.files.internal("gestures/mshape/");
        revCShapeFileHandle = Gdx.files.internal("gestures/revcshape/");
        triangleFileHandle = Gdx.files.internal("gestures/triangle/");

        if (sigmaFileHandle.exists()) {
            gesturesList.put("sigma", sigmaFileHandle);
        }

        if (zShapeFileHandle.exists()) {
            gesturesList.put("zshape", zShapeFileHandle);
        }

        if (invZShapeFileHandle.exists()) {
            gesturesList.put("zinv", invZShapeFileHandle);
        }

        if (horizontalFileHandle.exists()) {
            gesturesList.put("horizontal", horizontalFileHandle);
        }

        if (verticalFileHandle.exists()) {
            gesturesList.put("vertical", verticalFileHandle);
        }

        if (vShapeFileHandle.exists()) {
            gesturesList.put("vshape", vShapeFileHandle);
        }

        if (invVShapeFileHandle.exists()) {
            gesturesList.put("vinv", invVShapeFileHandle);
        }

        if (alphaFileHandle.exists()) {
            gesturesList.put("alpha", alphaFileHandle);
        }

        if (gammaFileHandle.exists()) {
            gesturesList.put("gamma", gammaFileHandle);
        }

        if (mShapeFileHandle.exists()) {
            gesturesList.put("mshape", mShapeFileHandle);
        }

        if (revCShapeFileHandle.exists()) {
            gesturesList.put("revcshape", revCShapeFileHandle);
        }

        if (triangleFileHandle.exists()) {
            gesturesList.put("triangle", triangleFileHandle);
        }

    }

    public void load() {
        loadGestureJSON();

        // store high score
        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }
        // store background option
        if (!prefs.contains("backgroundOption")) {
            prefs.putInteger("backgroundOption", 0);
        }
        // store sound settings
        if (!prefs.contains("soundToggle")) {
            prefs.putBoolean("soundToggle", true);
        }
        setSoundToggle(prefs.getBoolean("soundToggle"));

        // store music settings
        if (!prefs.contains("musicToggle")) {
            prefs.putBoolean("musicToggle", true);
        }
        setMusicToggle(prefs.getBoolean("musicToggle"));

        // splash screen logo
        textures.put("logoTexture", new Texture(Gdx.files.internal("data/logo.png")));
        textures.get("logoTexture").setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        sprites.put("logo", new TextureRegion(textures.get("logoTexture"), 0, 0, 512, 350));

        // fonts
        fonts.put("scoreText", new BitmapFont(Gdx.files.internal("data/carterOne.fnt"), true));
        fonts.get("scoreText").getData().setScale(1f, -1f);
        fonts.put("menuText", new BitmapFont(Gdx.files.internal("data/carterOne.fnt"), true));
        fonts.get("menuText").getData().setScale(0.4f, -0.4f);

        // menu icons V2
        textures.put("menuTexture", new Texture(Gdx.files.internal("data/menuIcons.png")));
        sprites.put("mainPlayBtnUp", new TextureRegion(textures.get("menuTexture"), 800, 520, 280, 160));
        sprites.put("mainPlayBtnDown", new TextureRegion(textures.get("menuTexture"), 800, 680, 280, 160));
        sprites.put("loginBtnUp", new TextureRegion(textures.get("menuTexture"), 0, 240, 200, 80));
        sprites.put("loginBtnDown", new TextureRegion(textures.get("menuTexture"), 0, 320, 200, 80));
        sprites.put("watchDemoBtnUp", new TextureRegion(textures.get("menuTexture"), 240, 480, 240, 80));
        sprites.put("watchDemoBtnDown", new TextureRegion(textures.get("menuTexture"), 0, 480, 240, 80));
        sprites.put("cancelBtnUp", new TextureRegion(textures.get("menuTexture"), 200, 400, 200, 80));
        sprites.put("cancelBtnDown", new TextureRegion(textures.get("menuTexture"), 0, 400, 200, 80));
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
        sprites.put("helpBtnUp", new TextureRegion(textures.get("menuTexture"), 320, 560, 80, 80));
        sprites.put("helpBtnDown", new TextureRegion(textures.get("menuTexture"), 400, 560, 80, 80));
        sprites.put("musicBtnUp", new TextureRegion(textures.get("menuTexture"), 320, 640, 80, 80));
        sprites.put("musicBtnDown", new TextureRegion(textures.get("menuTexture"), 400, 640, 80, 80));
        sprites.put("soundBtnUp", new TextureRegion(textures.get("menuTexture"), 320, 720, 80, 80));
        sprites.put("soundBtnDown", new TextureRegion(textures.get("menuTexture"), 400, 720, 80, 80));
        sprites.put("backBtnUp", new TextureRegion(textures.get("menuTexture"), 480, 560, 80, 80));
        sprites.put("backBtnDown", new TextureRegion(textures.get("menuTexture"), 560, 560, 80, 80));
        sprites.put("musicMutedBtnUp", new TextureRegion(textures.get("menuTexture"), 480, 640, 80, 80));
        sprites.put("musicMutedBtnDown", new TextureRegion(textures.get("menuTexture"), 560, 640, 80, 80));
        sprites.put("soundMutedBtnUp", new TextureRegion(textures.get("menuTexture"), 480, 720, 80, 80));
        sprites.put("soundMutedBtnDown", new TextureRegion(textures.get("menuTexture"), 560, 720, 80, 80));
        sprites.put("xBtnUp", new TextureRegion(textures.get("menuTexture"), 640, 560, 80, 80));
        sprites.put("xBtnDown", new TextureRegion(textures.get("menuTexture"), 720, 560, 80, 80));
        sprites.put("tickBtnUp", new TextureRegion(textures.get("menuTexture"), 640, 640, 80, 80));
        sprites.put("tickBtnDown", new TextureRegion(textures.get("menuTexture"), 720, 640, 80, 80));
        sprites.put("newMark", new TextureRegion(textures.get("menuTexture"), 200, 320, 80, 40));
        sprites.put("checkMark", new TextureRegion(textures.get("menuTexture"), 280, 320, 80, 80));
        sprites.put("background1BtnUp", new TextureRegion(textures.get("menuTexture"), 400, 320, 80, 80));
        sprites.put("background1BtnDown", new TextureRegion(textures.get("menuTexture"), 400, 400, 80, 80));
        sprites.put("background2BtnUp", new TextureRegion(textures.get("menuTexture"), 480, 320, 80, 80));
        sprites.put("background2BtnDown", new TextureRegion(textures.get("menuTexture"), 480, 400, 80, 80));
        sprites.put("background3BtnUp", new TextureRegion(textures.get("menuTexture"), 560, 320, 80, 80));
        sprites.put("background3BtnDown", new TextureRegion(textures.get("menuTexture"), 560, 400, 80, 80));
        sprites.put("pauseMenu", new TextureRegion(textures.get("menuTexture"), 0, 800, 400, 200));
        sprites.put("backgroundMenu", new TextureRegion(textures.get("menuTexture"), 400, 800, 400, 200));
        sprites.put("gameOverMenu", new TextureRegion(textures.get("menuTexture"), 200, 0, 440, 320));
        sprites.put("oopsMenu", new TextureRegion(textures.get("menuTexture"), 640, 0, 440, 320));
        sprites.put("quitMenu", new TextureRegion(textures.get("menuTexture"), 640, 320, 400, 200));
        sprites.put("helpMenu", new TextureRegion(textures.get("menuTexture"), 1080, 280, 440, 720));
        sprites.put("gameTitle", new TextureRegion(textures.get("menuTexture"), 2000-480, 1000-300, 480, 320));
        sprites.put("zombieHead", new TextureRegion(textures.get("menuTexture"), 1520, 480, 160, 160));

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
        sprites.put("invertedVshape", new TextureRegion(textures.get("menuTexture"), 1700, 500, 100, 100));
        sprites.put("alpha", new TextureRegion(textures.get("menuTexture"), 1700, 600, 100, 100));
        sprites.put("gamma", new TextureRegion(textures.get("menuTexture"), 1800, 600, 100, 100));
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
        gestureHints.put(GestureRock.GestureType.SIGMA, sprites.get("sigma"));
        gestureHints.put(GestureRock.GestureType.M_SHAPE, sprites.get("Mshape"));
        gestureHints.put(GestureRock.GestureType.REV_C_SHAPE, sprites.get("invertedCshape"));
        gestureHints.put(GestureRock.GestureType.TRIANGLE, sprites.get("triangle"));

        // sprites of 'MISS'
        sprites.put("miss1", new TextureRegion(textures.get("menuTexture"), 1520, 0, 280, 120));
        sprites.put("miss2", new TextureRegion(textures.get("menuTexture"), 1520, 120, 280, 120));
        sprites.put("miss3", new TextureRegion(textures.get("menuTexture"), 1520, 240, 280, 120));
        sprites.put("miss4", new TextureRegion(textures.get("menuTexture"), 1520, 360, 280, 120));

        // 'MISS' animation
        TextureRegion[] miss = {sprites.get("miss2"), sprites.get("miss1"), sprites.get("miss2"), sprites.get("miss3"), sprites.get("miss4")};
        missAnimation = new Animation(0.1f, miss);
        missAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        // sprites of zombie climbing
        sprites.put("zombieClimb1", new TextureRegion(textures.get("menuTexture"), 1087, 0, 60, 130));
        sprites.put("zombieClimb2", new TextureRegion(textures.get("menuTexture"), 1159, 0, 60, 130));
        sprites.put("zombieClimb3", new TextureRegion(textures.get("menuTexture"), 1231, 0, 60, 130));
        sprites.put("zombieClimb4", new TextureRegion(textures.get("menuTexture"), 1303, 0, 60, 130));
        sprites.put("zombieClimb5", new TextureRegion(textures.get("menuTexture"), 1375, 0, 60, 130));
        sprites.put("zombieClimb6", new TextureRegion(textures.get("menuTexture"), 1447, 0, 60, 130));

        // zombie climbing animation
        TextureRegion[] zombieClimbing = {sprites.get("zombieClimb1"), sprites.get("zombieClimb2"), sprites.get("zombieClimb3"),
                sprites.get("zombieClimb4"), sprites.get("zombieClimb5"), sprites.get("zombieClimb6")};
        zombieClimbingAnimation = new Animation(0.2f, zombieClimbing);
        zombieClimbingAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // sprites of zombie climbing (ENEMY)
        sprites.put("enemyZombieClimb1", new TextureRegion(textures.get("menuTexture"), 1087, 130, 60, 130));
        sprites.put("enemyZombieClimb2", new TextureRegion(textures.get("menuTexture"), 1159, 130, 60, 130));
        sprites.put("enemyZombieClimb3", new TextureRegion(textures.get("menuTexture"), 1231, 130, 60, 130));
        sprites.put("enemyZombieClimb4", new TextureRegion(textures.get("menuTexture"), 1303, 130, 60, 130));
        sprites.put("enemyZombieClimb5", new TextureRegion(textures.get("menuTexture"), 1375, 130, 60, 130));
        sprites.put("enemyZombieClimb6", new TextureRegion(textures.get("menuTexture"), 1447, 130, 60, 130));

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

        // sprites of spinner
        textures.put("spinner", new Texture(Gdx.files.internal("data/spinnerAnimation.png")));
        sprites.put("spinner1", new TextureRegion(textures.get("spinner"), 0, 0, 100, 100));
        sprites.put("spinner2", new TextureRegion(textures.get("spinner"), 100, 0, 100, 100));
        sprites.put("spinner3", new TextureRegion(textures.get("spinner"), 200, 0, 100, 100));
        sprites.put("spinner4", new TextureRegion(textures.get("spinner"), 300, 0, 100, 100));
        sprites.put("spinner5", new TextureRegion(textures.get("spinner"), 400, 0, 100, 100));
        sprites.put("spinner6", new TextureRegion(textures.get("spinner"), 500, 0, 100, 100));
        sprites.put("spinner7", new TextureRegion(textures.get("spinner"), 600, 0, 100, 100));
        sprites.put("spinner8", new TextureRegion(textures.get("spinner"), 700, 0, 100, 100));
        sprites.put("spinner9", new TextureRegion(textures.get("spinner"), 800, 0, 100, 100));
        sprites.put("spinner10", new TextureRegion(textures.get("spinner"), 900, 0, 100, 100));
        sprites.put("spinner11", new TextureRegion(textures.get("spinner"), 1000, 0, 100, 100));
        sprites.put("spinner12", new TextureRegion(textures.get("spinner"), 1100, 0, 100, 100));
        sprites.put("spinner13", new TextureRegion(textures.get("spinner"), 1200, 0, 100, 100));
        sprites.put("spinner14", new TextureRegion(textures.get("spinner"), 1300, 0, 100, 100));
        sprites.put("spinner15", new TextureRegion(textures.get("spinner"), 1400, 0, 100, 100));
        sprites.put("spinner16", new TextureRegion(textures.get("spinner"), 1500, 0, 100, 100));
        sprites.put("spinner17", new TextureRegion(textures.get("spinner"), 1600, 0, 100, 100));

        // spinner animation
        TextureRegion[] spinner = {sprites.get("spinner1"), sprites.get("spinner2"), sprites.get("spinner3"), sprites.get("spinner4"), sprites.get("spinner5"), sprites.get("spinner6"),
                sprites.get("spinner7"), sprites.get("spinner8"), sprites.get("spinner9"), sprites.get("spinner10"), sprites.get("spinner11"), sprites.get("spinner12"),
                sprites.get("spinner13"), sprites.get("spinner14"), sprites.get("spinner15"), sprites.get("spinner16"), sprites.get("spinner17")};
        spinnerAnimation = new Animation(0.05f, spinner);
        spinnerAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // background
        textures.put("backgrounds", new Texture(Gdx.files.internal("data/backgrounds.png")));
        backgrounds[0] = new TextureRegion(textures.get("backgrounds"), 0, 0, 450, 800);
        backgrounds[1] = new TextureRegion(textures.get("backgrounds"), 450, 0, 450, 800);
        backgrounds[2] = new TextureRegion(textures.get("backgrounds"), 900, 0, 450, 800);
        sprites.put("cloudsBackground", new TextureRegion(textures.get("backgrounds"), 1350, 0, 450, 800));
        sprites.put("backgroundTint", new TextureRegion(textures.get("backgrounds"), 1800, 0, 450, 800));

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

        //soundFXs
        soundFXs.put(SoundFX.EXPLODE1, Gdx.audio.newSound(Gdx.files.internal("sounds/explode1.ogg")));
        soundFXs.put(SoundFX.EXPLODE2, Gdx.audio.newSound(Gdx.files.internal("sounds/explode2.ogg")));
        soundFXs.put(SoundFX.EXPLODE3, Gdx.audio.newSound(Gdx.files.internal("sounds/explode3.ogg")));
        soundFXs.put(SoundFX.EXPLODE4, Gdx.audio.newSound(Gdx.files.internal("sounds/explode4.ogg")));
        soundFXs.put(SoundFX.ROCKCRACK1, Gdx.audio.newSound(Gdx.files.internal("sounds/rockcrack1.ogg")));
        soundFXs.put(SoundFX.ROCKCRACK2, Gdx.audio.newSound(Gdx.files.internal("sounds/rockcrack2.ogg")));
        soundFXs.put(SoundFX.ROCKCRACK3, Gdx.audio.newSound(Gdx.files.internal("sounds/rockcrack3.ogg")));
        soundFXs.put(SoundFX.ROCKCRACK4, Gdx.audio.newSound(Gdx.files.internal("sounds/rockcrack4.ogg")));
        soundFXs.put(SoundFX.UIBUTTONCLICKDOWN, Gdx.audio.newSound(Gdx.files.internal("sounds/UIButtonClickDown.ogg")));
        soundFXs.put(SoundFX.UIBUTTONUP, Gdx.audio.newSound(Gdx.files.internal("sounds/UIButtonClickUp.ogg")));

        //BGMs
        soundBGM.put(BGM.BATTLEMUSICNORMALLAYER, Gdx.audio.newMusic(Gdx.files.internal("sounds/battlemusicnormallayer.ogg")));
        soundBGM.put(BGM.BATTLEMUSICINTENSELAYER, Gdx.audio.newMusic(Gdx.files.internal("sounds/battlemusicintenselayer.ogg")));
        soundBGM.put(BGM.BATTLEMUSICNORMALLAYERLOOP, Gdx.audio.newMusic(Gdx.files.internal("sounds/battlemusicnormallayerloop.ogg")));
        soundBGM.put(BGM.BATTLEMUSICINTENSELAYERLOOP, Gdx.audio.newMusic(Gdx.files.internal("sounds/battlemusicintenselayerloop.ogg")));
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

    public static void setBackgroundOption(int option) {
        prefs.putInteger("backgroundOption", option);
        prefs.flush();
    }

    public static int getBackgroundOption() {
        return prefs.getInteger("backgroundOption");
    }

    public static void setSoundToggle(boolean toggle) {
        prefs.putBoolean("soundToggle", toggle);
        prefs.flush();
        if (toggle) {
            soundVolume = 1f;
        } else {
            soundVolume = 0f;
        }
    }

    public static boolean getSoundToggle() {
        return prefs.getBoolean("soundToggle");
    }

    public static void setMusicToggle(boolean toggle) {
        prefs.putBoolean("musicToggle", toggle);
        prefs.flush();
        if (toggle) {
            for (EnumMap.Entry<BGM, Music> entry: soundBGM.entrySet()) {
                entry.getValue().setVolume(1f);
            }
        } else {
            for (EnumMap.Entry<BGM, Music> entry: soundBGM.entrySet()) {
                entry.getValue().setVolume(0f);
            }
        }
    }

    public static boolean getMusicToggle() {
        return prefs.getBoolean("musicToggle");
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
        for (Object o : soundFXs.values().toArray()) {
            Sound temp = (Sound) o;
            soundFXs.remove(o);
            temp.dispose();
        }
    }
}
