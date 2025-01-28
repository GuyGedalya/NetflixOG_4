package com.example.nog.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nog.Adapters.MovieAdapter;
import com.example.nog.ObjectClasses.Movie;
import com.example.nog.R;

import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        TextView noMoviesText = findViewById(R.id.no_movies_text);
        // Set 3 columns to display the movies:
        int numberOfColumns = 3;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);
        recyclerView.setLayoutManager(gridLayoutManager);

        //Displaying the movies:
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) getIntent().getSerializableExtra("movies");
        if (movies == null || movies.isEmpty()) {
            noMoviesText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            MovieAdapter adapter = new MovieAdapter(movies);
            recyclerView.setAdapter(adapter);
        }
    }
}

