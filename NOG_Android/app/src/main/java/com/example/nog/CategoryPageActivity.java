package com.example.nog;

import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
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

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryPageActivity extends AppCompatActivity {
    private CategoryMovieAdapter adapter;
    private MovieViewModel movieViewModel;

    protected VideoView videoView;
    protected RecyclerView recyclerView;

    protected int getLayoutResource() {
        return R.layout.activity_category;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        // Initialize Room database and repository
        AppDB database = Room.databaseBuilder(getApplicationContext(), AppDB.class, "my_database").build();
        MovieRepository repository = new MovieRepository(database.movieDao());

        // Initialize ViewModel
        MovieViewModelFactory factory = new MovieViewModelFactory(repository);
        movieViewModel = new ViewModelProvider(this, factory).get(MovieViewModel.class);

        // Set up RecyclerView and adapter
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new CategoryMovieAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCategories(null);

        observeViewModel();
        loadData();
    }

    protected void loadData() {
        // Login credentials for testing
        String testUserName = "guy";
        String testPassword = "g12345678";

        // API call to log in and retrieve token
        ApiClient.getApiService().logIn(new LoginRequest(testUserName, testPassword)).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Save token using singleton TokenManager
                    TokenManager.getInstance().setToken(response.body().getToken());

                    // Fetch categories and update UI
                    movieViewModel.getAllCategories().observe(CategoryPageActivity.this, categories -> {
                        if (categories != null) {
                            updateUI(categories);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                // Handle token creation failure
            }
        });
    }

    private void observeViewModel() {
        // Observe category data changes
        movieViewModel.getAllCategories().observe(this, categories -> {
            if (categories != null) {
                updateUI(categories);
            }
        });
    }

    private void updateUI(Map<String, List<Movie>> categories) {
        // Update RecyclerView with category data
        if (categories == null || categories.isEmpty()) {
            return;
        }
        if (adapter != null)
            adapter.setCategories(categories);
    }

    @Override
    protected void onResume() {
        super.onResume();
        observeViewModel();
    }
}
