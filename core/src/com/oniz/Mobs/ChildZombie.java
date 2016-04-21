package com.oniz.Mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.oniz.Sound.SoundManager;

import java.util.Random;

public class ChildZombie {

    private GestureRock gestureRock;

    private Vector2 position;
    private Vector2 velocity;

    private final float width = 60;
    private final float height = 130;
    private boolean isAlive;
    private boolean isExploding;
    private boolean isEnemy;

    private float explosionRunTime = 0;

    //for soundFXs
    private SoundManager soundManager;
    private boolean explosionSFXPlayed;


    public ChildZombie(float x, float y, boolean isEnemy) {
        Random r = new Random();
        position = new Vector2(x, y);
        velocity = new Vector2(0, r.nextInt(30)+35);
        isAlive = true;
        isExploding = false;
        this.isEnemy = isEnemy;
        gestureRock = new GestureRock(new Vector2(x+10, y+120));
        soundManager = SoundManager.getInstance();
        explosionSFXPlayed = false;
    }

    public void update(float delta) {
        position.add(velocity.cpy().scl(delta));
        gestureRock.updatePosition(velocity, delta);

        if (gestureRock.getY() <= position.y + 70) {
            isExploding = true;
        }
    }


    public void drawExplosion(SpriteBatch batcher, Animation explosionAnimation, float deltaTime) {

        if (isExploding()) {
            if (!explosionSFXPlayed) {
                soundManager.playExplosion();
                explosionSFXPlayed = true;
            }
            explosionRunTime += deltaTime;
            batcher.draw(explosionAnimation.getKeyFrame(explosionRunTime), position.x - 30, position.y, 130, 130);

            if (explosionAnimation.isAnimationFinished(explosionRunTime + 60 * deltaTime)) {
                isAlive = false;
            }
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

    public boolean isExploding() {
        return isExploding;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public GestureRock getGestureRock() {
        return gestureRock;
    }
}