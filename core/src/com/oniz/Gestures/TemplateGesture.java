package com.oniz.Gestures;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class TemplateGesture{
	private String name;

	private ArrayList<Vector2> points;

	private float[] protractorVector;

	private float[] bitmapVector;

	private static final int PATCH_SAMPLE_SIZE = 32;


	//TODO: Use this TemplateGesture Constructor instead.
	public TemplateGesture(String name, ArrayList<Vector2> points) {
		this.name = name;
		this.points = points;
		this.protractorVector = ProtractorGestureRecognizer.Vectorize(this.points);
		this.bitmapVector = BitmapGestureRecogniser.spatialSampling(this.points, PATCH_SAMPLE_SIZE, false);
	}

	public float[] getProtractorVector() {
		return protractorVector;
	}

	public float[] getBitmapVector() {
		return bitmapVector;
	}


	public ArrayList<Vector2> getPoints() {
		return points;
	}

	public String getName() {
		return name;
	}

}
