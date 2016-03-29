package com.oniz.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.oniz.Game.GameRenderer;
import com.oniz.Game.GameWorld;
import com.oniz.Game.InputHandler;
import com.oniz.Game.ZGame;
import com.oniz.Gestures.GestureRecognizerInputProcessor;
/**
 * GameScreen class which instantiates and coordinates GameWorld and GameRenderer.
 */
public class GameScreen implements Screen {

    private GameWorld gameWorld;
    private GameRenderer gameRenderer;
    private float runTime;

    private ZGame zgame;

    private InputMultiplexer inputMultiplexer;

    public GameScreen(ZGame zgame) {
        this.zgame = zgame;
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float gameWidth = 450;
        float gameHeight = 800;

        gameWorld = new GameWorld();

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new InputHandler(gameWorld, screenWidth / gameWidth, screenHeight / gameHeight));
        inputMultiplexer.addProcessor(new GestureRecognizerInputProcessor(gameWorld,screenWidth / gameWidth, screenHeight / gameHeight));
        Gdx.input.setInputProcessor(inputMultiplexer);

        gameRenderer = new GameRenderer(gameWorld , inputMultiplexer);
        gameWorld.setRenderer(gameRenderer);
        this.setGameWorld();
    }


    private void setGameWorld() {
        this.zgame.setGameWorld(this.gameWorld);
    }

    @Override
    public void render(float delta) {
        runTime += delta;
        gameWorld.update(delta);
        gameRenderer.render(runTime);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
