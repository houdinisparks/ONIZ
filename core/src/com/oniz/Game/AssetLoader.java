package com.oniz.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
    public ArrayList<TextureRegion> gestureHints;
    public Animation zombieClimbingAnimation;

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
        gestureHints = new ArrayList<TextureRegion>();
        load();
    }

    public void load() {

        // splash screen logo
        textures.put("logoTexture", new Texture(Gdx.files.internal("data/logo.png")));
        textures.get("logoTexture").setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        sprites.put("logo", new TextureRegion(textures.get("logoTexture"), 0, 0, 512, 114));

        // sprites from ZombieBird
        textures.put("menuTexture", new Texture(Gdx.files.internal("data/zombieBirdTexture.png")));
        sprites.put("ready", new TextureRegion(textures.get("menuTexture"), 59, 83, 34, 7));
        sprites.put("retry", new TextureRegion(textures.get("menuTexture"), 59, 110, 33, 7));
        sprites.put("gameOver", new TextureRegion(textures.get("menuTexture"), 59, 92, 46, 7));


        // play, pause, restart buttons
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

        // gesture hints
        textures.put("gestureHints", new Texture(Gdx.files.internal("data/gestureHints.png")));
        sprites.put("circleGestureHint", new TextureRegion(textures.get("gestureHints"), 0, 0, 15, 15));
        sprites.put("squareGestureHint", new TextureRegion(textures.get("gestureHints"), 15, 0, 15, 15));
        sprites.put("verticalGestureHint", new TextureRegion(textures.get("gestureHints"), 30, 0, 15, 15));
        sprites.put("horizontalGestureHint", new TextureRegion(textures.get("gestureHints"), 45, 0, 15, 15));
        gestureHints.add(sprites.get("circleGestureHint"));
        gestureHints.add(sprites.get("squareGestureHint"));
        gestureHints.add(sprites.get("verticalGestureHint"));
        gestureHints.add(sprites.get("horizontalGestureHint"));

        // sprites of zombie climbing
        textures.put("zombies", new Texture(Gdx.files.internal("data/climbAnimation.png")));
        sprites.put("zombieClimb1", new TextureRegion(textures.get("zombies"), 17, 24, 60, 122));
        sprites.put("zombieClimb2", new TextureRegion(textures.get("zombies"), 89, 24, 60, 122));
        sprites.put("zombieClimb3", new TextureRegion(textures.get("zombies"), 161, 24, 60, 122));
        sprites.put("zombieClimb4", new TextureRegion(textures.get("zombies"), 233, 24, 60, 122));
        sprites.put("zombieClimb5", new TextureRegion(textures.get("zombies"), 305, 24, 60, 122));
        sprites.put("zombieClimb6", new TextureRegion(textures.get("zombies"), 377, 24, 60, 122));

        TextureRegion[] zombieClimbing = {sprites.get("zombieClimb1"), sprites.get("zombieClimb2"), sprites.get("zombieClimb3"), sprites.get("zombieClimb4"), sprites.get("zombieClimb5"), sprites.get("zombieClimb6")};
        zombieClimbingAnimation = new Animation(0.2f, zombieClimbing);
        zombieClimbingAnimation.setPlayMode(Animation.PlayMode.LOOP);

        textures.put("pizzaStore", new Texture(Gdx.files.internal("data/pizzaStore.png")));
        sprites.put("background", new TextureRegion(textures.get("pizzaStore"), 338, 1, 325, 758));
    }

    public void dispose() {
        for(Object o : textures.values().toArray()) {
            Texture temp = (Texture) o;
            textures.remove(o);
            temp.dispose();
        }
        for(Object o : fonts.values().toArray()) {
            BitmapFont temp = (BitmapFont) o;
            fonts.remove(o);
            temp.dispose();
        }
        for(Object o : sounds.values().toArray()) {
            Sound temp = (Sound) o;
            sounds.remove(o);
            temp.dispose();
        }
    }
}
