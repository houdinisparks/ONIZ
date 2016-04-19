package com.oniz.UI;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.oniz.Sound.SoundManager;

/**
 * Created by robin on 19/4/16.
 */
public class AdvancedClickListener extends ClickListener{

    private SoundManager soundManager;

    public AdvancedClickListener() {
        this.soundManager = soundManager.getInstance();
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        super.touchUp(event, x, y, pointer, button);
        soundManager.playUIButtonUp();
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        soundManager.playUIButtonClickDown();
        return super.touchDown(event, x, y, pointer, button);
    }
}
