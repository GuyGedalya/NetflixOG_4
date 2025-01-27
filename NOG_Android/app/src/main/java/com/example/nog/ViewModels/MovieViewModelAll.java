package com.example.nog.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.nog.ObjectClasses.Movie;
import com.example.nog.Repositories.MovieRepository;

import java.util.List;
import java.util.Map;

public class MovieViewModelAll extends ViewModel {
    private final MovieRepository repository;
    private LiveData<Map<String, List<Movie>>> categories;


    public MovieViewModelAll(MovieRepository repository) {
        this.repository = repository;
        this.categories = repository.getAllMovies();
    }

    public LiveData<Map<String, List<Movie>>> getAllCategories() {
        this.categories = repository.getAllMovies();
        return this.categories;
    }

    public void refreshCategories() {
        this.categories = repository.getAllMovies();
    }

}
