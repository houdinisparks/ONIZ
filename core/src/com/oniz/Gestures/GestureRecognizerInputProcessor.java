package com.oniz.Gestures;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import com.oniz.Game.GameWorld;
import com.oniz.Gestures.DrawPathGraphics.FixedList;
import com.oniz.Gestures.DrawPathGraphics.SwipeResolver;
import com.oniz.Gestures.DrawPathGraphics.simplify.ResolverRadialChaikin;
import com.oniz.Mobs.GestureRock;

public class GestureRecognizerInputProcessor extends InputAdapter {

    private float scaleFactorX;
    private float scaleFactorY;

    private final String TAG = "INPUT_PROCESSOR";

    private static final int GAME_HEIGHT = 800;

    private ProtractorGestureRecognizer protractorGestureRecognizer;

   // private BitmapGestureRecogniser protractorGestureRecognizer;

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
    public int minDistance = 5;

    private Vector2 lastPoint = new Vector2();

    private boolean isDrawing = false;

    private SwipeResolver simplifier = new ResolverRadialChaikin();
    private Array<Vector2> simplified;
    private GameWorld gameWorld;

    /*
    For management of adding JSON files
     */
    private static final String ZShapeType = "zshape";
    private static final String InvertedZShapeType = "invertedzshape";
    private static final String HorizontalLine = "horizontalline";
    private static final String VerticalLine = "verticalline";
    private static final String VShapeType = "vshape";
    private static final String InvertedVShapeType = "invertedvshape";
    private static final String AlphaType = "alpha";
    private static final String GammaType = "gamma";
    private static final String SigmaType = "sigma";

    private static int SigmaFileCount = 1;
    private static int ZShapeFileCount = 1;
    private static int InvertedZShapeFileCount = 1;
    private static int HorizontalLineFileCount = 1;
    private static int VerticalLineFileCount = 1;
    private static int VShapeTypeFileCount = 1;
    private static int InvertedVShapeFileCount = 1;
    private static int AlphaFileCount = 1;
    private static int GammaFileCount = 1;

