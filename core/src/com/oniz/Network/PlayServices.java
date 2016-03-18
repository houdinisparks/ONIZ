package com.oniz.Network;

/**
 * Created by yanyee on 3/18/2016.
 */
public interface PlayServices {

    public void signIn();

    public void signOut();

    public void rateGame();

    public void unlockAchievement();

    public void submitScore(int highScore);

    public void showAchievement();

    public void showScore();

    public boolean isSignedIn();
}
