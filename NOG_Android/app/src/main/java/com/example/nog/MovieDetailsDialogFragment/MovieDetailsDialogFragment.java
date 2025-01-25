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

import com.bumptech.glide.Glide;
import com.example.nog.R;
import com.example.nog.VideoPlayerActivity.VideoPlayerActivity;
import com.example.nog_android.Movie;
import com.example.nog_android.ApiClient;


public class MovieDetailsDialogFragment extends DialogFragment {
    private static final String ARG_MOVIE = "movie";

    private Movie movie;

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

        // Load image
        Glide.with(requireContext()).load(movie.getImagePath()).into(imageView);

        // Set title and release date
        titleTextView.setText(movie.getTitle());
        releaseDateTextView.setText("Release Date: " + movie.getReleaseDate());

        // Play button click listener
        playButton.setOnClickListener(v -> {
            // Play the movie
            VideoPlayerActivity.start(requireContext(), ApiClient.getFullMovieUrl(movie.getFilmPath()));
            dismiss();
        });

        return view;
    }
}

