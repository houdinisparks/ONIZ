package com.oniz.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.oniz.Game.AssetLoader;
import com.oniz.Game.GameWorld;
import com.oniz.Game.ZGame;
import com.oniz.Network.PlayEventListener;
import com.oniz.UI.SimpleButton;


public class MatchMakingScreen implements Screen, PlayEventListener {

    public static ZGame zgame;

    private Stage stage;

    private SimpleButton cancelBtn;

    private float runTime;
    private TextureRegion background, backgroundTint;
    private BitmapFont menuFont;
    private Animation spinnerAnimation;

    private SpriteBatch batcher;
    private OrthographicCamera cam;

    private boolean isCancellable = false;

    public MatchMakingScreen(ZGame zgame) {
        this.zgame = zgame;
        this.stage = new Stage(new FitViewport(450, 800));

        background = AssetLoader.getInstance().backgrounds[GameWorld.BACKGROUND1];
        backgroundTint = AssetLoader.getInstance().sprites.get("backgroundTint");
        menuFont = AssetLoader.getInstance().fonts.get("menuText");
        spinnerAnimation = AssetLoader.getInstance().spinnerAnimation;

        stage = new Stage(new FitViewport(450, 800));

        //Viewport - Aspect Radio
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 450, 800); //false for y upwards

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

        cancelBtn = new SimpleButton(125, 250, 200, 80, AssetLoader.getInstance().sprites.get("cancelBtnUp"), AssetLoader.getInstance().sprites.get("cancelBtnDown"));
        cancelBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cancelMatchmaking();
            }
        });
        cancelBtn.setVisible(false);
        stage.addActor(cancelBtn);

    }

    public void setBackground(int backgroundOption) {
        this.background = AssetLoader.getInstance().backgrounds[backgroundOption];
    }

    //cancel matchmaking process
    private void cancelMatchmaking() {
        zgame.playServices.leaveGame();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        //need to reset this when we switch back to this screen
        isCancellable = false;
        cancelBtn.setVisible(false);
    }

    @Override
    public void render(float delta) {
        runTime += delta;
        Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batcher.begin();
        batcher.disableBlending();
        // draw the background
        batcher.draw(background, 0, 0, 450, 800);

        batcher.enableBlending();
        // draw the background tint
        batcher.draw(backgroundTint, 0, 0, 450, 800);
        // draw text
        menuFont.draw(batcher, "Matchmaking", 122, 400);
        // draw spinner animation
        batcher.draw(spinnerAnimation.getKeyFrame(runTime), 175, 450, 100, 100);

        batcher.end();


        //show cancel when room is created
        if (!isCancellable) {
            if (zgame.playServices.canLeaveRoom()) {
                isCancellable = true;
                cancelBtn.setVisible(true);
            }
        }

        stage.draw();
    }

    @Override
    public void leftRoom() {
        Gdx.app.log("Left room", "Match making screen left room");
        zgame.switchScreen(ZGame.ScreenState.START);
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
        Gdx.app.log("DIPOSE", "MatchMakingScreenDisposed!");
    }
}
