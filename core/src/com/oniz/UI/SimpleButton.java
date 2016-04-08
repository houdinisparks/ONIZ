package com.oniz.UI;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.oniz.Game.AssetLoader;


public class SimpleButton extends ImageButton {
    public SimpleButton(float x, float y, float width, float height, TextureRegion buttonUp, TextureRegion buttonDown) {
        super(new TextureRegionDrawable(buttonUp), new TextureRegionDrawable(buttonDown));
        super.setPosition(x, y);
        super.setWidth(width);
        super.setHeight(height);
    }
}