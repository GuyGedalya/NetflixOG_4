package com.example.nog.MovieDetailsDialogFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.nog.Adapters.RecommendedMoviesAdapter;
import com.example.nog.R;
import com.example.nog.VideoPlayerActivity.VideoPlayerActivity;
import com.example.nog.ObjectClasses.Movie;
import com.example.nog.connectionClasses.ApiClient;
import com.example.nog.connectionClasses.ApiService;
import com.example.nog.ObjectClasses.TokenManager;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsDialogFragment extends DialogFragment {
    private static final String ARG_MOVIE = "movie";
    private Movie movie;
    private RecyclerView recommendedMoviesRecyclerView;
    private RecommendedMoviesAdapter moviesAdapter;
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

        // Initialize RecyclerView with a Grid 2 columns:
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        recommendedMoviesRecyclerView.setLayoutManager(gridLayoutManager);

        // Initialize the Adapter with an empty list
        moviesAdapter = new RecommendedMoviesAdapter(new ArrayList<>());
        recommendedMoviesRecyclerView.setAdapter(moviesAdapter);

        // Generate the full URL for the movie image and load it using Glide
        String fullImageUrl = ApiClient.getFullMovieUrl(movie.getImagePath());
        Glide.with(requireContext()).load(fullImageUrl).into(imageView);

        // Set the movie title and release date in the respective TextViews
        titleTextView.setText(movie.getTitle());
        String releaseDateText = getString(R.string.release_date, movie.getReleaseDate());
        releaseDateTextView.setText(releaseDateText);

        // Set a click listener for the play button
        playButton.setOnClickListener(v -> {
            // Start the video player activity with the movie URL and close the dialog
            VideoPlayerActivity.start(requireContext(), ApiClient.getFullMovieUrl(movie.getFilmPath()));
            addMovieToWatchlist(movie);
            dismiss();
        });

        // Fetch recommended movies for the selected movie
        fetchRecommendedMovies();
        return view;
    }
    private void addMovieToWatchlist(Movie movie) {
        // Get the token and from the TokenManager
        String token = TokenManager.getInstance().getToken();

        // Get the API service instance
        ApiService apiService = ApiClient.getApiService();

        // Make the network call to add the movie to the watchlist
        Call<Void> call = apiService.addAsWatched(movie.getMongoId(), "Bearer " + token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    // Log success
                    Log.d("MovieDetails", "Movie added to watchlist successfully.");
                } else {
                    // Handle API failure
                    Log.e("MovieDetails", "Failed to add movie to watchlist. Response code: " + response.code());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                // Handle network failure
                Log.e("MovieDetails", "Error adding movie to watchlist: " + t.getMessage());
            }
        });
    }

    private void fetchRecommendedMovies() {
        // Get the API service instance
        ApiService apiService = ApiClient.getApiService();
        Log.d("fetchRecommendedMovies", "API service initialized");

        // Retrieve the user's authentication token
        String token = TokenManager.getInstance().getToken();
        Log.d("fetchRecommendedMovies", "Token retrieved: " + (token != null ? "Valid token" : "Token is null"));

        // Make a network call to fetch movie recommendations
        Call<List<Movie>> call = apiService.getRecommendations(movie.getMongoId(), "Bearer " + token);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Movie>> call, @NonNull Response<List<Movie>> response) {

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
            public void onFailure(@NonNull Call<List<Movie>> call, @NonNull Throwable t) {
                // Handle network call failures
                showNoMoviesFoundMessage();
            }
        });
    }

    private void showNoMoviesFoundMessage() {
        // Create a TextView to display a "No movies found" message
        TextView noMoviesTextView = new TextView(requireContext());
        noMoviesTextView.setText(getString(R.string.no_movies_found));
        noMoviesTextView.setTextSize(16);
        noMoviesTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
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
