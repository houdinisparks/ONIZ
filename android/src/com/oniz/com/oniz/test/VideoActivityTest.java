package com.oniz.com.oniz.test;

import android.os.Build;
import android.widget.VideoView;

import com.oniz.BuildConfig;
import com.oniz.R;
import com.oniz.VideoActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by robin
 */

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class VideoActivityTest {
    private VideoActivity activity;

    // @Before => JUnit 4 annotation that specifies this method should run before each test is run
    // Useful to do setup for objects that are needed in the test
    @Before
    public void setup() {
        // Convenience method to run MainActivity through the Activity Lifecycle methods:
        // onCreate(...) => onStart() => onPostCreate(...) => onResume()
        activity = Robolectric.setupActivity(VideoActivity.class);
    }

    // @Test => JUnit 4 annotation specifying this is a test to be run
    // The test simply checks that our VideoView exists
    @Test
    public void validateVideoViewContent() {
        VideoView videoView = (VideoView) activity.findViewById(R.id.videoView);
        assertNotNull("Video view could not be found", videoView);
    }

    @Test
    public void validateVideoViewIsPlaying() {
        VideoView videoView = (VideoView) activity.findViewById(R.id.videoView);
        assertTrue("Video view is not playing", videoView.isPlaying());
    }
}
