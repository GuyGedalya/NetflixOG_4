package com.example.nog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.nog.Repositories.MovieRepository;
import com.example.nog.ViewModels.MovieViewModel;
import com.example.nog.ViewModels.MovieViewModelFactory;
import com.example.nog_android.ApiClient;
import com.example.nog_android.AppDB;
import com.example.nog_android.LoginRequest;
import com.example.nog_android.Movie;
import com.example.nog_android.Token.TokenManager;
import com.example.nog_android.TokenResponse;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageActivity extends AppCompatActivity {
    private CategoryMovieAdapter adapter;
    private MovieViewModel movieViewModel;

    protected VideoView videoView;
    protected RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new NavigationMenuFragment())
                    .commit();
        }




        // Database and ViewModel setup
        AppDB database = Room.databaseBuilder(getApplicationContext(), AppDB.class, "my_database").build();
        MovieRepository repository = new MovieRepository(database.movieDao());
        MovieViewModelFactory factory = new MovieViewModelFactory(repository);
        movieViewModel = new ViewModelProvider(this, factory).get(MovieViewModel.class);

        // VideoView setup
        videoView = findViewById(R.id.video_view);
        videoView.setVisibility(View.GONE);

        // RecyclerView setup
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new CategoryMovieAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCategories(null);

        observeViewModel();
        loadData();
    }

    protected void loadData() {
        String testUserName = "guy";
        String testPassword = "g12345678";

        ApiClient.getApiService().logIn(new LoginRequest(testUserName, testPassword)).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TokenManager.getInstance().setToken(response.body().getToken());
                    Log.d("HomePageActivity", "Token created successfully: " + response.body().getToken());

                    movieViewModel.getCategories().observe(HomePageActivity.this, categories -> {
                        if (categories != null) {
                            updateUI(categories);
                        } else {
                            Log.e("HomePageActivity", "Failed to fetch categories.");
                        }
                    });
                } else {
                    Log.e("HomePageActivity", "Failed to create token. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.e("HomePageActivity", "Error creating token: " + t.getMessage());
            }
        });
    }

    private void observeViewModel() {
        Log.d("HomePageActivity", "observeViewModel");
        movieViewModel.getCategories().observe(this, categories -> {
            if (categories != null) {
                Log.d("HomePageActivity", "Categories fetched successfully");
                updateUI(categories);
            } else {
                Log.e("HomePageActivity", "Failed to fetch categories");
            }
        });
    }

    private void updateUI(Map<String, List<Movie>> categories) {
        if (categories == null || categories.isEmpty()) {
            Log.e("HomePageActivity", "Cannot update UI: categories are null or empty.");
            return;
        }
        if (adapter != null)
            adapter.setCategories(categories);
        Movie randomMovie = getRandomMovie(categories);
        if (randomMovie != null) {
            ProgressBar loadingSpinner = findViewById(R.id.loading_spinner);
            videoView.setVideoPath(ApiClient.getFullMovieUrl(randomMovie.getFilmPath()));
            videoView.setOnErrorListener((mp, what, extra) -> {
                loadingSpinner.setVisibility(View.GONE);
                return true;
            });

            playVideo();
        }
    }

    private Movie getRandomMovie(Map<String, List<Movie>> categories) {
        for (List<Movie> movies : categories.values()) {
            if (movies != null && !movies.isEmpty()) {
                return movies.get((int) (Math.random() * movies.size()));
            }
        }
        return null;
    }

    @Override
    protected void onResume() {
        Log.d("HomePageActivity", "onResume");
        super.onResume();
        observeViewModel();
    }

    protected void playVideo() {
        videoView.setVisibility(View.VISIBLE);
        videoView.start();
    }
}
