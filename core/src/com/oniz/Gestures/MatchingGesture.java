package com.oniz.Gestures;

public class MatchingGesture {
	private TemplateGesture gesture;	

	private double score;

	public MatchingGesture(TemplateGesture gesture, double score) {
		this.gesture = gesture;
		this.score = score;
	}

	public TemplateGesture getGesture() {
		return gesture;
	}

	public double getScore() {
		return score;
	}
}
