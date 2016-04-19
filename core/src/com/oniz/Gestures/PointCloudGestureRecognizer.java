//package com.oniz.Gestures;
//
//import com.badlogic.gdx.math.Vector2;
//
//import java.util.ArrayList;
//import java.util.Vector;
//
///**
// * Created by yanyee on 4/19/2016.
// */
//public class PointCloudGestureRecognizer {
//
//    private ArrayList<TemplateGesture> registeredGestures;
//
//    private int NUMSAMPLES = 32;
//
//    protected static void normalize(ArrayList<CloudPoint> points, int numberOfPoints) {
//
//
//    }
//
//    protected  ArrayList<CloudPoint> resample(ArrayList<CloudPoint> inputPoints) {
//        final float increment = PathLength(inputPoints) / (NUMSAMPLES- 1);
//        ArrayList<CloudPoint> newPoints = new ArrayList<CloudPoint>();  //length should be 64
//
//        float distanceSoFar = 0f;
//        CloudPoint firstCloudPoint = inputPoints.get(0);
//
//        int index = 0;
//        float currentPointX = Float.MIN_VALUE;
//        float currentPointY = Float.MIN_VALUE;
//
//        newPoints.add(firstCloudPoint);
//        float firstPointX = firstCloudPoint.x;
//        float firstPointY = firstCloudPoint.y;
//        int firstStrokeID = firstCloudPoint.strokeID;
//
//        index++;
//        int i = 0;
//        int count = inputPoints.size();
//
//        /*
//        : I ← Path-Length(points) / (n − 1)
//2: D ← 0
//3: newP oints ← points0
//4: for each pi in points such that i ≥ 1 do
//5: if pi.strokeId == pi−1.strokeId then
//6: d ← Euclidean-Distance(pi−1, pi)
//7: if (D + d) ≥ I then
//8: q.x ← pi−1.x +((I − D)/d) · (pi.x - pi−1.x)
//9: q.y ← pi−1.y +((I − D)/d) · (pi.y - pi−1.y)
//10: q.strokeId ← pi.strokeId
//11: Append(newP oints, q)
//12: Insert(points, i, q) // q will be the next pi
//13: D ← 0
//14: else D ← D + d
//15: return newP oints
//Path-Length (Points point
//         */
//
//        while (i < count) { //for each pi in points such that i >= 1
//            if (currentPointX == Float.MIN_VALUE) {
//                i++;
//                if (i >= count) {
//                    break;
//                }
//
//                currentPointX = inputPoints.get(i).x;
//                currentPointY = inputPoints.get(i).y;
//            }
//
//            int currentStrokeID = inputPoints.get(i).strokeID;
//            int nextStrokeID = inputPoints.get(i - 1).strokeID;
//
//            if (currentStrokeID == nextStrokeID) {
//
//                float deltaX = currentPointX - firstPointX;
//                float deltaY = currentPointY - firstPointY;
//
//                float distance = (float) Math.hypot(deltaX, deltaY); //eucleadien distance
//
//                if (distanceSoFar + distance >= increment) { //if this condition holds then add it in
//                    float ratio = (increment - distanceSoFar) / distance;
//                    float nx = firstPointX + ratio * deltaX;
//                    float ny = firstPointY + ratio * deltaY;
//                    int nStrokeID = currentStrokeID;
//                    newPoints.add(index, new CloudPoint(nx, ny , nStrokeID));
//                    index++;
//                    firstPointX = nx;       //nx will be the next pi.x
//                    firstPointY = ny;       //ny will be the next pi.y
//                    distanceSoFar = 0;
//
//                } else {
//                    firstPointX = currentPointX;
//                    firstPointY = currentPointY;
//                    currentPointX = Float.MIN_VALUE;
//                    currentPointY = Float.MIN_VALUE;
//                    distanceSoFar += distance;
//                }
//
//            }
//
//
//
//        }
//
//        for (i = index; i < NUMSAMPLES; i++) {
//            newPoints.add(index, new Vector2(firstPointX, firstPointY));
//        }
//        return newPoints;
//    }
//
//    protected static float PathLength(ArrayList<CloudPoint> inputPoints) {
//        float d = 0.0f;
//
//        for (int i = 1; i < inputPoints.size(); i++) {
//            CloudPoint a = inputPoints.get(i);
//            CloudPoint b = inputPoints.get(i - 1);
//
//            d += Distance(b, a);
//        }
//
//        return d;
//    }
//
//    protected static float Distance(CloudPoint a, CloudPoint b) {
//        float dx = b.x - a.x;
//        float dy = b.y - a.y;
//
//        return (float) Math.sqrt(dx * dx + dy * dy);
//    }
//
//
//    public MatchingGesture recognize (ArrayList<CloudPoint> testPoints) {
//
//
//    }
//
//}
//
//class CloudPoint {
//
//    public float x;
//    public float y;
//    public int strokeID;
//
//    public CloudPoint(float x, float y, int strokeID) {
//        this.x = x;
//        this.y = y;
//        this.strokeID = strokeID;
//    }
//
//
//}