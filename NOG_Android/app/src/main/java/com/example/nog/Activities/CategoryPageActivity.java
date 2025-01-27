package com.example.nog.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nog.Adapters.CategoryMovieAdapter;
import com.example.nog.R;
import com.example.nog.Repositories.MovieRepository;
import com.example.nog.ViewModels.MovieViewModelAll;
import com.example.nog.ViewModels.MovieViewModelAllFactory;
import com.example.nog.connectionClasses.ApiClient;
import com.example.nog.connectionClasses.AppDB;
import com.example.nog.connectionClasses.LoginRequest;
import com.example.nog.ObjectClasses.Movie;
import com.example.nog.ObjectClasses.TokenManager;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryPageActivity extends AppCompatActivity {
    private CategoryMovieAdapter adapter;
    private MovieViewModelAll movieViewModelAll;

    protected RecyclerView recyclerView;

    protected int getLayoutResource() {
        return R.layout.activity_category;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        // Initialize Room database and repository
        AppDB database = MyDataBase.getInstance(this);
        MovieRepository repository = new MovieRepository(database.movieDao());


        // Initialize ViewModel
        MovieViewModelAllFactory factory = new MovieViewModelAllFactory(repository);
        movieViewModelAll = new ViewModelProvider(this, factory).get(MovieViewModelAll.class);

        // Set up RecyclerView and adapter
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new CategoryMovieAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCategories(null);
        loadData();
    }

    protected void loadData() {
        // Login credentials for testing
        String testUserName = "guy";
        String testPassword = "g12345678";

        // API call to log in and retrieve token
        ApiClient.getApiService().logIn(new LoginRequest(testUserName, testPassword)).enqueue(new Callback<TokenManager>() {
            @Override
            public void onResponse(Call<TokenManager> call, Response<TokenManager> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Save token using singleton TokenManager
                    TokenManager.getInstance().setToken(response.body().getToken());

                    // Fetch categories and update UI
                    movieViewModelAll.getAllCategories().observe(CategoryPageActivity.this, categories -> {
                        if (categories != null) {
                            updateUI(categories);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<TokenManager> call, Throwable t) {
                // Handle token creation failure
            }
        });
    }

    private void refreshCategories() {
        // Observe category data changes
        movieViewModelAll.refreshCategories();
    }

    private void updateUI(Map<String, List<Movie>> categories) {
        adapter.setCategories(categories);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCategories();
    }
}
