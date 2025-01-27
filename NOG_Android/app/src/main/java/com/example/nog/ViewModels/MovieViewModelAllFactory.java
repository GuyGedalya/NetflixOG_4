package com.example.nog.ViewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.nog.Repositories.MovieRepository;

public class MovieViewModelAllFactory implements ViewModelProvider.Factory {
    private final MovieRepository repository;

    public MovieViewModelAllFactory(MovieRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MovieViewModelAll.class)) {
            return (T) new MovieViewModelAll(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

