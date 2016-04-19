package com.oniz.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.oniz.Game.AssetLoader;
import com.oniz.Game.GameWorld;
import com.oniz.Game.ZGame;
import com.oniz.Network.LoginListener;
import com.oniz.Sound.SoundManager;
import com.oniz.UI.AdvancedClickListener;
import com.oniz.UI.SimpleButton;


public class StartScreen implements LoginListener, Screen {

    private ZGame zGame;

    private Stage stage;
    private Skin skin;
    private TextureRegion cloudsBackground, backgroundMenu, checkMark;
    private SpriteBatch batcher;
    private OrthographicCamera cam;

    private SimpleButton quickGameBtn, loginBtn, settingsBtn, singlePlayerBtn, backBtn, helpBtn, soundBtn, soundMutedBtn, musicBtn, musicMutedBtn, background1Btn, background2Btn, background3Btn;
    private boolean backgroundMenuToggle = false;
    private boolean soundToggle = true;
    private boolean musicToggle = true;

    private SoundManager soundManager;

    public StartScreen(final ZGame zGame) {
        this.zGame = zGame;

        soundManager = SoundManager.getInstance();

        loadAssets();
        setup();
    }

    public void setup() {
        //catch back button
        Gdx.input.setCatchBackKey(true);

        //login listener
        // Viewport - Aspect Radio
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 450, 800); //false for y upwards

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

        stage = new Stage(new FitViewport(450, 800), batcher);

