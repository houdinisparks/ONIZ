package com.oniz.Mobs;

import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class ChildZombie {

    private Vector2 position;
    private Vector2 velocity;

    private int id;
    private float width;
    private float height;
    private boolean isAlive;
    private int gestureType; // '0' is circle, '1' is square etc
    private int points;


    public ChildZombie(float x, float y) {
        Random r = new Random();
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, r.nextInt(20)+10);
        this.width = 60;
        this.height = 122;
        this.isAlive = true;
        this.gestureType = r.nextInt(2);
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

    public int getGestureType() {
        return gestureType;
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public void setAlive(boolean val) {
        this.isAlive = val;
    }
}