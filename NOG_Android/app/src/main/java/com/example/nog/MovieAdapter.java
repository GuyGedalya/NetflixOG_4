package com.example.nog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nog.MovieDetailsDialogFragment.MovieDetailsDialogFragment;
import com.example.nog_android.ApiClient;
import com.example.nog_android.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final List<Movie> movies;

    public MovieAdapter(List<Movie> movies) {
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

        // קביעת שם הסרט
        holder.movieTitle.setText(movie.getTitle());

        // יצירת URL מלא לתמונה
        String fullImageUrl = ApiClient.getFullMovieUrl(movie.getImagePath());

        // טעינת התמונה באמצעות Glide
        Glide.with(holder.itemView.getContext())
                .load(fullImageUrl)
                .placeholder(R.drawable.logo)
                .into(holder.movieImage);

        // מאזין לחיצה על תמונה
        holder.movieImage.setOnClickListener(v -> {
            // פתיחת פרטי הסרט ב-DialogFragment
            MovieDetailsDialogFragment dialogFragment = MovieDetailsDialogFragment.newInstance(movie);
            dialogFragment.show(((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager(), "MovieDetailsDialog");
        });
    }



    @Override
    public int getItemCount() {
        return movies.size();
    }


    public void setMovies(List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView movieImage;
        TextView movieTitle;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.movie_image);
            movieTitle = itemView.findViewById(R.id.movie_title);
        }
    }
}
