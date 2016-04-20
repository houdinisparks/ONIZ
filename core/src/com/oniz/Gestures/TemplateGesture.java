package com.oniz.Gestures;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class TemplateGesture{
	private String name;

	private ArrayList<Vector2> points;

	private float[] protractorVector;

	private float[] pointcloudVector;

	private float[] bitmapVector;

	private Rect boundingBox;

	private static final int PATCH_SAMPLE_SIZE = 32;




	//TODO: Use this TemplateGesture Constructor instead.
	public TemplateGesture(String name, ArrayList<Vector2> points) {
		this.name = name;
		this.points = points;
		//this.protractorVector = ProtractorGestureRecognizer.Vectorize(this.points);
		this.pointcloudVector = PointCloudGestureRecognizer.normalize(this.points);
	}

	public float[] getPointcloudVector() {
		return pointcloudVector;
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



	public Rect getBoundingBox() {
		return boundingBox;
	}




}
/**
 * Rectangle Object for Bounding Box Object
 */
