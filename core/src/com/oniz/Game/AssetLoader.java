package com.oniz.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Hashtable;

/**
 * Created by robin on 1/3/16.
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
    public Animation zombieAnimation;
    public TextureRegion background;

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
        load();
    }

    public void load() {
//        textures.put("pauseUp", new Texture(Gdx.files.internal("lineDark/lineDark09.png")));
        textures.put("pauseDown", new Texture(Gdx.files.internal("shadedDark/shadedDark14.png")));

        textures.put("pauseUp", new Texture(Gdx.files.internal("lineLight/lineLight12.png")));

        textures.put("playUp", new Texture(Gdx.files.internal("lineLight/lineLight14.png")));
//        textures.put("playUp", new Texture(Gdx.files.internal("lineDark/lineDark11.png")));
        textures.put("playDown", new Texture(Gdx.files.internal("shadedDark/shadedDark16.png")));

        textures.put("restartUp", new Texture(Gdx.files.internal("lineLight/lineLight10.png")));
        textures.put("restartDown", new Texture(Gdx.files.internal("shadedDark/shadedDark12.png")));

        // sprites of zombie climbing
        textures.put("zombies", new Texture(Gdx.files.internal("data/zombieTexture.png")));
        sprites.put("zombieClimb1", new TextureRegion(textures.get("zombies"), 302, 0, 80, 125));
        sprites.put("zombieClimb2", new TextureRegion(textures.get("zombies"), 385, 0, 80, 125));
        sprites.put("zombieClimb3", new TextureRegion(textures.get("zombies"), 468, 0, 80, 125));

        TextureRegion[] zombies = {sprites.get("zombieClimb1"), sprites.get("zombieClimb2"), sprites.get("zombieClimb3")};
        zombieAnimation = new Animation(0.6f, zombies);
        zombieAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        background = new TextureRegion(textures.get("zombies"), 0, 0, 297, 447);
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
