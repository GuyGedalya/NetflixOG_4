package com.example.nog.MovieDetailsDialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nog.MovieAdapter;
import com.example.nog.R;
import com.example.nog.VideoPlayerActivity.VideoPlayerActivity;
import com.example.nog_android.Movie;
import com.example.nog_android.ApiClient;
import com.example.nog_android.ApiService;
import com.example.nog_android.Token.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsDialogFragment extends DialogFragment {
    private static final String ARG_MOVIE = "movie";

    // Holds the movie data passed to the fragment
    private Movie movie;

    // RecyclerView for displaying recommended movies
    private RecyclerView recommendedMoviesRecyclerView;

    // Adapter for managing the list of recommended movies
    private MovieAdapter moviesAdapter;

    // Static method to create a new instance of the fragment with a specific movie
    public static MovieDetailsDialogFragment newInstance(Movie movie) {
        MovieDetailsDialogFragment fragment = new MovieDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the movie object from the fragment's arguments
        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable(ARG_MOVIE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the dialog layout
        View view = inflater.inflate(R.layout.dialog_movie_details, container, false);

        // Find views by their IDs
        ImageView imageView = view.findViewById(R.id.movie_image);
        TextView titleTextView = view.findViewById(R.id.movie_title);
        TextView releaseDateTextView = view.findViewById(R.id.movie_release_date);
        Button playButton = view.findViewById(R.id.play_button);
        recommendedMoviesRecyclerView = view.findViewById(R.id.recommended_movies_recycler_view);

        // Initialize RecyclerView with a linear layout manager
        recommendedMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize the MovieAdapter with an empty list
        moviesAdapter = new MovieAdapter(new ArrayList<>());
        recommendedMoviesRecyclerView.setAdapter(moviesAdapter);

        // Generate the full URL for the movie image and load it using Glide
        String fullImageUrl = ApiClient.getFullMovieUrl(movie.getImagePath());
        Glide.with(requireContext()).load(fullImageUrl).into(imageView);

        // Set the movie title and release date in the respective TextViews
        titleTextView.setText(movie.getTitle());
        releaseDateTextView.setText("Release Date: " + movie.getReleaseDate());

        // Set a click listener for the play button
        playButton.setOnClickListener(v -> {
            // Start the video player activity with the movie URL and close the dialog
            VideoPlayerActivity.start(requireContext(), ApiClient.getFullMovieUrl(movie.getFilmPath()));
            dismiss();
        });

        // Fetch recommended movies for the selected movie
        fetchRecommendedMovies();

        return view;
    }

    private void fetchRecommendedMovies() {
        // Get the API service instance
        ApiService apiService = ApiClient.getApiService();

        // Retrieve the user's authentication token
        String token = TokenManager.getInstance().getToken();

        // Make a network call to fetch movie recommendations
        Call<List<Movie>> call = apiService.getRecommendations(String.valueOf(movie.getId()), "Bearer " + token);
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                // If the response is successful and contains a body
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> recommendedMovies = response.body();
                    if (!recommendedMovies.isEmpty()) {
                        // Update the adapter with the recommended movies
                        moviesAdapter.setMovies(recommendedMovies);
                    } else {
                        // Show a message when no recommended movies are found
                        showNoMoviesFoundMessage();
                    }
                } else {
                    // Handle cases where the response is not successful
                    showNoMoviesFoundMessage();
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                // Handle network call failures
                showNoMoviesFoundMessage();
            }
        });
    }

    private void showNoMoviesFoundMessage() {
        // Create a TextView to display a "No movies found" message
        TextView noMoviesTextView = new TextView(requireContext());
        noMoviesTextView.setText("Not Found Any Related Movies");
        noMoviesTextView.setTextSize(16);
        noMoviesTextView.setTextColor(getResources().getColor(R.color.black));
        noMoviesTextView.setGravity(View.TEXT_ALIGNMENT_CENTER);

        // Hide the RecyclerView
        recommendedMoviesRecyclerView.setVisibility(View.GONE);

        // Add the TextView to the parent layout
        ViewGroup parent = (ViewGroup) recommendedMoviesRecyclerView.getParent();
        if (parent != null) {
            parent.addView(noMoviesTextView);
        }
    }
}
