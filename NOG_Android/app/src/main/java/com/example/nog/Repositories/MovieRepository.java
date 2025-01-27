package com.example.nog.Repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nog.connectionClasses.ApiClient;
import com.example.nog.connectionClasses.ApiService;
import com.example.nog.ObjectClasses.Movie;
import com.example.nog.connectionClasses.MovieDao;
import com.example.nog.ObjectClasses.TokenManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private final MutableLiveData<Map<String, List<Movie>>> moviesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Map<String, List<Movie>>> allMoviesLiveData = new MutableLiveData<>();

    private final ApiService apiService;
    private final MovieDao movieDao;
    private final ExecutorService executorService;

    public MovieRepository(MovieDao movieDao) {
        this.apiService = ApiClient.getApiService();
        this.movieDao = movieDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<Map<String, List<Movie>>> getPromotedMovies() {
        fetchPromotedMovies();
        return moviesLiveData;
    }

    public LiveData<Map<String, List<Movie>>> getAllMovies() {
        fetchAllMovies();
        return allMoviesLiveData;
    }

    public void fetchAllMovies() {
        String token = TokenManager.getInstance().getToken();
        apiService.getAllMovies("Bearer " + token).enqueue(new Callback<Map<String, List<Movie>>>() {
            @Override
            public void onResponse(Call<Map<String, List<Movie>>> call, Response<Map<String, List<Movie>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executorService.execute(() -> {
                        List<Movie> movies = flattenMovies(response.body());
                        movieDao.insert(movies.toArray(new Movie[0]));
                        moviesLiveData.postValue(response.body());
                    });
                } else {
                    fetchAllFromDatabase();
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<Movie>>> call, Throwable t) {
                fetchFromDatabase();
            }
        });
    }

    private void fetchAllFromDatabase() {
        executorService.execute(() -> {
            List<Movie> localMovies = movieDao.index();
            allMoviesLiveData.postValue(groupMovies(localMovies));
        });
    }

    public void fetchPromotedMovies() {
        String token = TokenManager.getInstance().getToken();
        apiService.getPromotedMovies("Bearer " + token).enqueue(new Callback<Map<String, List<Movie>>>() {
            @Override
            public void onResponse(Call<Map<String, List<Movie>>> call, Response<Map<String, List<Movie>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executorService.execute(() -> {
                        List<Movie> movies = flattenMovies(response.body());
                        movieDao.insert(movies.toArray(new Movie[0]));
                        moviesLiveData.postValue(response.body());
                    });
                } else {
                    fetchFromDatabase();
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<Movie>>> call, Throwable t) {
                fetchFromDatabase();
            }
        });
    }

    private void fetchFromDatabase() {
        executorService.execute(() -> {
            List<Movie> localMovies = movieDao.index();
            moviesLiveData.postValue(groupMovies(localMovies));
        });
    }

    private List<Movie> flattenMovies(Map<String, List<Movie>> groupedMovies) {
        List<Movie> movies = new ArrayList<>();
        for (List<Movie> movieList : groupedMovies.values()) {
            movies.addAll(movieList);
        }
        return movies;
    }

    private Map<String, List<Movie>> groupMovies(List<Movie> movies) {
        return movies.stream()
                .collect(Collectors.groupingBy(movie -> movie.getCategories().get(0).getName()));
    }
}

