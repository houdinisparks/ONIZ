package com.oniz.Gestures;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * This class is orientation, position, and scale invariant.
 * Only the following methods are used, for Protractor Recogniser class
 * UNUSED CLASS.
 */
public class DollarUnistrokeRecognizer {



	public static int NumSamples = 64;

	public static float SquareSize = 250.0f;

	public static Vector2 Origin = new Vector2(0.0f, 0.0f);

	private ArrayList<Vector2> points;

	private float radians;

	protected DollarUnistrokeRecognizer(ArrayList<Vector2> points) {
		this.points = points;
		this.radians = 0.0f;
	}

	// This should be in "ProtractorGestureRecognizer" class :P.
	public static float[] Vectorize(ArrayList<Vector2> points) {
		DollarUnistrokeRecognizer dollar = new DollarUnistrokeRecognizer(points);
		//this.points = points
		dollar.Resample();
		Gdx.app.log("DollarUnistrokeRe: ", "" + dollar.points.size());
		dollar.CalculateIndicativeAngle();
		dollar.Rotate();
		dollar.Scale();
		dollar.Translate();
		
		float sum = 0.0f;
		float magnitude = 0.0f;
		float[] vector = new float[dollar.points.size() * 2];
		int index = 0;
		for (Vector2 v : dollar.points) {
			vector[index++] = v.x;
			vector[index++] = v.y;
			
			sum += v.x * v.x + v.y * v.y;
		}

		magnitude = (float) Math.sqrt(sum);
		for (int i = 0; i < vector.length; i++)
			vector[i] /= magnitude;

		return vector;
	}

	/*
	Step 1 : TemporalResample
	To make gesture paths directly comparable even at different movement speeds.
	32 <= N <= 256. Pseudocode uses N = 64.
		1. Calculate total length of M Points Path
		2.Divide M by (N-1) = I (increment between N new points

	At the end of the day, templates and candidate gesture will have N Points.
	 */
	protected void Resample() {
		float I = PathLength() / (NumSamples - 1);
		float D = 0.0f;
		ArrayList<Vector2> newPoints = new ArrayList<Vector2>();
		newPoints.add(points.get(0));

		for (int idx = 1; idx < points.size(); idx++) {
			Vector2 curr = points.get(idx);
			Vector2 prev = points.get(idx - 1);

			float d = Distance(prev, curr);

			if (D + d >= I) {
				Vector2 q = new Vector2();

				q.x = prev.x + ((I - D) / d) * (curr.x - prev.x);
				q.y = prev.y + ((I - D) / d) * (curr.y - prev.y);

				newPoints.add(q);
				points.add(idx + 1, q);

				D = 0.0f;
			} else {
				D += d;
			}
		}

		if (newPoints.size() == NumSamples - 1)
			newPoints.add(new Vector2(points.get(points.size() - 1).x, points
					.get(points.size() - 1).y));

		points = newPoints;
	}

	protected Vector2 Centroid() {
		float x = 0.0f;
		float y = 0.0f;

		for (Vector2 v : points) {
			x += v.x;
			y += v.y;
		}

		x /= points.size();
		y /= points.size();

		return new Vector2(x, y);
	}

	/*
	Step 2.1 : Find Indicate Angle of Candidate Gesture
	Angle formed between centroid of the gesture, and gesture's first point.
	 */
	protected void CalculateIndicativeAngle() {
		Vector2 c = Centroid();
		radians = MathUtils.atan2(c.y - points.get(0).y, c.x - points.get(0).x);
	}

	/*
	Step 2.2 : Rotate Candidate Gesture
	Rotate the gesture until the indicative angle becomes 0 decgrees.
	Finding the best angular match.
	 */
	protected void Rotate() {
		Vector2 c = Centroid();
		float cos = MathUtils.cos(-radians);
		float sin = MathUtils.sin(-radians);

		ArrayList<Vector2> newPoints = new ArrayList<Vector2>();

		for (Vector2 v : points) {
			float qx = (v.x - c.x) * cos - (v.y - c.y) * sin + c.x;
			float qy = (v.x - c.x) * sin + (v.y - c.y) * cos + c.y;

			newPoints.add(new Vector2(qx, qy));
		}

		points = newPoints;
	}

	/*
	Step 4 : Scaled to a Reference Square
	Allow us to rotate the candidate about its centroid and asume that changes in pairwise
	point-distances between C and Ti are due only to rotation, not to aspect ratio.
	 */
	protected void Scale() {
		Rectangle B = BoundingBox();
		ArrayList<Vector2> newPoints = new ArrayList<Vector2>();

		for (Vector2 v : points) {
			float qx = v.x * (SquareSize / B.width);
			float qy = v.y * (SquareSize / B.height);

			newPoints.add(new Vector2(qx, qy));
		}

		points = newPoints;
	}

	protected Rectangle BoundingBox() {
		float minX = Float.POSITIVE_INFINITY;
		float maxX = Float.NEGATIVE_INFINITY;
		float minY = Float.POSITIVE_INFINITY;
		float maxY = Float.NEGATIVE_INFINITY;

		for (Vector2 v : points) {
			if (v.x < minX)
				minX = v.x;
			if (v.x > maxX)
				maxX = v.x;
			if (v.y < minY)
				minY = v.y;
			if (v.y > maxY)
				maxY = v.y;
		}

		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

	/*
	Step 2.2 : Translated to Reference Point
	Translates the gesture so that is centroid is at (0,0)
	 */
	protected void Translate() {
		Vector2 c = Centroid();
		ArrayList<Vector2> newPoints = new ArrayList<Vector2>();

		for (Vector2 v : points) {
			float qx = v.x + Origin.x - c.x;
			float qy = v.y + Origin.y - c.y;

			newPoints.add(new Vector2(qx, qy));
		}

		points = newPoints;
	}

	protected float PathLength() {
		float d = 0.0f;

		for (int i = 1; i < points.size(); i++) {
			Vector2 a = points.get(i);
			Vector2 b = points.get(i - 1);

			d += Distance(b, a);
		}

		return d;
	}

	protected float Distance(Vector2 a, Vector2 b) {
		float dx = b.x - a.x;
		float dy = b.y - a.y;

		return (float) Math.sqrt(dx * dx + dy * dy);
	}

}
