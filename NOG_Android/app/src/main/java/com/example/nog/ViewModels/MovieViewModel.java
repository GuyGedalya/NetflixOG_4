package com.example.nog.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.nog_android.Movie;
import com.example.nog.Repositories.MovieRepository;

import java.util.List;
import java.util.Map;

public class MovieViewModel extends ViewModel {
    private final MovieRepository repository;

    public MovieViewModel(MovieRepository repository) {
        this.repository = repository;
    }

    public LiveData<Map<String, List<Movie>>> getCategories() {
        return repository.getPromotedMovies();
    }

    public LiveData<Map<String, List<Movie>>> getAllCategories() {
        return repository.getAllMovies();
    }

}