    public GestureRecognizerInputProcessor(GameWorld gameWorld, float scaleFactorX, float scaleFactorY) {
        super();
        this.gameWorld = gameWorld;


        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;

		/*-----------Gesture Detection------------*/
        protractorGestureRecognizer = new ProtractorGestureRecognizer();
        //protractorGestureRecognizer = new BitmapGestureRecogniser();

        //Add all Json files as Gestures automatically
        FileHandle zShapeFileHandle;
        FileHandle invZShapeFileHandle;
        FileHandle horizontalFileHandle;
        FileHandle verticalFileHandle;
        FileHandle vShapeFileHandle;
        FileHandle invVShapeFileHandle;
        FileHandle alphaFileHandle;
        FileHandle gammaFileHandle;
        FileHandle sigmaFileHandle;

        boolean noMoreFilesToAdd = false;

        while (!noMoreFilesToAdd) {

            noMoreFilesToAdd = true;

            zShapeFileHandle = Gdx.files.internal("gestures/zshape/" + ZShapeType + ZShapeFileCount + ".json");
            invZShapeFileHandle = Gdx.files.internal("gestures/invertedzshape/" + InvertedZShapeType + InvertedZShapeFileCount + ".json");
            horizontalFileHandle = Gdx.files.internal("gestures/horizontalline/" + HorizontalLine + HorizontalLineFileCount + ".json");
            verticalFileHandle = Gdx.files.internal("gestures/verticalline/" + VerticalLine + VerticalLineFileCount + ".json");
            vShapeFileHandle = Gdx.files.internal("gestures/vshape/" + VShapeType + VShapeTypeFileCount + ".json");
            invVShapeFileHandle = Gdx.files.internal("gestures/invertedvshape/" + InvertedVShapeType + InvertedVShapeFileCount + ".json");
            alphaFileHandle = Gdx.files.internal("gestures/alpha/" + AlphaType + AlphaFileCount + ".json");
            gammaFileHandle = Gdx.files.internal("gestures/gamma/" + GammaType + GammaFileCount + ".json");
            sigmaFileHandle = Gdx.files.internal("gestures/sigma/" + SigmaType + SigmaFileCount + ".json");

            if (sigmaFileHandle.exists()) {
                SigmaFileCount += 1;
                protractorGestureRecognizer.addGestureFromFile(sigmaFileHandle);
                noMoreFilesToAdd = false;

            }

            if (zShapeFileHandle.exists()) {
                ZShapeFileCount += 1;
                protractorGestureRecognizer.addGestureFromFile(zShapeFileHandle);
                noMoreFilesToAdd = false;

            }

            if (invZShapeFileHandle.exists()) {
                protractorGestureRecognizer.addGestureFromFile(invZShapeFileHandle);
                InvertedZShapeFileCount += 1;
                noMoreFilesToAdd = false;
            }

            if (horizontalFileHandle.exists()) {
                protractorGestureRecognizer.addGestureFromFile(horizontalFileHandle);
                HorizontalLineFileCount += 1;
                noMoreFilesToAdd = false;

            }

            if (verticalFileHandle.exists()) {
                protractorGestureRecognizer.addGestureFromFile(verticalFileHandle);
                VerticalLineFileCount += 1;
                noMoreFilesToAdd = false;
            }

            if (vShapeFileHandle.exists()) {
                protractorGestureRecognizer.addGestureFromFile(vShapeFileHandle);
                VShapeTypeFileCount += 1;
                noMoreFilesToAdd = false;
            }

            if (invVShapeFileHandle.exists()) {
                protractorGestureRecognizer.addGestureFromFile(invVShapeFileHandle);
                InvertedVShapeFileCount += 1;
                noMoreFilesToAdd = false;
            }

            if (alphaFileHandle.exists()) {
                protractorGestureRecognizer.addGestureFromFile(alphaFileHandle);
                AlphaFileCount += 1;
                noMoreFilesToAdd = false;
            }

            if (gammaFileHandle.exists()) {
                protractorGestureRecognizer.addGestureFromFile(gammaFileHandle);
                GammaFileCount += 1;
                noMoreFilesToAdd = false;
            }

            if (noMoreFilesToAdd) {
                break;
            }

        }


        //--For

//        //--For Inverted Z Shape Files//
//        while (true) {
//            fileHandle = Gdx.files.internal("gestures/" + InvertedZShapeType + InvertedZShapeFileCount + ".json");
//            if (fileHandle.exists()) {
//                protractorGestureRecognizer.addGestureFromFile(fileHandle);
//                InvertedZShapeFileCount += 1;
//            } else {
//                break;
//            }
//        }

        Gdx.app.log("Gesture:" + ZShapeType, ZShapeFileCount + "");
        Gdx.app.log("Gesture:" +InvertedZShapeType, InvertedZShapeFileCount + "");
        Gdx.app.log("Gesture:" +VShapeType, VShapeTypeFileCount + "");
        Gdx.app.log("Gesture:" +InvertedVShapeType, InvertedVShapeFileCount + "");
        Gdx.app.log("Gesture:" +GammaType, GammaFileCount + "");
        Gdx.app.log("Gesture:" +AlphaType, AlphaFileCount + "");
        Gdx.app.log("Gesture:" +VerticalLine, VerticalLineFileCount + "");
        Gdx.app.log("Gesture:" +HorizontalLine, HorizontalLineFileCount + "");

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
//        Gdx.app.log(TAG, "touchDown x: " + x + " y: " + y);
        /*-------Gesture Detection-----------*/
        originalPath.add(new Vector2(x, y));

		/*-------Gesture Path Display---------*/
        if (pointer != inputPointer)
            return false;
        isDrawing = true;

        //clear points
        inputPoints.clear();

        //starting point
        lastPoint = new Vector2(scaleX(x), GAME_HEIGHT - scaleY(y));
        inputPoints.insert(lastPoint);

        resolve();
        return false; //return false
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
//        Gdx.app.log(TAG, "touchDragged x: " + x + " y: " + y);
        /*-------Gesture Detection-----------*/
        originalPath.add(new Vector2(x, y));

        /*-------Gesture Path Display---------*/

        if (pointer != inputPointer)
            return false;
        isDrawing = true;

        Vector2 v = new Vector2(scaleX(x), GAME_HEIGHT - scaleY(y));

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
//        Gdx.app.log(TAG, "touchUp x: " + x + " y: " + y);

        /*-------Gesture Detection-----------*/


        Gdx.app.log("Gesture Point Count", "Count is: " + originalPath.size());

        if (originalPath.size() >= 8) {
            originalPath.add(new Vector2(x, y));
            MatchingGesture match = protractorGestureRecognizer.Recognize(originalPath);

            if (match.getScore() < 3) {
                Gdx.app.log("Gesture Name/Score", "none matched. " + match.getScore());
            } else {
                Gdx.app.log("Gesture Name/Score", match.getGesture().getName()
                        + Double.toString(match.getScore()));
                gameWorld.weakenZombie(convertToGestureType(match.getGesture().getName()));
            }


        }
        originalPath.clear();

        /*-------Gesture Path Display---------*/
        resolve();
        isDrawing = false;
        simplified.clear();

        return false;
    }

    private GestureRock.GestureType convertToGestureType(String name) {
//        Gdx.app.log("Gesture Convert", name.split(" ")[1]);

        if (name.contains(InvertedZShapeType)) {
            return GestureRock.GestureType.INVERTED_Z_SHAPE;

        } else if (name.contains(ZShapeType)) {
            return GestureRock.GestureType.Z_SHAPE;

        } else if (name.contains(VerticalLine)) {
            return GestureRock.GestureType.VERTICAL_LINE;


//        } else if (name.contains(HorizontalLine)) {
//            return GestureRock.GestureType.HORIZONTAL_LINE;

        } else if (name.contains(InvertedVShapeType)) {
            return GestureRock.GestureType.INVERTED_V_SHAPE;

        } else if (name.contains(VShapeType)) {
            return GestureRock.GestureType.V_SHAPE;

        } else if (name.contains(GammaType)) {
            return GestureRock.GestureType.GAMMA;

        } else if (name.contains(AlphaType)) {
            return GestureRock.GestureType.ALPHA;

        } else if (name.contains(SigmaType)) {
            return GestureRock.GestureType.SIGMA;

        } else {
            return null;
        }

    }


    /**
     * Scale device touch coordinates to match that of the game.
     *
     * @param screenX - original x-coordinate based on device screen
     * @return scaled x-coordinate
     */
    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    /**
     * Scale device touch coordinates to match that of the game.
     *
     * @param screenY - original y-coordinate based on device screen
     * @return scaled y-coordinate
     */
    private int scaleY(int screenY) {
        return (int) (screenY / scaleFactorY);
    }


}