        // buttons
        quickGameBtn = new SimpleButton(125, 400, 200, 120, AssetLoader.getInstance().sprites.get("mainPlayBtnUp"), AssetLoader.getInstance().sprites.get("mainPlayBtnDown"));
        quickGameBtn.addListener(new AdvancedClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                quickGame();
            }
        });

        loginBtn = new SimpleButton(125, 280, 200, 80, AssetLoader.getInstance().sprites.get("loginBtnUp"), AssetLoader.getInstance().sprites.get("loginBtnDown"));
        loginBtn.addListener(new AdvancedClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                signIn();
            }
        });

        settingsBtn = new SimpleButton(10, 10, 80, 80, AssetLoader.getInstance().sprites.get("settingsBtnUp"), AssetLoader.getInstance().sprites.get("settingsBtnDown"));
        settingsBtn.addListener(new AdvancedClickListener() {


            @Override
            public void clicked(InputEvent event, float x, float y) {
                // disable startScreen buttons
                quickGameBtn.setVisible(false);
                loginBtn.setVisible(false);
                settingsBtn.setVisible(false);
                helpBtn.setVisible(false);
                singlePlayerBtn.setVisible(false);

                // enable settingScreen buttons
                backBtn.setVisible(true);
                if (soundToggle) {
                    soundBtn.setVisible(true);
                } else {
                    soundMutedBtn.setVisible(true);
                }
                if (musicToggle) {
                    musicBtn.setVisible(true);
                } else {
                    musicMutedBtn.setVisible(true);
                }
                backgroundMenuToggle = true;
                background1Btn.setVisible(true);
                background2Btn.setVisible(true);
                background3Btn.setVisible(true);
            }
        });

        singlePlayerBtn = new SimpleButton(450 - 90, 10, 80, 80, AssetLoader.getInstance().sprites.get("singlePlayerBtnUp"), AssetLoader.getInstance().sprites.get("singlePlayerBtnDown"));
        singlePlayerBtn.addListener(new AdvancedClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                zGame.setMultiplayerMode(false);
                zGame.switchScreen(ZGame.ScreenState.MAIN);
            }
        });

        backBtn = new SimpleButton(10, 800 - 90, 80, 80, AssetLoader.getInstance().sprites.get("backBtnUp"), AssetLoader.getInstance().sprites.get("backBtnDown"));
        backBtn.addListener(new AdvancedClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // disable settingScreen buttons
                backBtn.setVisible(false);
                soundBtn.setVisible(false);
                soundMutedBtn.setVisible(false);
                musicBtn.setVisible(false);
                musicMutedBtn.setVisible(false);
                backgroundMenuToggle = false;
                background1Btn.setVisible(false);
                background2Btn.setVisible(false);
                background3Btn.setVisible(false);

                // enable startScreen buttons
                quickGameBtn.setVisible(true);
                if (zGame.playServices.isSignedIn()) {
                    loginBtn.setVisible(false);
                } else {
                    loginBtn.setVisible(true);
                }
                settingsBtn.setVisible(true);
                helpBtn.setVisible(true);
                singlePlayerBtn.setVisible(true);
            }
        });

        helpBtn = new SimpleButton(100, 10, 80, 80, AssetLoader.getInstance().sprites.get("helpBtnUp"), AssetLoader.getInstance().sprites.get("helpBtnDown"));
        helpBtn.addListener(new AdvancedClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: help screen
            }
        });

        soundBtn = new SimpleButton(135, 10, 80, 80, AssetLoader.getInstance().sprites.get("soundBtnUp"), AssetLoader.getInstance().sprites.get("soundBtnDown"));
        soundBtn.addListener(new AdvancedClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundToggle = false;
                soundBtn.setVisible(false);
                soundMutedBtn.setVisible(true);
                // TODO: toggle sound
            }
        });

        soundMutedBtn = new SimpleButton(135, 10, 80, 80, AssetLoader.getInstance().sprites.get("soundMutedBtnUp"), AssetLoader.getInstance().sprites.get("soundMutedBtnDown"));
        soundMutedBtn.addListener(new AdvancedClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundToggle = true;
                soundBtn.setVisible(true);
                soundMutedBtn.setVisible(false);
                // TODO: toggle sound
            }
        });

        musicBtn = new SimpleButton(235, 10, 80, 80, AssetLoader.getInstance().sprites.get("musicBtnUp"), AssetLoader.getInstance().sprites.get("musicBtnDown"));
        musicBtn.addListener(new AdvancedClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                musicToggle = false;
                musicBtn.setVisible(false);
                musicMutedBtn.setVisible(true);
                // TODO: toggle music
            }
        });

        musicMutedBtn = new SimpleButton(235, 10, 80, 80, AssetLoader.getInstance().sprites.get("musicMutedBtnUp"), AssetLoader.getInstance().sprites.get("musicMutedBtnDown"));
        musicMutedBtn.addListener(new AdvancedClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                musicToggle = true;
                musicBtn.setVisible(true);
                musicMutedBtn.setVisible(false);
                // TODO: toggle music
            }
        });

        background1Btn = new SimpleButton(85, 390, 80, 80, AssetLoader.getInstance().sprites.get("background1BtnUp"), AssetLoader.getInstance().sprites.get("background1BtnDown"));
        background1Btn.addListener(new AdvancedClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                zGame.getGameWorld().setBackgroundOption(GameWorld.BACKGROUND1);
            }
        });

        background2Btn = new SimpleButton(185, 390, 80, 80, AssetLoader.getInstance().sprites.get("background2BtnUp"), AssetLoader.getInstance().sprites.get("background2BtnDown"));
        background2Btn.addListener(new AdvancedClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                zGame.getGameWorld().setBackgroundOption(GameWorld.BACKGROUND2);
            }
        });

        background3Btn = new SimpleButton(285, 390, 80, 80, AssetLoader.getInstance().sprites.get("background3BtnUp"), AssetLoader.getInstance().sprites.get("background3BtnDown"));
        background3Btn.addListener(new AdvancedClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                zGame.getGameWorld().setBackgroundOption(GameWorld.BACKGROUND3);
            }
        });

        stage.addActor(quickGameBtn);
        stage.addActor(loginBtn);
        stage.addActor(settingsBtn);
        stage.addActor(singlePlayerBtn);
        stage.addActor(backBtn);
        stage.addActor(helpBtn);
        stage.addActor(soundBtn);
        stage.addActor(soundMutedBtn);
        stage.addActor(musicBtn);
        stage.addActor(musicMutedBtn);
        stage.addActor(background1Btn);
        stage.addActor(background2Btn);
        stage.addActor(background3Btn);

        soundBtn.setVisible(false);
        soundMutedBtn.setVisible(false);
        musicBtn.setVisible(false);
        musicMutedBtn.setVisible(false);
        backBtn.setVisible(false);
        background1Btn.setVisible(false);
        background2Btn.setVisible(false);
        background3Btn.setVisible(false);

        // show "login" button when not signed in
        if (zGame.playServices.isSignedIn()) {
            loginBtn.setVisible(false);
        } else {
            loginBtn.setVisible(true);
        }

        Gdx.app.log("WIDTH:", Gdx.graphics.getWidth() + "");
        Gdx.app.log("HEIGHT:", Gdx.graphics.getHeight() + "");
    }

    //Load Assets
    public void loadAssets() {
        AssetLoader.getInstance();
        skin = AssetLoader.getInstance().skin;
        cloudsBackground = AssetLoader.getInstance().sprites.get("cloudsBackground");
        backgroundMenu = AssetLoader.getInstance().sprites.get("backgroundMenu");
        checkMark = AssetLoader.getInstance().sprites.get("checkMark");
    }

    // Google Services Methods
    private void signIn() {
        if (zGame.playServices.isSignedIn()) {
            loginBtn.setVisible(false);
        } else {
            if (!zGame.playServices.isConnecting())
                zGame.playServices.signIn();
        }
    }

    private void quickGame() {
        Gdx.app.log("isSignedIn?", zGame.playServices.isSignedIn() + "");
        if (zGame.playServices.isSignedIn() && !zGame.playServices.isConnecting()) {
            zGame.setMultiplayerMode(true);
            zGame.switchScreen(ZGame.ScreenState.MATCHMAKING);
            zGame.playServices.startQuickGame();
        } else {
            //oops! plz sign in!
            loginBtn.setVisible(true);

        }
    }


    @Override
    public void show() {
        Gdx.app.log("LIFECYCLE", "SHOW");
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
        if (backgroundMenuToggle) {
            batcher.draw(backgroundMenu, 25, 350, 400, 200);
        }
        batcher.end();

        stage.draw();

        batcher.begin();
        if (backgroundMenuToggle) {
            if (zGame.getGameWorld().getBackgroundOption() == GameWorld.BACKGROUND1) {
                batcher.draw(checkMark, 75, 440, 40, 40);
            } else if (zGame.getGameWorld().getBackgroundOption() == GameWorld.BACKGROUND2) {
                batcher.draw(checkMark, 175, 440, 40, 40);
            } else if (zGame.getGameWorld().getBackgroundOption() == GameWorld.BACKGROUND3) {
                batcher.draw(checkMark, 275, 440, 40, 40);
            }
        }
        batcher.end();
    }

    @Override
    public void loginStatus(String status) {
        Gdx.app.log("LOGINSTATUS", status);
        if(status.equals("success")) {
            loginBtn.setVisible(false);
        } else {
            loginBtn.setVisible(true);
        }
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
        AssetLoader.getInstance().dispose();
        Gdx.app.log("LIFECYCLE", "DISPOSE");
    }
}
