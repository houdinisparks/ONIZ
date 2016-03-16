package com.oniz.UI;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.oniz.Game.AssetLoader;


public class SimpleButton extends ImageButton {
    public SimpleButton(Drawable imageUp, Drawable imageDown) {
        //imageUp, imageDown
        super(imageUp, imageDown);
    }
}
