package com.oniz.Gestures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by yanyee on 4/19/2016.
 */
public class PointCloudGestureRecognizer {

    private ArrayList<TemplateGesture> templates;

    private static ConcurrentHashMap<TemplateGesture, Float> listOfMatches;

    private static int NUMSAMPLES = 32;

    private static WorkerRunnable[] workerThreads;

    private static String TAG = "PointCloudeGestureRecogniser";

    private static Semaphore runningThreads;

    private static final int NUMBER_OF_THREADS = 6;

    private static ExecutorService poolOfThreads;

    private static volatile float NUMBER


    /*@
    PREPROCESSING
     */

    public PointCloudGestureRecognizer() {
        this.templates = new ArrayList<TemplateGesture>();
        this.workerThreads = new WorkerRunnable[NUMBER_OF_THREADS];
        this.listOfMatches = new ConcurrentHashMap<TemplateGesture, Float>();
        this.runningThreads = new Semaphore(NUMBER_OF_THREADS);
        this.poolOfThreads = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    }

    public synchronized void addGesture(TemplateGesture templateGesture) {
        templates.add(templateGesture);
    }

    public void addGestureFromFile(FileHandle fileHandle) {

        if (fileHandle.isDirectory()) {
            FileHandle[] files = fileHandle.list("json");
            for (FileHandle f : files)
                addGestureFromFile(f);

        } else {
            JsonReader jreader = new JsonReader();

            JsonValue map = jreader.parse(fileHandle);
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

            if (_arrlist_vector.size() <= 5) {
                Gdx.app.log(TAG, "iNPUT SIZE: " + _arrlist_vector.size());
            } else {
                addGesture(new TemplateGesture(_name, _arrlist_vector));
            }
        }
    }

    protected static float[] resample2(ArrayList<Vector2> inputPoints) {

//        ArrayList<Vector2> newPoints = new ArrayList<Vector2>(); //length should be 64
//        newPoints.add(new Vector2(inputPoints.get(0).x, inputPoints.get(0).y));

        float[] newPoints = new float[NUMSAMPLES * 2];
        newPoints[0] = inputPoints.get(0).x;
        newPoints[1] = inputPoints.get(1).y;

        int numPoints = 1;


        float I = pathlength(inputPoints) / (NUMSAMPLES - 1);
        float D = 0f;
        for (int i = 1; i < inputPoints.size(); i++) {

            float d = distance(inputPoints.get(i - 1), inputPoints.get(i));
            if (D + d >= I) {

                //Vector2 firstPoint = inputPoints.get(i - 1);
                float firstPointX = inputPoints.get(i - 1).x;
                float firstPointY = inputPoints.get(i - 1).y;

                while (D + d >= I) {
                    float t = Math.min(Math.max((I - D) / d, 0.0f), 1.0f);
                    if (Float.isNaN(t)) {
                        t = 0.5f;
                    }

                    newPoints[numPoints * 2] = (1.0f - t) * firstPointX + t * inputPoints.get(i).x;
                    newPoints[numPoints * 2 + 1] = (1.0f - t) * firstPointY + t * inputPoints.get(i).y;

//                    newPoints.add(numPoints++, new Vector2(
//                                    (1.0f - t) * firstPoint.x + t * inputPoints.get(i).x,
//                                    (1.0f - t) * firstPoint.y + t * inputPoints.get(i).y)
//                    );
                    numPoints++;

                    d = D + d - I;
                    D = 0;
                    firstPointX = newPoints[(numPoints - 1) * 2];
                    firstPointY = newPoints[(numPoints - 1) * 2 + 1];
                    //firstPoint = newPoints.get(numPoints - 1);  //get prev
                }
                D = d;
            } else {

                D += d;
            }
        }

        if (numPoints == NUMSAMPLES - 1) {
            newPoints[numPoints * 2] = inputPoints.get(inputPoints.size() - 1).x;
            newPoints[numPoints * 2 + 1] = inputPoints.get(inputPoints.size() - 1).y;
            numPoints++;
//            newPoints.add(numPoints++, new Vector2(inputPoints.get(inputPoints.size() - 1).x,
//                    inputPoints.get(inputPoints.size() - 1).y));
        }

        return newPoints;

    }

