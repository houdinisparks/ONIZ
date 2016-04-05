package com.oniz.Gestures;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

import sun.rmi.runtime.Log;

public class ProtractorGestureRecognizer {
    String tag = "ProtractorGestureRecognizer";


    /*
    ---------------------FIELDS--------------------
     */
    public static int NumSamples = 64;

    public static float SquareSize = 250.0f;

    public static Vector2 Origin = new Vector2(0.0f, 0.0f);

    private ArrayList<Vector2> currentPoints;

    private float radians;

    private ArrayList<TemplateGesture> registeredGestures;

    /*
    --------------------CONSTRUCTOR------------------
     */
    public ProtractorGestureRecognizer() {
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

    /*
    Step 1 : Resample
    To make gesture paths directly comparable even at different movement speeds.
    32 <= N <= 256. Pseudocode uses N = 64.
        1. Calculate total length of M Points Path
        2.Divide M by (N-1) = I (increment between N new points

    At the end of the day, templates and candidate gesture will have N Points.
    Note: This method is implemented in the Vectorise method.
    CHECKED : Output vector has 64 points.
     */
    protected static ArrayList<Vector2> Resample(ArrayList<Vector2> inputPoints) {
        final float increment = PathLength(inputPoints) / (NumSamples - 1);
        ArrayList<Vector2> newPoints = new ArrayList<Vector2>(); //length should be 64

        float distanceSoFar = 0f;
        float firstPointX = inputPoints.get(0).x;
        float firstPointY = inputPoints.get(0).y;

        int index = 0;
        float currentPointX = Float.MIN_VALUE;
        float currentPointY = Float.MIN_VALUE;

        newPoints.add(new Vector2(firstPointX, firstPointY));
        index++;
        int i = 0;
        int count = inputPoints.size();
        while (i < count) {
            if (currentPointX == Float.MIN_VALUE) {
                i++;
                if (i >= count) {
                    break;
                }

                currentPointX = inputPoints.get(i).x;
                currentPointY = inputPoints.get(i).y;
            }

            float deltaX = currentPointX - firstPointX;
            float deltaY = currentPointY - firstPointY;

            float distance = (float) Math.hypot(deltaX, deltaY);

            if (distanceSoFar + distance >= increment) {
                float ratio = (increment - distanceSoFar) / distance;
                float nx = firstPointX + ratio * deltaX;
                float ny = firstPointY + ratio * deltaY;
                newPoints.add(index, new Vector2(nx, ny));
                index++;
                firstPointX = nx;
                firstPointY = ny;
                distanceSoFar = 0;

            } else {
                firstPointX = currentPointX;
                firstPointY = currentPointY;
                currentPointX = Float.MIN_VALUE;
                currentPointY = Float.MIN_VALUE;
                distanceSoFar += distance;
            }

        }

        for (i = index; i < NumSamples; i++) {
            newPoints.add(index, new Vector2(firstPointX, firstPointY));
        }
        return newPoints;
    }


//    protected static ArrayList<Vector2> Resample(ArrayList<Vector2> inputPoints) {
//        float I = PathLength(inputPoints) / (NumSamples - 1);
//        float D = 0.0f;
//        ArrayList<Vector2> newPoints = new ArrayList<Vector2>();
//        newPoints.add(inputPoints.get(0));
//
//        //might have a problem here, modifying arraylist while in loop
//        for (int idx = 1; idx < inputPoints.size(); idx++) {
//            Vector2 curr = inputPoints.get(idx);
//            Vector2 prev = inputPoints.get(idx - 1);
//
//            float d = Distance(prev, curr);
//
//            if (D + d >= I) {
//                Vector2 q = new Vector2();
//
//                q.x = prev.x + ((I - D) / d) * (curr.x - prev.x);
//                q.y = prev.y + ((I - D) / d) * (curr.y - prev.y);
//
//                newPoints.add(q);
//                inputPoints.add(idx + 1, q);        //q will be the next pi
//
//                D = 0.0f;
//            } else {
//                D += d;
//            }
//
//        }

//		if (newPoints.size() == NumSamples - 1)
//			newPoints.add(new Vector2(inputPoints.get(inputPoints.size() - 1).x, inputPoints
//					.get(inputPoints.size() - 1).y));
//
//        Gdx.app.log("tag", "Size of inputPoints: " + newPoints.size());
//        return newPoints;
//    }

    protected static float PathLength(ArrayList<Vector2> inputPoints) {
        float d = 0.0f;

        for (int i = 1; i < inputPoints.size(); i++) {
            Vector2 a = inputPoints.get(i);
            Vector2 b = inputPoints.get(i - 1);

            d += Distance(b, a);
        }

        return d;
    }

    protected static float Distance(Vector2 a, Vector2 b) {
        float dx = b.x - a.x;
        float dy = b.y - a.y;

        return (float) Math.sqrt(dx * dx + dy * dy);
    }


    /*
    Step 2 : Vectorise.
    Generate a vector representation for the gesture.
     */
    public static float[] Vectorize(ArrayList<Vector2> inputPoints) {
        ArrayList<Vector2> resampledInputPoints = Resample(inputPoints);
        Gdx.app.log("Vectorising number of points: ", "" + resampledInputPoints.size());

        Vector2 centroid = Centroid(resampledInputPoints);
        ArrayList<Vector2> translatedPoints = Translate(resampledInputPoints, centroid);
        double indicativeAngle = MathUtils.atan2(translatedPoints.get(0).y, translatedPoints.get(0).x);

        double baseOrientation = (Math.PI / 4) * Math.floor((indicativeAngle + Math.PI / 8) / (Math.PI / 4));
        double delta = baseOrientation - indicativeAngle;

        float sum = 0;
        float[] vector = new float[translatedPoints.size() * 2];
        int index = 0;

        for (Vector2 points : translatedPoints) {
            float newX = (float) (points.x * Math.cos(delta) - points.y * Math.sin(delta));
            float newY = (float) (points.y * Math.cos(delta) + points.x * Math.sin(delta));
            vector[index++] = newX;
            vector[index++] = newY;

            sum += newX * newX + newY * newY;
        }

        float magnitude = (float) Math.sqrt(sum);
        for (int i = 0; i < vector.length; i++) {
            vector[i] /= magnitude;
        }

        return vector;
    }

    /*
    Step 2.1 : Find Centroid of Candidate Gesture
    //CHECKED
     */
    protected static Vector2 Centroid(ArrayList<Vector2> resampledInputPoints) {
        float x = 0.0f;
        float y = 0.0f;

        for (Vector2 v : resampledInputPoints) {
            x += v.x;
            y += v.y;
        }

        x /= resampledInputPoints.size();
        y /= resampledInputPoints.size();

        return new Vector2(x, y);
    }


    /*
    Step 2.2 : Translated to Reference Point
    Translates the gesture so that is centroid is at (0,0)
    //CHECKED
     */
    protected static ArrayList<Vector2> Translate(ArrayList<Vector2> resampledInputPoints, Vector2 centroid) {

        ArrayList<Vector2> newPoints = new ArrayList<Vector2>();

        for (Vector2 v : resampledInputPoints) {
            float qx = v.x + Origin.x - centroid.x;
            float qy = v.y + Origin.y - centroid.y;

            newPoints.add(new Vector2(qx, qy));
        }

        return newPoints;
    }
    /*
	Step 2.3 : Find Indicate Angle of Candidate Gesture
	Angle formed between centroid of the gesture, and gesture's first point.
	 */
//	protected void CalculateIndicativeAngle() {
//		Vector2 c = Centroid();
//		radians = MathUtils.atan2(c.y - points.get(0).y, c.x - points.get(0).x);
//	}

	/*
	Step 2.3 : Rotate Candidate Gesture
	Rotate the gesture until the indicative angle becomes 0 decgrees.
	Finding the best angular match.
	 */
//	protected void Rotate() {
//		Vector2 c = Centroid();
//		float cos = MathUtils.cos(-radians);
//		float sin = MathUtils.sin(-radians);
//
//		ArrayList<Vector2> newPoints = new ArrayList<Vector2>();
//
//		for (Vector2 v : points) {
//			float qx = (v.x - c.x) * cos - (v.y - c.y) * sin + c.x;
//			float qy = (v.x - c.x) * sin + (v.y - c.y) * cos + c.y;
//
//			newPoints.add(new Vector2(qx, qy));
//		}
//

//		points = newPoints;
//	}


    public MatchingGesture Recognize(ArrayList<Vector2> originalPath) {
        float[] vector = Vectorize(originalPath);

        TemplateGesture match = null;
        float maxScore = 0;
        float currentScore;
        for (TemplateGesture gesture : registeredGestures) {
            currentScore = 1.0f / (OptimalCosineDistance(gesture.getVector(), vector));

            if (currentScore > maxScore) {
                maxScore = currentScore;
                match = gesture;
            }
        }

        return new MatchingGesture(match, maxScore);
    }


//    public MatchingGesture Recognize(ArrayList<Vector2> originalPath) {
//        float[] vector = DollarUnistrokeRecognizer.Vectorize(originalPath);
//
//        TemplateGesture match = null;
//        float b = Float.POSITIVE_INFINITY;
//        for (TemplateGesture gesture : registeredGestures) {
//            float d = OptimalCosineDistance(gesture.getVector(), vector);
//
//            if (d < b) {
//                b = d;
//                match = gesture;
//            }
//        }
//
//        return new MatchingGesture(match, 1.0f / b);
//    }

//	RECOGNIZE (vector, templates)
//	maxScore ← 0
//	foreach template in templates do
//	distance ← OPTIMAL-COSINE-DISTANCE (templatevector, vector) score ← 1 / distance
//	if score > maxScore then maxScore ← score
//	match ← templatename return 〈match, score〉


    private float OptimalCosineDistance(float[] v1, float[] v2) {
        float a = 0.0f;
        float b = 0.0f;
        float angle = 0.0f;


        //int len = Math.min(v1.length, v2.length);
        Gdx.app.log("OptimalCosineDistance", "Size of v1: " + v1.length +
                "Size of v2: " + v2.length);
        for (int i = 0; i < v1.length; i += 2) {
            a += v1[i] * v2[i] + v1[i + 1] * v2[i + 1];git
            b += v1[i] * v2[i + 1] - v1[i + 1] * v2[i];
        }

        angle = (float) Math.atan(b / a);
        Gdx.app.log("cosinedistancelog: ", "" + Math.PI / 2);
        return (float) Math.acos(a * Math.cos(angle) + b * Math.sin(angle));
    }

}
