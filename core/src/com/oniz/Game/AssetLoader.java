package com.oniz.Game;

import com.badlogic.gdx.Gdx;
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
    public Animation zombieClimbingAnimation, enemyZombieClimbingAnimation;
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
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        load();
    }

    public void load() {

        // splash screen logo
        textures.put("logoTexture", new Texture(Gdx.files.internal("data/logo.png")));
        textures.get("logoTexture").setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        sprites.put("logo", new TextureRegion(textures.get("logoTexture"), 0, 0, 512, 114));

        // fonts
        fonts.put("badaboom", new BitmapFont(Gdx.files.internal("data/badaboom.fnt"), true));
        fonts.get("badaboom").getData().setScale(1f, -1f);

        // menu icons
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

        // gesture stages
        sprites.put("brownRock", new TextureRegion(textures.get("menuTexture"), 580, 0, 200, 200));
        sprites.put("redRock", new TextureRegion(textures.get("menuTexture"), 580, 200, 200, 200));
        sprites.put("yellowRock", new TextureRegion(textures.get("menuTexture"), 580, 400, 200, 200));
        sprites.put("greenRock", new TextureRegion(textures.get("menuTexture"), 580, 600, 200, 200));
        gestureStages.put(GestureRock.Stage.BROWN, sprites.get("brownRock"));
        gestureStages.put(GestureRock.Stage.RED, sprites.get("redRock"));
        gestureStages.put(GestureRock.Stage.YELLOW, sprites.get("yellowRock"));
        gestureStages.put(GestureRock.Stage.GREEN, sprites.get("greenRock"));

        // gesture hints
        sprites.put("horizontalLine", new TextureRegion(textures.get("menuTexture"), 780, 0, 200, 200));
        sprites.put("verticalLine", new TextureRegion(textures.get("menuTexture"), 780, 200, 200, 200));
        sprites.put("Vshape", new TextureRegion(textures.get("menuTexture"), 780, 400, 200, 200));
        sprites.put("invertedVshape", new TextureRegion(textures.get("menuTexture"), 780, 600, 200, 200));
        sprites.put("alpha", new TextureRegion(textures.get("menuTexture"), 780, 800, 200, 200));
        sprites.put("Zshape", new TextureRegion(textures.get("menuTexture"), 980, 0, 200, 200));
        sprites.put("invertedZshape", new TextureRegion(textures.get("menuTexture"), 980, 200, 200, 200));
        sprites.put("gamma", new TextureRegion(textures.get("menuTexture"), 980, 400, 200, 200));
        gestureHints.put(GestureRock.GestureType.HORIZONTAL_LINE, sprites.get("horizontalLine"));
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

        // sprites of zombie climbing (ENEMY)
        textures.put("enemyZombies", new Texture(Gdx.files.internal("data/climbAnimationEnemy.png")));
        sprites.put("enemyZombieClimb1", new TextureRegion(textures.get("enemyZombies"), 17, 24, 60, 122));
        sprites.put("enemyZombieClimb2", new TextureRegion(textures.get("enemyZombies"), 89, 24, 60, 122));
        sprites.put("enemyZombieClimb3", new TextureRegion(textures.get("enemyZombies"), 161, 24, 60, 122));
        sprites.put("enemyZombieClimb4", new TextureRegion(textures.get("enemyZombies"), 233, 24, 60, 122));
        sprites.put("enemyZombieClimb5", new TextureRegion(textures.get("enemyZombies"), 305, 24, 60, 122));
        sprites.put("enemyZombieClimb6", new TextureRegion(textures.get("enemyZombies"), 377, 24, 60, 122));

        TextureRegion[] zombieClimbing = {sprites.get("zombieClimb1"), sprites.get("zombieClimb2"), sprites.get("zombieClimb3"), sprites.get("zombieClimb4"), sprites.get("zombieClimb5"), sprites.get("zombieClimb6")};
        zombieClimbingAnimation = new Animation(0.2f, zombieClimbing);
        zombieClimbingAnimation.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] enemyZombieClimbing = {sprites.get("enemyZombieClimb1"), sprites.get("enemyZombieClimb2"), sprites.get("enemyZombieClimb3"), sprites.get("enemyZombieClimb4"), sprites.get("enemyZombieClimb5"), sprites.get("enemyZombieClimb6")};
        enemyZombieClimbingAnimation = new Animation(0.2f, enemyZombieClimbing);
        enemyZombieClimbingAnimation.setPlayMode(Animation.PlayMode.LOOP);

        textures.put("pizzaStore", new Texture(Gdx.files.internal("data/pizzaStore.png")));
        textures.put("cloudsBg", new Texture(Gdx.files.internal("data/cloudsBg.png")));
        textures.put("waitingBg", new Texture(Gdx.files.internal("data/waitingScreen.png")));
        sprites.put("background", new TextureRegion(textures.get("pizzaStore"), 338, 1, 325, 758));
        sprites.put("cloudsBackground", new TextureRegion(textures.get("cloudsBg"), 338, 1, 325, 758));
        sprites.put("waitingBackground", new TextureRegion(textures.get("waitingBg"), 338, 1, 325, 758));
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