    protected static float pathlength(ArrayList<Vector2> inputPoints) {
        float d = 0.0f;

        for (int i = 1; i < inputPoints.size(); i++) {
            Vector2 a = inputPoints.get(i);
            Vector2 b = inputPoints.get(i - 1);

            d += distance(b, a);
        }

        return d;
    }

    protected static float[] normalize(ArrayList<Vector2> points) {
        float[] resampledPoints = resample2(points);
        scale(resampledPoints);
        translateToOrigin(resampledPoints);

        return resampledPoints;
    }


    protected static void scale(float[] newPoints) {
        float xmin = Float.MAX_VALUE;
        float xmax = 0;
        float ymin = Float.MAX_VALUE;
        float ymax = 0;
        for (int i = 0; i < NUMSAMPLES; i++) {
            xmin = Math.min(xmin, newPoints[i * 2]);
            ymin = Math.min(ymin, newPoints[i * 2 + 1]);
            xmax = Math.max(xmax, newPoints[i * 2]);
            ymax = Math.max(ymax, newPoints[i * 2 + 1]);
        }
        float scale = Math.max(xmax - xmin, ymax - ymin);
        for (int i = 0; i < NUMSAMPLES; i++) {

            newPoints[i * 2] = (newPoints[i * 2] - xmin) / scale;
            newPoints[i * 2 + 1] = (newPoints[i * 2 + 1] - ymin) / scale;
        }
        Gdx.app.log(TAG, "scale points size list: " + newPoints.length);
    }

    protected static void translateToOrigin(float[] points) {

        float centroidptx = 0f;
        float centroidpty = 0f;
        for (int i = 0; i < NUMSAMPLES; i++) {
            centroidptx += points[i * 2];
            centroidpty += points[i * 2 + 1];
        }
        Gdx.app.log(TAG, "NUMSAMPLES: " + NUMSAMPLES + " Size of List: " + points.length / 2);
        Vector2 centroid = new Vector2(centroidptx / NUMSAMPLES, centroidpty / NUMSAMPLES);

        for (int i = 0; i < NUMSAMPLES; i++) {
            {
                points[i * 2] -= centroid.x;
                points[i * 2 + 1] -= centroid.y;
            }
        }
    }

    protected static float clouddistance(float[] testPoints, float[] templates,
                                         int start) {
        boolean[] matched = new boolean[NUMSAMPLES];
        float sum = 0;
        int i = start;
        // boolean assigned = false;
        do {
            int index = -1;

            float min = Float.MAX_VALUE;
            // assigned = false;
            for (int j = 0; j < matched.length; j++) {
                if (!matched[j]) {
                    float distance = squaredDistance(testPoints[i * 2], testPoints[i * 2 + 1],
                            templates[j * 2], templates[j * 2 + 1]);
                    if (distance < min) {
                        min = distance;
                        index = j;
                        //assigned = true;
                    }
                }
            }


            //if(assigned){
            matched[index] = true;
            //}

            float weight = 1.0f - (((i - start + NUMSAMPLES) % NUMSAMPLES) / NUMSAMPLES);
            sum += weight * min;
            i = (i + 1) % NUMSAMPLES;

        } while (i != start);
        //System.out.println("Are all points matched?: " + matched.toString());
        return sum;
    }

    protected static float greedycloudmatch(float[] testPoints, float[] templates
    ) {

        float epsilon = 0.5f;
        int step = (int) Math.floor(Math.pow(NUMSAMPLES, 1 - epsilon));
        float min = Float.MAX_VALUE;
        int i = 0;
        while (i < NUMSAMPLES) {

            float distance1 = clouddistance(testPoints, templates, i);
            float distance2 = clouddistance(templates, testPoints, i);
            min = Math.min(Math.min(min, distance1), distance2);
            i += step;
            //Gdx.app.log(TAG,"greedycloudmatch step value: " + step + " i value: " + i);
        }

        return min;

    }

    //utility functions


    protected static float squaredDistance(float ax, float ay, float bx, float by) {
        float dx = bx - ax;
        float dy = by - ay;

        return dx * dx + dy * dy;
    }


    protected static float squaredDistance(Vector2 a, Vector2 b) {
        float dx = b.x - a.x;
        float dy = b.y - a.y;

        return dx * dx + dy * dy;
    }


    protected static float distance(Vector2 a, Vector2 b) {
        float dx = b.x - a.x;
        float dy = b.y - a.y;

        return (float) Math.sqrt(dx * dx + dy * dy);
    }



