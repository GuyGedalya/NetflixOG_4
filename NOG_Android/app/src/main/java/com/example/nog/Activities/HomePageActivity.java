package com.example.nog.Activities;

import android.os.Bundle;
import android.widget.VideoView;
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
import com.example.nog.ObjectClasses.Movie;
import java.util.List;
import java.util.Map;


public class HomePageActivity extends BaseActivity {
    private CategoryMovieAdapter adapter;
    private MovieViewModel movieViewModel;

    protected VideoView videoView;
    protected RecyclerView recyclerView;

    private Movie randomMovie;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        // Requesting the data:
        movieViewModel.getCategories().observe(HomePageActivity.this, categories -> {
            if (categories != null) {
                updateUI(categories);
            }});
    }

    // Refreshing the categories:
    private void refreshViewModel() {
        movieViewModel.refreshCategories();
    }

    // Update the UI and play random movie:
    private void updateUI(Map<String, List<Movie>> categories) {
        adapter.setCategories(categories);
        this.randomMovie = getRandomMovie(categories);
        displayRandomMovie(randomMovie);
    }

    // Displaying random movie:
    private void displayRandomMovie(Movie movie) {
        if (movie != null) {
            videoView.setVideoPath(ApiClient.getFullMovieUrl(movie.getFilmPath()));
            playVideo();
        }
    }

    // Choose random movie:
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
