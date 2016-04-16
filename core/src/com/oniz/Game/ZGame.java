package com.oniz.Game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.oniz.Network.LoginListener;
import com.oniz.Network.PlayServices;
import com.oniz.Screens.GameScreen;
import com.oniz.Screens.MainScreen;
import com.oniz.Screens.MatchMakingScreen;
import com.oniz.Screens.StartScreen;
import com.oniz.Screens.SplashScreen;


public class ZGame extends Game {
    AssetLoader assets;

    public PlayServices playServices;

    private Screen startScreen, gameScreen = null;
    private MatchMakingScreen matchMakingScreen;

    private GameWorld gameWorld;

    private boolean multiplayerMode = false;

    private boolean opponentDefeated = false;

    public ZGame(PlayServices playServices) {
        super();
        this.playServices = playServices;
        this.playServices.setGame(this);
    }

    public enum State
    {
        PAUSE,
        RUN,
        RESUME,
        STOPPED
    }


    public enum ScreenState {
        START, MATCHMAKING, MAIN, GAMEOVER, SPLASH
    }
    @Override
    public void create() {
        Gdx.app.log("ONIZ", "created");
        assets = AssetLoader.getInstance();
        startScreen = new StartScreen(this);
        this.playServices.addLoginListener((LoginListener) startScreen);
        matchMakingScreen = new MatchMakingScreen(this);

        //splash seems to only activate if its passed as a new object
//        setScreen(startScreen);
        setScreen(new SplashScreen(this));
    }

    public void switchScreen(ScreenState screen) {
        switch(screen) {
            case START:
                setScreen(startScreen);
                break;
            case MATCHMAKING:
                setScreen(matchMakingScreen);
                break;
            case MAIN:
                if(gameScreen == null) {
                    gameScreen = new GameScreen(this);
                    setScreen(gameScreen);
                } else {
                    setScreen(gameScreen);
                    gameWorld.restartGame();
                }
                break;
            case GAMEOVER:
                setScreen(startScreen);
                break;
            default:
                setScreen(startScreen);
                break;
        }
    }

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        gameWorld.setGame(this);
    }

    public GameWorld getGameWorld() {
        return this.gameWorld;
    }

    public MatchMakingScreen getMatchMakingScreen() {
        return matchMakingScreen;
    }

    public boolean isGameWorldReady() {
        return gameWorld != null;
    }

    public void setMultiplayerMode(boolean value) {
        this.multiplayerMode = value;
    }

    public boolean isMultiplayerMode() {
        return this.multiplayerMode;
    }

    public void setOpponentDefeated(boolean value) {
        this.opponentDefeated = value;
    }

    public boolean isOpponentDefeated() {
        return this.opponentDefeated;
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log("LIFECYCLE0", "RESIZE");
    }

    @Override
    public void pause() {
        Gdx.app.log("LIFECYCLE0", "PAUSE");
    }

    @Override
    public void resume() {
        Gdx.app.log("LIFECYCLE0", "RESUME");
    }

    @Override
    public void dispose() {
        Gdx.app.log("LIFECYCLE0", "DISPOSE");
        assets.dispose();
        super.dispose();
//        assets.dispose();
    }

}