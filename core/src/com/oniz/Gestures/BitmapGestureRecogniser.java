package com.oniz.Gestures;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 * Created by yanyee on 4/9/2016.
 */


public class BitmapGestureRecogniser {


    String tag = "BitmapGestureRecognizer";


    /*
    ---------------------FIELDS--------------------
     */

    private ArrayList<TemplateGesture> registeredGestures;

    private static final float SCALING_THRESHOLD = 0.26f;

    private static final float NONUNIFORM_SCALE = (float) Math.sqrt(2);

    private static final int PATCH_SAMPLE_SIZE = 32;



    /*
   --------------------CONSTRUCTOR------------------
    */
    public BitmapGestureRecogniser() {
        registeredGestures = new ArrayList<TemplateGesture>();

    }
    /*
 ------------------METHODS (GESTURE MANAGEMENT)--------
  */

    public void addGesture(TemplateGesture tg) {
        registeredGestures.add(tg);
    }

    @SuppressWarnings("unchecked")
    public void addGestureFromFile(FileHandle handle) {
        if (handle.isDirectory()) {
            FileHandle[] files = handle.list("json");
            for (FileHandle f : files)
                addGestureFromFile(f);

        } else {
            JsonReader jreader = new JsonReader();

            JsonValue map = jreader.parse(handle);
            String _name = String.valueOf(map.get("Name"));
            //Gdx.app.log("Name: " , _name);

            //TODO: Generate vector with the method Vectorize(ArrayList<Vector2> points)
            //		this is done with the TemplateGesture constructor.

            JsonValue getPoints = map.get("Points");

            ArrayList<Vector2> _arrlist_vector = new ArrayList<Vector2>();
            for (JsonValue point : getPoints.iterator()) {
                float x = point.get("X").asFloat();
                float y = point.get("Y").asFloat();
                //System.out.println(tag + " "+ String.valueOf(x) + " " + y);
                _arrlist_vector.add(new Vector2(x, y));
            }

            addGesture(new TemplateGesture(_name, _arrlist_vector));
        }
    }

    public void removeGesture(TemplateGesture tg) {
        registeredGestures.remove(tg);
    }


	/*
    ---------------------METHODS(RECOGNIZER)--------------------
 	*/

    /**
     * Samples the gesture spatially by rendering the gesture into a 2D
     * grayscale bitmap. Scales the gesture to fit the size of the bitmap.
     *
     * @param gesturePoints      the points of the gestuer
     * @param bitmapSize         the size of the bitmap // PATCH_SAMPLE_SIZE
     * @param keepAspectRatio    if the scaling should keep the gesture's
     *                           aspect ratio //false
     * @return a bitmapSize x bitmapSize grayscale bitmap that is represented
     * as a 1D array. The float at index i represents the grayscale
     * value at pixel [i%bitmapSize, i/bitmapSize]
     */


