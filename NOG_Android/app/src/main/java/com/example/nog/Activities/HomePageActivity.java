package com.example.nog.Activities;

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

import com.example.nog.Adapters.CategoryMovieAdapter;
import com.example.nog.R;
import com.example.nog.Repositories.MovieRepository;
import com.example.nog.ViewModels.MovieViewModel;
import com.example.nog.ViewModels.MovieViewModelFactory;
import com.example.nog.connectionClasses.ApiClient;
import com.example.nog.connectionClasses.AppDB;
import com.example.nog.connectionClasses.LoginRequest;
import com.example.nog.ObjectClasses.Movie;
import com.example.nog.ObjectClasses.TokenManager;
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

    private Movie randomMovie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer setup
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomePageActivity.class));
            } else if (id == R.id.nav_categories) {
                startActivity(new Intent(this, CategoryPageActivity.class));
            } else if (id == R.id.nav_manager) {
            } else if (id == R.id.nav_search) {
            }
            drawerLayout.closeDrawers();
            return true;
        });


        // Database and ViewModel setup
        AppDB database = MyDataBase.getInstance(this);
        MovieRepository repository = new MovieRepository(database.movieDao());
        MovieViewModelFactory factory = new MovieViewModelFactory(repository);
        movieViewModel = new ViewModelProvider(this, factory).get(MovieViewModel.class);

        // VideoView setup
        videoView = findViewById(R.id.video_view1);

        // RecyclerView setup
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new CategoryMovieAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCategories(null);


        loadData();
    }

    protected void loadData() {
        String testUserName = "guy";
        String testPassword = "g12345678";

        ApiClient.getApiService().logIn(new LoginRequest(testUserName, testPassword)).enqueue(new Callback<TokenManager>() {
            @Override
            public void onResponse(Call<TokenManager> call, Response<TokenManager> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TokenManager.getInstance().setToken(response.body().getToken());
                    TokenManager.getInstance().setUser(response.body().getUser());
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
            public void onFailure(Call<TokenManager> call, Throwable t) {
                Log.e("HomePageActivity", "Error creating token: " + t.getMessage());
            }
        });
    }

    private void refreshViewModel() {
        movieViewModel.refreshCategories();
    }

    private void updateUI(Map<String, List<Movie>> categories) {
        adapter.setCategories(categories);
        this.randomMovie = getRandomMovie(categories);
        displayRandomMovie(randomMovie);
    }

    private void displayRandomMovie(Movie movie) {
        if (movie != null) {
            videoView.setVideoPath(ApiClient.getFullMovieUrl(movie.getFilmPath()));
            playVideo();
        } else {
            Log.e("HomePageActivity", "No movies available to display.");
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
        super.onResume();
        refreshViewModel();
        displayRandomMovie(this.randomMovie);
    }

    protected void playVideo() {
        videoView.start();
    }
}
