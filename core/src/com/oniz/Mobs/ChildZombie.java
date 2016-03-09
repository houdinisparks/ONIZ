package com.oniz.Mobs;

import com.badlogic.gdx.math.Vector2;

public class ChildZombie {

    private Vector2 position;
    private Vector2 velocity;

    private int id;
    private float width;
    private float height;
    private boolean isAlive;
    private int gestureType; // '0' is circle, '1' is triangle etc
    private int points;


    public ChildZombie(float x, float y, float width, float height, float v) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, v);
        this.width = width;
        this.height = height;
        this.isAlive = true;
    }

    public void update(float delta) {
        position.add(velocity.cpy().scl(delta));
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public void setAlive(boolean val) {
        this.isAlive = val;
    }
}