    public static float[] spatialSampling( ArrayList<Vector2> gesturePoints,
                                          int bitmapSize, boolean keepAspectRatio) {

        final float targetPatchSize = bitmapSize - 1;
        float[] sample = new float[bitmapSize * bitmapSize];
        Arrays.fill(sample, 0);

        Rect gestureBoundingBox = generateBoundingBox(gesturePoints);
        final float gestureWidth = gestureBoundingBox.width();
        final float gestureHeight = gestureBoundingBox.height();
        Gdx.app.log("gestureWidth: " , ""+gestureWidth);
        Gdx.app.log("gestureHeight: " , ""+gestureHeight);

        float sx = targetPatchSize / gestureWidth;
        float sy = targetPatchSize / gestureHeight;

        if (keepAspectRatio) {
            float scale = sx < sy ? sx : sy;
            sx = scale;
            sy = scale;
        } else {
            float aspectRatio = gestureWidth / gestureHeight;
            if (aspectRatio > 1) {
                aspectRatio = 1 / aspectRatio;
            }
            if (aspectRatio < SCALING_THRESHOLD) {
                float scale = sx < sy ? sx : sy;
                sx = scale;
                sy = scale;
            } else {
                if (sx > sy) {
                    float scale = sy * NONUNIFORM_SCALE;
                    if (scale < sx) {
                        sx = scale;
                    }
                } else {
                    float scale = sx * NONUNIFORM_SCALE;
                    if (scale < sy) {
                        sy = scale;
                    }
                }
            }
        }
        float preDx = -gestureBoundingBox.centerX();
        float preDy = -gestureBoundingBox.centerY();
        float postDx = targetPatchSize / 2;
        float postDy = targetPatchSize / 2;


        int size;
        float xpos;
        float ypos;
//
//      final GestureStroke stroke = strokes.get(index);
//      float[] strokepoints;

        size = gesturePoints.size();
        final float[] pts = new float[size*2];
        for (int i = 0; i < size; i += 2) {
            pts[i * 2] = (gesturePoints.get(i).x + preDx) * sx + postDx;
            pts[i*2 + 1] = (gesturePoints.get(i).y + preDy) * sy + postDy;
        }
        float segmentEndX = -1;
        float segmentEndY = -1;
        for (int i = 0; i < size*2; i += 2) {
            float segmentStartX = pts[i] < 0 ? 0 : pts[i];
            float segmentStartY = pts[i + 1] < 0 ? 0 : pts[i + 1];
            if (segmentStartX > targetPatchSize) {
                segmentStartX = targetPatchSize;
            }
            if (segmentStartY > targetPatchSize) {
                segmentStartY = targetPatchSize;
            }
            plot(segmentStartX, segmentStartY, sample, bitmapSize);
            if (segmentEndX != -1) {
                // Evaluate horizontally
                if (segmentEndX > segmentStartX) {
                    xpos = (float) Math.ceil(segmentStartX);
                    float slope = (segmentEndY - segmentStartY) /
                            (segmentEndX - segmentStartX);
                    while (xpos < segmentEndX) {
                        ypos = slope * (xpos - segmentStartX) + segmentStartY;
                        plot(xpos, ypos, sample, bitmapSize);
                        xpos++;
                    }
                } else if (segmentEndX < segmentStartX) {
                    xpos = (float) Math.ceil(segmentEndX);
                    float slope = (segmentEndY - segmentStartY) /
                            (segmentEndX - segmentStartX);
                    while (xpos < segmentStartX) {
                        ypos = slope * (xpos - segmentStartX) + segmentStartY;
                        plot(xpos, ypos, sample, bitmapSize);
                        xpos++;
                    }
                }
                // Evaluate vertically
                if (segmentEndY > segmentStartY) {
                    ypos = (float) Math.ceil(segmentStartY);
                    float invertSlope = (segmentEndX - segmentStartX) /
                            (segmentEndY - segmentStartY);
                    while (ypos < segmentEndY) {
                        xpos = invertSlope * (ypos - segmentStartY) + segmentStartX;
                        plot(xpos, ypos, sample, bitmapSize);
                        ypos++;
                    }
                } else if (segmentEndY < segmentStartY) {
                    ypos = (float) Math.ceil(segmentEndY);
                    float invertSlope = (segmentEndX - segmentStartX) /
                            (segmentEndY - segmentStartY);
                    while (ypos < segmentStartY) {
                        xpos = invertSlope * (ypos - segmentStartY) + segmentStartX;
                        plot(xpos, ypos, sample, bitmapSize);
                        ypos++;
                    }
                }
            }
            segmentEndX = segmentStartX;
            segmentEndY = segmentStartY;
        }

        return sample;
    }

