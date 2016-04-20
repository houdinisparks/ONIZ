package com.oniz.Gestures;


import java.util.ArrayList;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import com.oniz.Game.AssetLoader;
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

    private PointCloudGestureRecognizer pointCloudGestureRecognizer;

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

    //soundFXs

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
    private static final String mShapeType = "mshape";
    private static final String RevCShapeType = "revcshape";
    private static final String TriangleShape = "triangle";

//    private static int SigmaFileCount = 1;
//    private static int ZShapeFileCount = 1;
//    private static int InvertedZShapeFileCount = 1;
//    private static int HorizontalLineFileCount = 1;
//    private static int VerticalLineFileCount = 1;
//    private static int VShapeTypeFileCount = 1;
//    private static int InvertedVShapeFileCount = 1;
//    private static int AlphaFileCount = 1;
//    private static int GammaFileCount = 1;

    public GestureRecognizerInputProcessor(GameWorld gameWorld, float scaleFactorX, float scaleFactorY) {
        super();
        this.gameWorld = gameWorld;


        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;

		/*-----------Gesture Detection------------*/
        //protractorGestureRecognizer = AssetLoader.getInstance().protractorGestureRecognizer;
        pointCloudGestureRecognizer = AssetLoader.getInstance().pointCloudGestureRecognizer;

//        Hashtable<String, FileHandle> gesturesList = AssetLoader.getInstance().gesturesList;
//
//        for (String key :
//                gesturesList.keySet()) {
//            protractorGestureRecognizer.addGestureFromFile(gesturesList.get(key));
//        }


        originalPath = new ArrayList<Vector2>();

		/*-----------Gesture Path Display------------*/
        int maxInputPoints = 100;
        this.inputPoints = new FixedList<Vector2>(maxInputPoints, Vector2.class);
        simplified = new Array<Vector2>(true, maxInputPoints, Vector.class);
        resolve();

        /*-----------Sounds-----------*/
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
        if (!gameWorld.isRunning()) {
            originalPath.clear();
            inputPoints.clear();
            simplified.clear();
            return false;
        }

//        Gdx.app.log(TAG, "touchDown x: " + x + " y: " + y);
        /*-------Gesture Detection-----------*/
        originalPath.add(new Vector2(x, y));
        Gdx.app.log("Points Added: ", " x: " + x+ " y:" + y );

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
        if (!gameWorld.isRunning())
            return false;
//        Gdx.app.log(TAG, "touchDragged x: " + x + " y: " + y);
        /*-------Gesture Detection-----------*/
        originalPath.add(new Vector2(x, y));
        Gdx.app.log("Points Added: ", " x: " + x+" y:" + y);


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
        final int xfinal = x;
        final int yfinal = y;
        if (!gameWorld.isRunning())
            return false;
        /*-------Gesture Detection-----------*/


        Gdx.app.log("Gesture Point Count", "Count is: " + originalPath.size());

        if (originalPath.size() >= 8) {

//            Gdx.app.postRunnable(new Runnable() {
//                @Override
//                public void run() {

                    originalPath.add(new Vector2(xfinal, yfinal));

                    MatchingGesture match = pointCloudGestureRecognizer.recognize(originalPath);

                    GestureRock.GestureType gestureType = convertToGestureType(match.getGesture().getName());

                    if (match.getScore() < 0.5f || gameWorld.zombieDoesNotExist(gestureType)) {
                        Gdx.app.log("Gesture Name/Score", "none matched. " + match.getScore());
                        gameWorld.isMissed.set(true);
                    } else {
                        Gdx.app.log("Gesture Name/Score", match.getGesture().getName()
                                + Double.toString(match.getScore()));
                        gameWorld.weakenZombie(gestureType);
                    }
                    originalPath.clear();

//                }
//            });


        }

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

        } else if (name.contains(mShapeType)) {
            return GestureRock.GestureType.M_SHAPE;

        } else if (name.contains(RevCShapeType)) {
            return GestureRock.GestureType.REV_C_SHAPE;

        } else if (name.contains(TriangleShape)) {
            return GestureRock.GestureType.TRIANGLE;

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

class FileHandleThread extends Thread {

    private ProtractorGestureRecognizer protractorGestureRecognizer;
    private FileHandle fileHandle;
    private int fileCount;

    public FileHandleThread(ProtractorGestureRecognizer protractorGestureRecognizer, FileHandle fileHandle) {
        this.protractorGestureRecognizer = protractorGestureRecognizer;
        this.fileHandle = fileHandle;
        this.fileCount = 1;
    }

    public void run() {

        if (fileHandle.isDirectory()) {
            FileHandle[] fileHandles = fileHandle.list(".json");
            for (FileHandle jsonFile : fileHandles) {
                fileCount += 1;
                protractorGestureRecognizer.addGestureFromFile(jsonFile);
            }
        } else {
            Gdx.app.error("Adding Gesture Error!: ", "File Handle not directory!");
        }

        Gdx.app.log("Gesture:" + fileHandle.name(), fileCount + "");

    }


}
