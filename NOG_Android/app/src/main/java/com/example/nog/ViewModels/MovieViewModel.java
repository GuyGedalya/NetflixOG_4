package com.example.nog.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.nog.ObjectClasses.Movie;
import com.example.nog.Repositories.MovieRepository;

import java.util.List;
import java.util.Map;

public class MovieViewModel extends ViewModel {
    private final MovieRepository repository;
    private LiveData<Map<String, List<Movie>>> categories;


    public MovieViewModel(MovieRepository repository) {
        this.repository = repository;
        this.categories = repository.getPromotedMovies();
    }

    public LiveData<Map<String, List<Movie>>> getCategories() {
        this.categories = repository.getPromotedMovies();
        return this.categories;
    }

    public void refreshCategories() {
        this.categories = repository.getPromotedMovies();
    }

}
