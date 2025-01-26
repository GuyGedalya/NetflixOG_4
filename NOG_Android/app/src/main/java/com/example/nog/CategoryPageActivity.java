package com.example.nog;

import android.os.Bundle;
import android.util.Log;
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

        // יצירת ה-Repository
        AppDB database = Room.databaseBuilder(getApplicationContext(), AppDB.class, "my_database").build();

        MovieRepository repository = new MovieRepository(database.movieDao());
        MovieViewModelFactory factory = new MovieViewModelFactory(repository);
        movieViewModel = new ViewModelProvider(this, factory).get(MovieViewModel.class);

        recyclerView = findViewById(R.id.recycler_view);
        adapter = new CategoryMovieAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCategories(null);

        observeViewModel();
        loadData();

    }

    protected void loadData() {
        // פרטי משתמש קיים
        String testUserName = "guy";
        String testPassword = "g12345678";

        // קריאה ל-API לקבלת הטוקן
        ApiClient.getApiService().logIn(new LoginRequest(testUserName, testPassword)).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // שמירת הטוקן בסינגלטון
                    TokenManager.getInstance().setToken(response.body().getToken());
                    Log.d("HomePageActivity", "Token created successfully: " + response.body().getToken());

                    // המשך תהליך טעינת הקטגוריות
                    movieViewModel.getAllCategories().observe(CategoryPageActivity.this, categories -> {
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
        movieViewModel.getAllCategories().observe(this, categories -> {
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
    }


    @Override
    protected void onResume() {
        Log.d("HomePageActivity", "onResume");
        super.onResume();
        observeViewModel();
    }

}
