package com.oniz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by robin on 20/4/16.
 */
public class VideoActivity extends Activity {

    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);
//        addListenerOnButton();
    }

    @Override
    protected void onStart() {
        super.onStart();

        final VideoView videoView =
                (VideoView) findViewById(R.id.videoView);

        videoView.setMediaController(new MediaController(this));

        String uri = "android.resource://" + getPackageName() + "/" + R.raw.oniz_demo_video2;

        videoView.setVideoURI(Uri.parse(uri));

        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.start();

    }

//    public void addListenerOnButton() {
//
//        final Context context = this;
//
//        button = (Button) findViewById(R.id.button1);
//
//
//    }

}