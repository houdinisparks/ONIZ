package com.oniz.Gestures;

import java.util.ArrayList;
import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Array;

import com.oniz.Game.GameWorld;
import com.oniz.Gestures.DrawPathGraphics.FixedList;
import com.oniz.Gestures.DrawPathGraphics.SwipeResolver;
import com.oniz.Gestures.DrawPathGraphics.simplify.ResolverRadialChaikin;
import com.oniz.Gestures.MatchingGesture;
import com.oniz.Mobs.ChildZombie;
import com.oniz.Mobs.ChildZombie.GestureType;
import com.oniz.UI.MenuButton;

public class GestureRecognizerInputProcessor extends InputAdapter {

    private final String TAG = "INPUT_PROCESSOR";

    private static final int GAME_HEIGHT = 800;
    
    private ProtractorGestureRecognizer recognizer;
    private ArrayList<Vector2> originalPath;

    private FixedList<Vector2> inputPoints;

    /**
     * The pointer associated with this swipe event
     */
    private int inputPointer = 0;

    /**
     * The minimum distance between the first and second point in a drawn line.
     */
    public int initialDistance = 10;

    /**
     * The minimum ditance between two points in a drawn line (starting at the second point)
     */
    public int minDistance = 20;

    private Vector2 lastPoint = new Vector2();

    private boolean isDrawing = false;

    private SwipeResolver simplifier = new ResolverRadialChaikin();
    private Array<Vector2> simplified;
    private GameWorld gameWorld;

    public GestureRecognizerInputProcessor(GameWorld gameWorld , float scaleFactorX, float scaleFactorY) {
        super();
        this.gameWorld = gameWorld;

		/*-----------Gesture Detection------------*/
        recognizer = new ProtractorGestureRecognizer();

        recognizer.addGestureFromFile(Gdx.files.internal("gestures/rectangle.json"));
        recognizer.addGestureFromFile(Gdx.files.internal("gestures/triangle.json"));
        recognizer.addGestureFromFile(Gdx.files.internal("gestures/x.json"));

        originalPath = new ArrayList<Vector2>();

		/*-----------Gesture Path Display------------*/
        int maxInputPoints = 100;
        this.inputPoints = new FixedList<Vector2>(maxInputPoints, Vector2.class);
        simplified = new Array<Vector2>(true, maxInputPoints, Vector.class);
        resolve();
    }

    /**
     * Returns the fixed list of input points (not simplified).
     *
     * @return the list of input points
     */
    public Array<Vector2> input() {
        return this.inputPoints;
    }

    /**
     * Returns the simplified list of points representing this swipe.
     *
     * @return
     */
    public Array<Vector2> path() {
        return simplified;
    }

    /**
     * If the points are dirty, the line is simplified.
     */

    public void resolve() {
        simplifier.resolve(inputPoints, simplified);
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        Gdx.app.log(TAG, "touchDown x: " + x + " y: " + y);
        /*-------Gesture Detection-----------*/
        originalPath.add(new Vector2(x, y));

		/*-------Gesture Path Display---------*/
        if (pointer != inputPointer)
            return false;
        isDrawing = true;

        //clear points
        inputPoints.clear();

        //starting point
        lastPoint = new Vector2(x, GAME_HEIGHT - y);
        inputPoints.insert(lastPoint);

        resolve();
        return false; //return false
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        Gdx.app.log(TAG, "touchDragged x: " + x + " y: " + y);
        /*-------Gesture Detection-----------*/
        originalPath.add(new Vector2(x, y));

        /*-------Gesture Path Display---------*/

        if (pointer != inputPointer)
            return false;
        isDrawing = true;

        Vector2 v = new Vector2(x, GAME_HEIGHT - y);

        //calc length
        float dx = v.x - lastPoint.x;
        float dy = v.y - lastPoint.y;
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        //TODO: use minDistanceSq

        //if we are under required distance
        if (len < minDistance && (inputPoints.size > 1 || len < initialDistance))
            return false;

        //add new point
        inputPoints.insert(v);

        lastPoint = v;

        //simplify our new line
        resolve();
        return true; //return false
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        Gdx.app.log(TAG, "touchUp x: " + x + " y: " + y);

        /*-------Gesture Detection-----------*/

        if (originalPath.size() >= 10) {
            originalPath.add(new Vector2(x, y));
            MatchingGesture match = recognizer.Recognize(originalPath);

            if (match.getScore() < 2) {
                Gdx.app.log("Gesture Name/Score", "none matched. " + match.getScore());
            } else {
                Gdx.app.log("Gesture Name/Score", match.getGesture().getName()
                        + Float.toString(match.getScore()));

                gameWorld.killZombie(convertToGestureType(match.getGesture().getName()));
            }


        }
        originalPath.clear();

        /*-------Gesture Path Display---------*/
        resolve();
        //inputPoints.clear();
        isDrawing = false;

        return false;
    }

    private GestureType convertToGestureType(String name) {

        if (name.equals("rectangle")) {
            return GestureType.SQUARE;
        }

        else if (name.equals("trianle")) {
            return GestureType.HORIZONTALLINE;
        }

        else {
            return null;
        }

//        switch (name) {
//            case "rectangle":
//                return GestureType.SQUARE;
//            case "triangle":
//                return GestureType.HORIZONTALLINE;
//
//            default:
//                return null;
//        }
    }


//    /**
//     * Scale device touch coordinates to match that of the game.
//     * @param screenX - original x-coordinate based on device screen
//     * @return scaled x-coordinate
//     */
//    private int scaleX(int screenX) {
//        return (int) (screenX / scaleFactorX);
//    }
//
//    /**
//     * Scale device touch coordinates to match that of the game.
//     * @param screenY - original y-coordinate based on device screen
//     * @return scaled y-coordinate
//     */
//    private int scaleY(int screenY) {
//        return (int) (screenY / scaleFactorY);
//    }
//
//    /**
//     * Getter method.
//     * @return HashTable of MenuButtons.
//     */
//    public Hashtable<String, MenuButton> getMenuButtons() {
//        return menuButtons;
//    }

}
