package com.example.nog.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.nog.R;
import com.example.nog.ObjectClasses.Movie;

import java.util.List;
import java.util.Map;

public class CategoryMovieAdapter extends RecyclerView.Adapter<CategoryMovieAdapter.CategoryViewHolder> {

    private final LayoutInflater mInflater;
    private Map<String, List<Movie>> categories;


    public CategoryMovieAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String categoryName = (String) categories.keySet().toArray()[position];
        List<Movie> movies = categories.get(categoryName);

        holder.categoryName.setText(categoryName);

        MovieAdapter movieAdapter = new MovieAdapter(movies);
        holder.moviesList.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.moviesList.setAdapter(movieAdapter);

    }

    public int getItemCount() {
        if (categories != null){
            return categories.size();
        }
        return 0;
    }

    public void setCategories(Map<String, List<Movie>> newCategories) {
        categories = newCategories;
        notifyDataSetChanged();
    }

    public Map<String, List<Movie>> getCategories() { return categories;}

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        RecyclerView moviesList;

        @SuppressLint("WrongViewCast")
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            moviesList = itemView.findViewById(R.id.movies_list); // ודא שה-ID הזה תואם ל-RecyclerView ב-layout
        }
    }



}
