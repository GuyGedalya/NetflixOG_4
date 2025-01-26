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

    private Movie movie;
    private RecyclerView recommendedMoviesRecyclerView;
    private MovieAdapter moviesAdapter;

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
        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable(ARG_MOVIE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_movie_details, container, false);

        ImageView imageView = view.findViewById(R.id.movie_image);
        TextView titleTextView = view.findViewById(R.id.movie_title);
        TextView releaseDateTextView = view.findViewById(R.id.movie_release_date);
        Button playButton = view.findViewById(R.id.play_button);
        recommendedMoviesRecyclerView = view.findViewById(R.id.recommended_movies_recycler_view);

        // Initialize RecyclerView
        recommendedMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        moviesAdapter = new MovieAdapter(new ArrayList<>());
        recommendedMoviesRecyclerView.setAdapter(moviesAdapter);

        // Create full URL for the image
        String fullImageUrl = ApiClient.getFullMovieUrl(movie.getImagePath());
        Glide.with(requireContext()).load(fullImageUrl).into(imageView);

        // Set title and release date
        titleTextView.setText(movie.getTitle());
        releaseDateTextView.setText("Release Date: " + movie.getReleaseDate());

        // Play button click listener
        playButton.setOnClickListener(v -> {
            VideoPlayerActivity.start(requireContext(), ApiClient.getFullMovieUrl(movie.getFilmPath()));
            dismiss();
        });

        // Fetch recommended movies
        fetchRecommendedMovies();

        return view;
    }

    private void fetchRecommendedMovies() {
        ApiService apiService = ApiClient.getApiService();
        String token = TokenManager.getInstance().getToken();

        Call<List<Movie>> call = apiService.getRecommendations(String.valueOf(movie.getId()), "Bearer " + token);
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> recommendedMovies = response.body();
                    if (!recommendedMovies.isEmpty()) {
                        moviesAdapter.setMovies(recommendedMovies);
                    } else {
                        showNoMoviesFoundMessage();
                    }
                } else {
                    showNoMoviesFoundMessage();
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                showNoMoviesFoundMessage();
            }
        });
    }

    private void showNoMoviesFoundMessage() {
        TextView noMoviesTextView = new TextView(requireContext());
        noMoviesTextView.setText("Not Found Any Related Movies");
        noMoviesTextView.setTextSize(16);
        noMoviesTextView.setTextColor(getResources().getColor(R.color.black));
        noMoviesTextView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        recommendedMoviesRecyclerView.setVisibility(View.GONE);

        ViewGroup parent = (ViewGroup) recommendedMoviesRecyclerView.getParent();
        if (parent != null) {
            parent.addView(noMoviesTextView);
        }
    }
}