    public MatchingGesture recognize(ArrayList<Vector2> testPoints) {

        //should we averages the result?

        float[] normalisedTestPoints = normalize(testPoints);
        float bestScore = Float.MAX_VALUE;
        TemplateGesture bestMatch = null;

//        parallelize the search?
//        for (TemplateGesture templates : this.templates) {
//            float distance = greedycloudmatch(normalisedTestPoints, templates.getPointcloudVector());
//            if (distance < bestScore) {
//                bestScore = distance;
//                bestMatch = templates;
//            }
//        }

        listOfMatches.clear();

        for (WorkerRunnable workerRunnable : workerThreads) {
            workerRunnable.setTestingVector(normalisedTestPoints.clone());
            try {
                runningThreads.acquire();
                poolOfThreads.execute(workerRunnable);
            } catch (Exception e) {
                Gdx.app.log("runnable: " , "interrupted");
                e.printStackTrace();
            }
        }

//        for (WorkerRunnable workerRunnable : workerThreads) {
//            while (!workerRunnable.isItDone()) {
//
//            }
//        }


        try {
            runningThreads.acquire(NUMBER_OF_THREADS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Gdx.app.log(TAG, "Execution Done! Concurrent Hash Map Elements: " + listOfMatches.toString());

        for (Map.Entry<TemplateGesture, Float> match : listOfMatches.entrySet()) {
            if (match.getValue() < bestScore) {
                bestMatch = match.getKey();
                bestScore = match.getValue();
            }
        }
        runningThreads.release(NUMBER_OF_THREADS);


        bestScore = (float) Math.max((bestScore - 2.0f) / -2.0f, 0.0);

        return new MatchingGesture(bestMatch, bestScore);
    }



    public void setRunnablesWithLoadedGestures() {

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            workerThreads[i] = new WorkerRunnable(listOfMatches, templates, i * templates.size() / NUMBER_OF_THREADS
                    , ((i + 1) * templates.size() / NUMBER_OF_THREADS) , runningThreads);
        }
    }

}

class WorkerRunnable implements Runnable {

    private ArrayList<TemplateGesture> templateGestures;
    private float[] testingVector;
    private ConcurrentHashMap<TemplateGesture, Float> concurrentHashMapHashMap;
    private int startIndex;
    private int endIndex;
    private Semaphore semaphore;

    private boolean isItDone;

    private static Object lock = new Object();

    public WorkerRunnable(ConcurrentHashMap<TemplateGesture, Float> concurrentHashMapHashMap,
                          ArrayList<TemplateGesture> templateGestures,
                          int startIndex, int endIndex, Semaphore semaphore) {

        this.concurrentHashMapHashMap = concurrentHashMapHashMap;
        this.templateGestures = templateGestures;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.semaphore = semaphore;
        isItDone = false;
        Gdx.app.log("Multithreading: " , "startIndex: " + startIndex + " endIndex: " + endIndex +
        "size of list: " + templateGestures.size());
    }

    public void setTestingVector(float[] testingVector) {
        this.testingVector = new float[testingVector.length];
        Gdx.app.log("Memory locations: ", "this.testingVecot: " + this.testingVector +
                "testingVector: " + testingVector);

        System.arraycopy(testingVector, 0, this.testingVector, 0, testingVector.length);
    }

    public void setTemplateGestures(ArrayList<TemplateGesture> templateGestures) {
        this.templateGestures = templateGestures;
    }


    public boolean isItDone() {
        return isItDone;
    }

    @Override
    public void run() {
        Gdx.app.log("Runnable: ", "running");
        isItDone = false;
        float score = Float.MAX_VALUE;
        TemplateGesture match = null;

        for (int i = startIndex; i < endIndex ; i++) {
            TemplateGesture templates = templateGestures.get(i);
            float distance = PointCloudGestureRecognizer.greedycloudmatch(this.testingVector, templates.getPointcloudVector());
            if (distance < score) {
                score = distance;
                match = templates;
            }
        }
        Gdx.app.log("Semaphore: ", "put in hashmap");


            concurrentHashMapHashMap.put(match, score);
            Gdx.app.log("Semaphore: ", "lock released");
            semaphore.release();

        isItDone = true;
    }

}


