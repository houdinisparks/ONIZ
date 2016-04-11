package com.oniz.Gestures;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class TemplateGesture{
	private String name;

	private ArrayList<Vector2> points;

	private float[] protractorVector;


	private float[] bitmapVector;

	private Rect boundingBox;

	private static final int PATCH_SAMPLE_SIZE = 32;



	//TODO: Use this TemplateGesture Constructor instead.
	public TemplateGesture(String name, ArrayList<Vector2> points) {
		this.name = name;
		this.points = points;
		this.protractorVector = ProtractorGestureRecognizer.Vectorize(this.points);
		this.bitmapVector = BitmapGestureRecogniser.spatialSampling(this.points, PATCH_SAMPLE_SIZE, false);
	}

	public TemplateGesture(String name, ArrayList<Vector2> points,
			float[] vector) {
		this.name = name;
		this.points = points;
		this.protractorVector = vector;
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
class Rect{

	public float bottom;
	public float left;
	public float right;
	public float top;

	public Rect() {

	}

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
