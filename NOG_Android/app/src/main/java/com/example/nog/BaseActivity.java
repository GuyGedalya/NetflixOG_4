package com.example.nog;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;


public abstract class BaseActivity extends AppCompatActivity {

    protected VideoView videoView;
    protected RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("BasePageActivity", "Done onCreate.");
        setContentView(getLayoutResource());
        Log.e("BasePageActivity", "Done setContentView.");

        // Initialize VideoView and toggle button
        videoView = findViewById(R.id.video_view);
        videoView.setVisibility(View.GONE); // Hide video by default
        Log.e("BasePageActivity", "Done videoView.");

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        if (recyclerView == null) {
            Log.e("BasePageActivity", "RecyclerView is null. Check XML ID or Layout resource.");
        } else {
            Log.d("BasePageActivity", "RecyclerView initialized successfully.");
        }
        Log.e("BasePageActivity", "Done recyclerView.");
    }

    // Abstract methods to be implemented by child activities
    protected abstract int getLayoutResource();
    protected abstract void loadData();

    // Play video
    protected void playVideo() {
        videoView.setVisibility(View.VISIBLE);
        videoView.start();
    }

    // Stop video
    protected void stopVideo() {
        videoView.setVisibility(View.GONE);
        videoView.stopPlayback();
    }
}
