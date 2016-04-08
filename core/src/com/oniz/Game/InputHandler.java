package com.oniz.Game;

import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.oniz.UI.MenuButton;

import sun.rmi.runtime.Log;

/**
 * InputHandler class to handle touch events.
 */
public class InputHandler implements InputProcessor {
    private GameWorld gameWorld;

    private Hashtable<String, MenuButton> menuButtons;
    private MenuButton playButton, resumeButton, restartButton, pauseButton, pauseHomeButton, gameOverHomeButton, playAgainButton;

    private float scaleFactorX;
    private float scaleFactorY;

    /**
     * Constructor which initialize all the menu buttons and store them in a hash table.
     * @param gameWorld - shared game world
     * @param scaleFactorX - ratio of screen width to game width
     * @param scaleFactorY - ratio of screen height to game height
     */
    public InputHandler(GameWorld gameWorld, float scaleFactorX, float scaleFactorY) {
        this.gameWorld = gameWorld;

        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;

        menuButtons = new Hashtable<String, MenuButton>();

        playButton = new MenuButton(120, 320, 200, 120,
                AssetLoader.getInstance().sprites.get("mainPlayBtnUp"), AssetLoader.getInstance().sprites.get("mainPlayBtnDown"));
        resumeButton = new MenuButton(85, 390, 80, 80,
                AssetLoader.getInstance().sprites.get("resumeBtnUp"), AssetLoader.getInstance().sprites.get("resumeBtnDown"));
        restartButton = new MenuButton(185, 390, 80, 80,
                AssetLoader.getInstance().sprites.get("restartBtnUp"), AssetLoader.getInstance().sprites.get("restartBtnDown"));
        pauseButton = new MenuButton(450-80, 800-80, 80, 80,
                AssetLoader.getInstance().sprites.get("pauseBtnUp"), AssetLoader.getInstance().sprites.get("pauseBtnDown"));
        pauseHomeButton = new MenuButton(285, 390, 80, 80,
                AssetLoader.getInstance().sprites.get("menuBtnUp"), AssetLoader.getInstance().sprites.get("menuBtnDown"));
        gameOverHomeButton = new MenuButton(250, 270, 80, 80,
                AssetLoader.getInstance().sprites.get("menuBtnUp"), AssetLoader.getInstance().sprites.get("menuBtnDown"));
        playAgainButton = new MenuButton(120, 270, 80, 80,
                AssetLoader.getInstance().sprites.get("restartBtnUp"), AssetLoader.getInstance().sprites.get("restartBtnDown"));

        menuButtons.put("playButton", playButton);
        menuButtons.put("resumeButton", resumeButton);
        menuButtons.put("restartButton", restartButton);
        menuButtons.put("pauseButton", pauseButton);
        menuButtons.put("pauseHomeButton", pauseHomeButton);
        menuButtons.put("gameOverHomeButton", gameOverHomeButton);
        menuButtons.put("playAgainButton", playAgainButton);
    }

    /**
     * Handle touch-down events.
     * @param screenX - x-coordinate of touch point based on screen of device
     * @param screenY - y-coordinate of touch point based on screen of device
     * @param pointer - not used
     * @param button - not used
     * @return true
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // scale the touch coordinates to match the game screen
        screenX = scaleX(screenX);
        // change to Cartesian coordinates
        screenY = 800 - scaleY(screenY);

        if (gameWorld.isReady()) {
            playButton.isTouchDown(screenX, screenY);

        } else if (gameWorld.isRunning()) {
            pauseButton.isTouchDown(screenX, screenY);

        } else if (gameWorld.isPaused()) {
            resumeButton.isTouchDown(screenX, screenY);
            restartButton.isTouchDown(screenX, screenY);
            pauseHomeButton.isTouchDown(screenX, screenY);

        } else if (gameWorld.isGameOver()) {
            playAgainButton.isTouchDown(screenX, screenY);
            gameOverHomeButton.isTouchDown(screenX, screenY);
        }
        return false;
    }

    /**
     * Handle touch-up events.
     * @param screenX - x-coordinate of touch point based on screen of device
     * @param screenY - y-coordinate of touch point based on screen of device
     * @param pointer - not used
     * @param button - not used
     * @return true if touch-up events are handled successfully
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // scale the touch coordinates to match the game screen
        screenX = scaleX(screenX);
        // change to Cartesian coordinates
        screenY = 800 - scaleY(screenY);

        if (gameWorld.isReady()) {
            if (playButton.isTouchUp(screenX, screenY)) {
                gameWorld.setState(GameWorld.GAME_RUNNING);
                return true;
            }
        } else if (gameWorld.isRunning()) {
            if (pauseButton.isTouchUp(screenX, screenY)) {
                gameWorld.setState(GameWorld.GAME_PAUSED);
                return true;
            }
        } else if (gameWorld.isPaused()) {
            if (resumeButton.isTouchUp(screenX, screenY)) {
                gameWorld.setState(GameWorld.GAME_RUNNING);
                return true;
            } else if (restartButton.isTouchUp(screenX, screenY)) {
                gameWorld.restartGame();
                return true;
            } else if (pauseHomeButton.isTouchUp(screenX, screenY)) {
                gameWorld.goHome();
                return true;
            }
        } else if (gameWorld.isGameOver()) {
            if (playAgainButton.isTouchUp(screenX, screenY)) {
                gameWorld.restartGame();
                return true;
            } else if (gameOverHomeButton.isTouchUp(screenX, screenY)) {
                gameWorld.goHome();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    /**
     * Scale device touch coordinates to match that of the game.
     * @param screenX - original x-coordinate based on device screen
     * @return scaled x-coordinate
     */
    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    /**
     * Scale device touch coordinates to match that of the game.
     * @param screenY - original y-coordinate based on device screen
     * @return scaled y-coordinate
     */
    private int scaleY(int screenY) {
        return (int) (screenY / scaleFactorY);
    }

    /**
     * Getter method.
     * @return HashTable of MenuButtons.
     */
    public Hashtable<String, MenuButton> getMenuButtons() {
        return menuButtons;
    }
}

