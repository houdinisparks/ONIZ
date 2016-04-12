package com.oniz.Mobs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.oniz.Game.AssetLoader;
import java.util.Random;

public class GestureRock {

    private Vector2 position;
    private final float width = 40;
    private final float height = 40;
    private GestureType gestureType;
    private Stage stage;

    private final Vector2 fallVelocity = new Vector2(0, -50);

    /**
     * Pre-defined gesture types
     */
    public enum GestureType {
        INVERTED_Z_SHAPE,
        ALPHA,
        VERTICAL_LINE,
        V_SHAPE,
        INVERTED_V_SHAPE,
        Z_SHAPE,
        GAMMA,
        SIGMA;

        private static final GestureType[] GESTURE_TYPES = values();
        private static final int SIZE = GESTURE_TYPES.length;
        private static Random random = new Random();

        public static GestureType generateRandomGestureType() {
            return GESTURE_TYPES[random.nextInt(SIZE)];
        }
    }

    /**
     * Pre-defined stages
     * RED --> YELLOW --> GREEN --> zombie gets killed (BROWN)
     */
    public enum Stage {
        RED,
        YELLOW,
        GREEN,
        BROWN;

        private static final Stage[] STAGES = values();
        private static final int SIZE = STAGES.length;
        private static Random random = new Random();

        public static Stage generateRandomStage() {
            return STAGES[random.nextInt(SIZE-1)];
        }
    }

    /**
     * Constructor which randomly assigns a gestureType and stage to a newly created zombie.
     * @param position - corresponds to the position of zombie
     */
    public GestureRock(Vector2 position) {
        this.position = position;
        gestureType = GestureType.generateRandomGestureType();
        stage = Stage.generateRandomStage();
    }

    public void updatePosition(Vector2 velocity, float delta) {
        if (stage == Stage.BROWN) {
            position.add(fallVelocity.cpy().scl(delta));
        } else {
            position.add(velocity.cpy().scl(delta));
        }
    }

    /**
     * Weaken the zombie by one stage and assign a new gestureType.
     */
    public void decrementStage() {
        if (stage == Stage.RED) {
            stage = Stage.YELLOW;
            gestureType = GestureType.generateRandomGestureType();
        } else if (stage == Stage.YELLOW) {
            stage = Stage.GREEN;
            gestureType = GestureType.generateRandomGestureType();
        } else if (stage == Stage.GREEN) {
            stage = Stage.BROWN;
        }
    }

    /**
     * Draw the gesture rock with the corresponding stage and gestureType.
     * @param batcher - sprite batch from GameRenderer
     */
    public void draw(SpriteBatch batcher) {
        batcher.draw(AssetLoader.getInstance().gestureStages.get(stage), position.x, position.y, width, height);
        if (stage != Stage.BROWN) {
            batcher.draw(AssetLoader.getInstance().gestureHints.get(gestureType), position.x, position.y, width, height);
        }
    }

    public float getY() {
        return position.y;
    }

    public GestureType getGestureType() {
        return gestureType;
    }

    public Stage getStage() {
        return stage;
    }
}
