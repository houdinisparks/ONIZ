package com.oniz.Mobs;

import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class ChildZombie {

    private GestureRock gestureRock;

    private Vector2 position;
    private Vector2 velocity;

    private final float width = 60;
    private final float height = 122;
    private boolean isAlive;
    private boolean isEnemy;


    public ChildZombie(float x, float y, boolean isEnemy) {
        Random r = new Random();
        position = new Vector2(x, y);
        velocity = new Vector2(0, r.nextInt(20)+20);
        isAlive = true;
        this.isEnemy = isEnemy;
        gestureRock = new GestureRock(new Vector2(x+10, y+120));
    }

    public void update(float delta) {
        position.add(velocity.cpy().scl(delta));
        gestureRock.updatePosition(velocity, delta);

        if (gestureRock.getY() <= position.y + 100) {
            isAlive = false;
        }
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
        return isAlive;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public GestureRock getGestureRock() {
        return gestureRock;
    }
}