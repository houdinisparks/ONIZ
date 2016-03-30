package com.oniz.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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
import com.oniz.UI.SimpleButton;

/**
 * Created by yanyee on 3/18/2016.
 */
public class StartScreen implements Screen {


    public static ZGame zGame;

    private Stage stage;
    private Skin skin;

    private TextButton signInOutBtn;

    public StartScreen(final ZGame zGame) {
        this.zGame = zGame;

        skin = AssetLoader.getInstance().skin;
        stage = new Stage(new FitViewport(450, 800));

        final TextButton button = new TextButton("Quick Game", skin, "default");

        button.setWidth(200);
        button.setHeight(50);
        button.setPosition(100, 600);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                quickGame();
            }
        });

        signInOutBtn = new TextButton("Sign In", skin, "default");
        if(zGame.playServices.isSignedIn()) {
            signInOutBtn.setText("Sign Out");
        }
        signInOutBtn.setWidth(200);
        signInOutBtn.setHeight(50);
        signInOutBtn.setPosition(100, 500);

        signInOutBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                signInOut();
            }
        });

        final TextButton sendMessageBtn = new TextButton("Send message", skin, "default");
        sendMessageBtn.setWidth(200);
        sendMessageBtn.setHeight(50);
        sendMessageBtn.setPosition(100, 400);

        sendMessageBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sendMessage();
            }
        });

        final TextButton singlePlayer = new TextButton("Single Player", skin, "default");
        singlePlayer.setWidth(200);
        singlePlayer.setHeight(50);
        singlePlayer.setPosition(100, 300);

        singlePlayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                zGame.switchScreen(ZGame.ScreenState.MAIN);
            }
        });

        stage.addActor(signInOutBtn);
        stage.addActor(button);
        stage.addActor(sendMessageBtn);
        stage.addActor(singlePlayer);

        Gdx.app.log("WIDTH:", Gdx.graphics.getWidth() + "");
        Gdx.app.log("HEIGHT:", Gdx.graphics.getHeight() + "");

    }

    //Google Services Methods
    private void signInOut() {
        if(zGame.playServices.isSignedIn()) {
            zGame.playServices.signOut();
            signInOutBtn.setText("Sign In");
        } else {
            zGame.playServices.signIn();
            signInOutBtn.setText("Sign Out");
        }
    }

    private void quickGame() {
        Gdx.app.log("isSIGNEDin?", zGame.playServices.isSignedIn()+"");
        if(zGame.playServices.isSignedIn()) {
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
        stage.draw();
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
