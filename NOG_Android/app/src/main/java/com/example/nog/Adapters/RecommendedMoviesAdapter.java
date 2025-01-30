package com.example.nog.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nog.ObjectClasses.Movie;
import com.example.nog.R;
import com.example.nog.connectionClasses.ApiClient;

import java.util.List;

public class RecommendedMoviesAdapter extends RecyclerView.Adapter<RecommendedMoviesAdapter.MovieViewHolder> {

    private List<Movie> movies;

    // Constructor
    public RecommendedMoviesAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        // Set movie title and image
        holder.titleTextView.setText(movie.getTitle());
        String fullImageUrl = ApiClient.getFullMovieUrl(movie.getImagePath());
        Glide.with(holder.imageView.getContext()).load(fullImageUrl).into(holder.imageView);

        // Disable click listener or set a placeholder action
        holder.itemView.setOnClickListener(null); // No action on click
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    // ViewHolder class
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movie_image);
            titleTextView = itemView.findViewById(R.id.movie_title);
        }
    }

    // Update movie list method (optional)
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}
