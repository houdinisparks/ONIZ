package com.oniz.Screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.oniz.Game.AssetLoader;
import com.oniz.Game.ZGame;
import com.oniz.Gestures.ProtractorGestureRecognizer;
import com.oniz.TweenAccessors.SpriteAccessor;

import java.util.Hashtable;


public class SplashScreen implements Screen {

    private TweenManager manager;
    private SpriteBatch batcher;
    private Sprite sprite;
    private ZGame game;

    public SplashScreen(ZGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        sprite = new Sprite(AssetLoader.getInstance().sprites.get("logo"));
        sprite.setColor(1, 1, 1, 0);

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        float desiredWidth = width * .7f;
        float scale = desiredWidth / sprite.getWidth();

        sprite.setSize(sprite.getWidth() * scale, sprite.getHeight() * scale);
        sprite.setPosition((width / 2) - (sprite.getWidth() / 2), (height / 2)
                - (sprite.getHeight() / 2));
        setupTween();
        batcher = new SpriteBatch();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // do something important here, asynchronously to the rendering thread
                AssetLoader.getInstance().loadGestures();
                // post a Runnable to the rendering thread that processes the result
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        // process the result, e.g. add it to an Array<Result> field of the ApplicationListener.
                        tearDownTween();
                    }
                });
                Gdx.app.log("GESTURES LOADING", "SHOULD BE DONE NOW!");

            }
        }).start();
    }

    private void setupTween() {
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        manager = new TweenManager();


        Tween.to(sprite, SpriteAccessor.ALPHA, 3f).target(1)
                .ease(TweenEquations.easeInExpo)
                .start(manager);
    }

    private void tearDownTween() {

        TweenCallback cb = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                game.switchScreen(ZGame.ScreenState.START);
            }
        };

        Tween.to(sprite, SpriteAccessor.ALPHA, 2f).target(0.1f)
                .ease(TweenEquations.easeOutExpo)
                .setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE)
                .start(manager);

    }

    @Override
    public void render(float delta) {
        manager.update(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batcher.begin();
        sprite.draw(batcher);
        batcher.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}