    private static void plot(float x, float y, float[] sample, int sampleSize) {
        x = x < 0 ? 0 : x;
        y = y < 0 ? 0 : y;
        int xFloor = (int) Math.floor(x);
        int xCeiling = (int) Math.ceil(x);
        int yFloor = (int) Math.floor(y);
        int yCeiling = (int) Math.ceil(y);

        // if it's an integer
        if (x == xFloor && y == yFloor) {
            int index = yCeiling * sampleSize + xCeiling;
            if (sample[index] < 1) {
                sample[index] = 1;
            }
        } else {
            final double xFloorSq = Math.pow(xFloor - x, 2);
            final double yFloorSq = Math.pow(yFloor - y, 2);
            final double xCeilingSq = Math.pow(xCeiling - x, 2);
            final double yCeilingSq = Math.pow(yCeiling - y, 2);
            float topLeft = (float) Math.sqrt(xFloorSq + yFloorSq);
            float topRight = (float) Math.sqrt(xCeilingSq + yFloorSq);
            float btmLeft = (float) Math.sqrt(xFloorSq + yCeilingSq);
            float btmRight = (float) Math.sqrt(xCeilingSq + yCeilingSq);
            float sum = topLeft + topRight + btmLeft + btmRight;

            float value = topLeft / sum;
            int index = yFloor * sampleSize + xFloor;
            if (value > sample[index]) {
                sample[index] = value;
            }

            value = topRight / sum;
            index = yFloor * sampleSize + xCeiling;
            if (value > sample[index]) {
                sample[index] = value;
            }

            value = btmLeft / sum;
            index = yCeiling * sampleSize + xFloor;
            if (value > sample[index]) {
                sample[index] = value;
            }

            value = btmRight / sum;
            index = yCeiling * sampleSize + xCeiling;
            if (value > sample[index]) {
                sample[index] = value;
            }
        }
    }

    private static Rect generateBoundingBox(ArrayList<Vector2> points) {
        Rect boundingBox = null;
        for (Vector2 point : points) {
            if (boundingBox == null) {
                boundingBox = new Rect(point.y, point.x, point.x, point.y);
            } else {
                boundingBox.union(point.x, point.y);
            }
        }
        return boundingBox;
    }



    /**
     * Calculates the squared Euclidean distance between two vectors.
     *
<<<<<<< HEAD
     * @param vector1   //testing vector
     * @param vector2   //template vector
     * @return the distance //score
     */
    public static float squaredEuclideanDistance(float[] vector1, float[] vector2) {
        float squaredDistance = 0;
        int size = vector1.length;
        Gdx.app.log("vector1 length: ", vector1.length + "");
        Gdx.app.log("vector2 length: ", vector2.length + "");
        for (int i = 0; i < size; i++) {
            float difference = vector1[i] - vector2[i];
            squaredDistance += difference * difference;
        }
        return squaredDistance / size;
    }

    public MatchingGesture Recognize(ArrayList<Vector2> originalPath) {
        float[] testVector = spatialSampling(originalPath, PATCH_SAMPLE_SIZE, false);

        TemplateGesture match = null;
        double distance = 0;
        double maxScore = 0;
        double currentScore;
        int numberOfGestures = 0;

        for (TemplateGesture templateGesture : registeredGestures) {

            distance = squaredEuclideanDistance(testVector, templateGesture.getBitmapVector());
            Gdx.app.log("squaredE: ", distance + "");
            numberOfGestures += 1;

            if (distance == 0) {
                currentScore = Double.MAX_VALUE;
            }else {
                currentScore = 1 / distance;
            }

            if (currentScore > maxScore) {
                Gdx.app.log("Current Score: ", currentScore + "");
                maxScore = currentScore;
                match = templateGesture;
            }
        }

        Gdx.app.log("Number of Gestures: ", "" + numberOfGestures);
        return new MatchingGesture(match, maxScore);
    }


}

/**
 * Rectangle Object for Bounding Box Object
 */
class Rect{

    public float bottom;
    public float left;
    public float right;
    public float top;


    public Rect(float bottom, float left, float right, float top) {
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.top = top;
    }

    public Rect clone(){
        return new Rect(this.bottom, this.left, this.right, this.top);
    }

    public float centerX() {
        return (this.left + this.right) / 2;
    }

    public float centerY() {
        return (this.top+this.bottom) /2;

    }

    public float width() {
        return this.right - this.left;

    }

    public float height() {
        return this.bottom - this.top;

    }

    public void set(float bottom, float left, float right, float top) {
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;

    }

    public void union(float x, float y) {
        if (x < this.left) {
            this.left = x;
        }

        if (x > this.right) {
            this.right = x;

        }

        if (y < this.top) {
            this.top = y;
        }

        if (y > this.bottom) {
            this.bottom = y;
        }
    }

    public void union(Rect rect) {
        if (rect.left < this.left) {
            this.left = rect.left;
        }
        if(rect.right > this.right) {
            this.right = rect.right;
        }

        if (rect.top < this.top) {
            this.top = rect.top;
        }

        if (rect.bottom > this.bottom) {
            this.bottom = rect.bottom;
        }
    }
}

