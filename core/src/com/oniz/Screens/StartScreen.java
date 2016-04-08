package com.oniz.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.oniz.Game.AssetLoader;
import com.oniz.Game.ZGame;
import com.oniz.UI.MenuButton;
import com.oniz.UI.SimpleButton;


public class StartScreen implements Screen {

    public static ZGame zGame;

    private Stage stage;
    private Skin skin;
    private TextureRegion cloudsBackground;
    private SpriteBatch batcher;
    private OrthographicCamera cam;

    private SimpleButton quickGameBtn, loginBtn, logoutBtn, settingsBtn, singlePlayerBtn;

    public StartScreen(final ZGame zGame) {
        this.zGame = zGame;

        skin = AssetLoader.getInstance().skin;
        cloudsBackground =  AssetLoader.getInstance().sprites.get("cloudsBackground");
        stage = new Stage(new FitViewport(450, 800));

        // Viewport - Aspect Radio
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 450, 800); //false for y upwards

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

        // buttons
        quickGameBtn = new SimpleButton(125, 400, 200, 120, AssetLoader.getInstance().sprites.get("mainPlayBtnUp"), AssetLoader.getInstance().sprites.get("mainPlayBtnDown"));
        quickGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                quickGame();
            }
        });

        loginBtn = new SimpleButton(125, 280, 200, 80, AssetLoader.getInstance().sprites.get("loginBtnUp"), AssetLoader.getInstance().sprites.get("loginBtnDown"));
        loginBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                signInOut();
            }
        });

        logoutBtn = new SimpleButton(125, 280, 200, 80, AssetLoader.getInstance().sprites.get("logoutBtnUp"), AssetLoader.getInstance().sprites.get("logoutBtnDown"));
        logoutBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                signInOut();
            }
        });

        settingsBtn = new SimpleButton(10, 10, 80, 80, AssetLoader.getInstance().sprites.get("settingsBtnUp"), AssetLoader.getInstance().sprites.get("settingsBtnDown"));
        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sendMessage();
            }
        });

        singlePlayerBtn = new SimpleButton(450-90, 10, 80, 80, AssetLoader.getInstance().sprites.get("singlePlayerBtnUp"), AssetLoader.getInstance().sprites.get("singlePlayerBtnDown"));
        singlePlayerBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                zGame.setMultiplayerMode(false);
                zGame.switchScreen(ZGame.ScreenState.MAIN);
            }
        });

        stage.addActor(quickGameBtn);
        stage.addActor(loginBtn);
        stage.addActor(logoutBtn);
        stage.addActor(settingsBtn);
        stage.addActor(singlePlayerBtn);

        // show "login" button when not signed in; show "logout" button when signed in
        if (zGame.playServices.isSignedIn()) {
            loginBtn.setVisible(false);
            logoutBtn.setVisible(true);
        } else {
            loginBtn.setVisible(true);
            logoutBtn.setVisible(false);
        }

        Gdx.app.log("WIDTH:", Gdx.graphics.getWidth() + "");
        Gdx.app.log("HEIGHT:", Gdx.graphics.getHeight() + "");

    }

    // Google Services Methods
    private void signInOut() {
        if(zGame.playServices.isSignedIn()) {
            zGame.playServices.signOut();
            loginBtn.setVisible(true);
            logoutBtn.setVisible(false);
        } else {
            zGame.playServices.signIn();
            loginBtn.setVisible(false);
            logoutBtn.setVisible(true);
        }
    }

    private void quickGame() {
        Gdx.app.log("isSignedIn?", zGame.playServices.isSignedIn()+"");
        if(zGame.playServices.isSignedIn()) {
            zGame.setMultiplayerMode(true);
            zGame.switchScreen(ZGame.ScreenState.MATCHMAKING);
            zGame.playServices.startQuickGame();
        }
    }

    private void sendMessage() {
        if(zGame.playServices.isSignedIn())
            zGame.playServices.broadcastMessage("hello");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batcher.begin();
        batcher.disableBlending();

        // draw the background
        batcher.draw(cloudsBackground, 0, 0, 450, 800);

        batcher.enableBlending();
        stage.draw();
        batcher.end();
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log("LIFECYCLE", "RESIZE");
    }

    @Override
    public void pause() {
        Gdx.app.log("LIFECYCLE", "PAUSE");
    }

    @Override
    public void resume() {
        Gdx.app.log("LIFECYCLE", "RESUME");
    }

    @Override
    public void hide() {
        Gdx.app.log("LIFECYCLE", "HIDE");
    }

    @Override
    public void dispose() {
        Gdx.app.log("LIFECYCLE", "DISPOSE");
    }
}
