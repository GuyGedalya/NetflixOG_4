package com.example.nog.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.example.nog.ObjectClasses.Movie;
import com.example.nog.ObjectClasses.TokenManager;
import com.example.nog.R;
import com.example.nog.connectionClasses.ApiClient;
import com.example.nog.connectionClasses.ApiService;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseActivity extends AppCompatActivity {
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_custom);
        }

        SearchView searchView = findViewById(R.id.search_view);
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        // Set text color
        searchEditText.setTextColor(ContextCompat.getColor(this, android.R.color.white));


        // Set a listener for text submission
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    // Call API with the search query
                    fetchMovies(query);

                    // Clear focus to hide the keyboard
                    searchView.clearFocus();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        // Set up DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigation_view);

        // Call the isManager function to check user role
        TokenManager.getInstance().isAdmin(isAdmin -> {
            // Show the "Manager" menu item if the user is a manager
            // Hide the "Manager" menu item if the user is not a manager
            navigationView.getMenu().findItem(R.id.nav_manager).setVisible(isAdmin);
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomePageActivity.class));
            } else if (id == R.id.nav_categories) {
                startActivity(new Intent(this, CategoryPageActivity.class));
            } else if (id == R.id.nav_manager) {
                startActivity(new Intent(this, ManagerActivity.class));
            } else if (id == R.id.nav_logout) {

                TokenManager.getInstance().clearToken();


                Intent intent = new Intent(this, MainActivity.class);
                // Prevent going back:
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }else if (id == R.id.nav_mode) { // Option for toggling mode
                int currentMode = AppCompatDelegate.getDefaultNightMode();

                if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
                    // Switch to Light Mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    // Switch to Dark Mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }

                // Restart the activity to apply the theme change
                recreate();
            }
            drawerLayout.closeDrawers();
            return true;
        });


        // Load unique content into the content_frame
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutResource(), findViewById(R.id.content_frame));
    }

    private void fetchMovies(String query) {
        ApiService apiService = ApiClient.getApiService();
        String token = TokenManager.getInstance().getToken();

        // Log the token and query for debugging
        Log.d("API Debug", "Token: " + token);
        Log.d("API Debug", "Query: " + query);

        apiService.query(query, "Bearer " + token).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Movie>> call, @NonNull Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API Response", response.body().toString());
                    List<Movie> movies = response.body();
                    openSearchResultsActivity(movies);
                } else {
                    openSearchResultsActivity(Collections.emptyList());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Movie>> call, @NonNull Throwable t) {
                Log.e("API Debug", "Request failed: ", t);
            }
        });
    }

    private void openSearchResultsActivity(List<Movie> movies) {
        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra("movies", (Serializable) movies);
        startActivity(intent);
    }


    // This method must be overridden by child classes to provide their layout:
    @LayoutRes
    protected abstract int getLayoutResource();
}
