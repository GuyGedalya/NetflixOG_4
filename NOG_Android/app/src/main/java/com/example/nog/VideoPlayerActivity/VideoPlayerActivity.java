package com.example.nog.VideoPlayerActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nog.R;

public class VideoPlayerActivity extends AppCompatActivity {
    private static final String EXTRA_VIDEO_PATH = "video_path";


    // Static method to start the VideoPlayerActivity with the specified video path:
    public static void start(Context context, String videoPath) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra(EXTRA_VIDEO_PATH, videoPath);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        VideoView videoView = findViewById(R.id.video_view);

        // Retrieve the video path passed through the intent
        String videoPath = getIntent().getStringExtra(EXTRA_VIDEO_PATH);
        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.setMediaController(new MediaController(this));

        // Request focus for the VideoView
        videoView.requestFocus();
        videoView.start();
    }
}
