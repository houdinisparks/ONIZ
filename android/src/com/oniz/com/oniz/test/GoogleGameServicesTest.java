package com.oniz.com.oniz.test;

import android.os.Build;

import com.google.example.games.basegameutils.GameHelper;
import com.oniz.AndroidLauncher;
import com.oniz.BuildConfig;
import com.oniz.Network.PlayEventListener;
import com.oniz.ONIZGameHelper;
import com.oniz.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.gms.ShadowGooglePlayServicesUtil;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created by robin
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, shadows = {ShadowGooglePlayServicesUtil.class})
@RunWith(RobolectricGradleTestRunner.class)
public class GoogleGameServicesTest {
    private MockActivity activity;
    private ONIZGameHelper onizGameHelper;

    // @Before => JUnit 4 annotation that specifies this method should run before each test is run
    // Useful to do setup for objects that are needed in the test
    @Before
    public void setup() {
        // Convenience method to run MainActivity through the Activity Lifecycle methods:
        // onCreate(...) => onStart() => onPostCreate(...) => onResume()
        activity = Robolectric.setupActivity(MockActivity.class);
    }

    // @Test => JUnit 4 annotation specifying this is a test to be run
    // The test simply checks that our activity exists
    @Test
    public void validateActivityExists() {
        assertNotNull("AndroidLauncher is null", activity);
    }

    @Test
    public void canCreateONIZGameHelper() {
        onizGameHelper = new ONIZGameHelper(activity, GameHelper.CLIENT_GAMES);
        assertTrue(onizGameHelper != null);
    }

    @Test
    public void cannotLeaveGameWhenRoomIsNull() {
        onizGameHelper = new ONIZGameHelper(activity, GameHelper.CLIENT_GAMES);
        assertTrue(onizGameHelper != null);
        assertFalse("Room should be null by default", onizGameHelper.canLeaveRoom());
        assertFalse("You are not supposed to be able to leave when room is null", onizGameHelper.leaveGame());
    }

    @Test
    public void canAddAndRemovePlayEventListeners() {
        onizGameHelper = new ONIZGameHelper(activity, GameHelper.CLIENT_GAMES);
        assertTrue(onizGameHelper != null);
        PlayEventListener pel = new PlayEventListener() {
            @Override
            public void leftRoom() {

            }

            @Override
            public void disconnected() {

            }
        };

        try {
            onizGameHelper.addPlayEventListener(pel);
            onizGameHelper.removePlayEventListener(pel);
        } catch (Exception ex) {
            fail("failed to add and remove listeners");
        }

    }

    @Test
    public void loginFlow() {
        onizGameHelper = new ONIZGameHelper(activity, GameHelper.CLIENT_GAMES);
        assertTrue(onizGameHelper != null);
        try {
            onizGameHelper.getApiClient();
            fail("Should fail with IllegalStateException"); //expect exception to be thrown
        } catch (IllegalStateException ex) {
            assertEquals("Should fail with IllegalStateException", new IllegalStateException().getClass().getSimpleName(), ex.getClass().getSimpleName());
        }



        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
            @Override
            public void onSignInFailed() {
                System.out.println("failed to sign in");
                assertTrue(true);
            }

            @Override
            public void onSignInSucceeded() {
                System.out.println("success to sign in");
                assertTrue(true);
            }

        };

        onizGameHelper.setup(gameHelperListener);

        try {
            assertNotNull(onizGameHelper.getApiClient());
        } catch (IllegalStateException ex) {
            fail("No exception should be thrown");
        }

        assertFalse("Should not be connecting", onizGameHelper.isConnecting());
        onizGameHelper.beginUserInitiatedSignIn();
        assertTrue("Should be connecting", onizGameHelper.isConnecting());
    }


}