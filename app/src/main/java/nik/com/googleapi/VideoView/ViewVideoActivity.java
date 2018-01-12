package nik.com.googleapi.VideoView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import nik.com.googleapi.R;

public class ViewVideoActivity extends AppCompatActivity {


    private String filename;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        filename = extras.getString("videofilename");

        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoPath(filename);
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
        videoView.start();

    }
}
