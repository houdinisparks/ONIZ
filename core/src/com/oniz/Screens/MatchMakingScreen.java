package com.oniz.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.oniz.Game.AssetLoader;
import com.oniz.Game.ZGame;
import com.oniz.UI.SimpleButton;

/**
 * Created by robin on 19/3/16.
 */
public class MatchMakingScreen implements Screen{

    public static ZGame zgame;

    private Stage stage;
    private Skin skin;
    private Table table;

    private SimpleButton buttonB;

    public MatchMakingScreen(ZGame zgame) {
        this.zgame = zgame;
        this.stage = new Stage(new FitViewport(450, 800));
        this.skin = AssetLoader.getInstance().skin;

//        buttonB = new SimpleButton(new Image(AssetLoader.getInstance().textures.get("buttonBUp")).getDrawable(), new Image(AssetLoader.getInstance().textures.get("buttonBDown")).getDrawable());

        Label waitingLbl = new Label("Waiting for players...", skin);
        waitingLbl.setPosition(100, 400);

        table = new Table();
        table.setFillParent(true);
        table.setDebug(true);

        table.pad(5);

//        table.add(buttonB).top().right().fillX();
        table.row();
        table.add(waitingLbl).center().fill();

        //TODO: enable cancel?

        stage.addActor(table);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        stage.draw();
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
