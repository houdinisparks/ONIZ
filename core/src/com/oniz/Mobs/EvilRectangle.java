package com.oniz.Mobs;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by robin on 3/3/16.
 */
public class EvilRectangle extends Rectangle {
    private boolean alive;
    public float x = 0;
    public float y = 0;
    public float width = 0;
    public float height = 0;
    public EvilRectangle(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.alive = true;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public void setAlive(boolean val) {
        this.alive = val;
    }

}
