package com.example.nog.Repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nog_android.ApiClient;
import com.example.nog_android.ApiService;
import com.example.nog_android.Movie;
import com.example.nog_android.MovieDao;
import com.example.nog_android.Token.TokenManager;

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
    // API service instance for making network calls
    private final ApiService apiService;

    // DAO instance for database operations
    private final MovieDao movieDao;

    // Executor service for background tasks
    private final ExecutorService executorService;

    // Constructor initializes API service, DAO, and executor service
    public MovieRepository(MovieDao movieDao) {
        this.apiService = ApiClient.getApiService();
        this.movieDao = movieDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    // Fetches promoted movies either from the API or local database
    public LiveData<Map<String, List<Movie>>> getPromotedMovies() {
        MutableLiveData<Map<String, List<Movie>>> moviesLiveData = new MutableLiveData<>();

        // Retrieve the token for API authentication
        String token = TokenManager.getInstance().getToken();

        if (token == null) {
            // If no token is available, fetch movies from the local database
            executorService.execute(() -> {
                List<Movie> localMovies = movieDao.index();
                moviesLiveData.postValue(groupMovies(localMovies));
            });
            return moviesLiveData;
        }

        // Make a network request to fetch promoted movies
        apiService.getPromotedMovies("Bearer " + token).enqueue(new Callback<Map<String, List<Movie>>>() {
            @Override
            public void onResponse(Call<Map<String, List<Movie>>> call, Response<Map<String, List<Movie>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Save movies into the database and update LiveData
                    executorService.execute(() -> {
                        List<Movie> movies = flattenMovies(response.body());
                        movieDao.insert(movies.toArray(new Movie[0]));
                        moviesLiveData.postValue(response.body());
                    });
                } else {
                    // On API failure, fetch movies from the database
                    executorService.execute(() -> {
                        List<Movie> localMovies = movieDao.index();
                        moviesLiveData.postValue(groupMovies(localMovies));
                    });
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<Movie>>> call, Throwable t) {
                // On network failure, fetch movies from the database
                executorService.execute(() -> {
                    List<Movie> localMovies = movieDao.index();
                    moviesLiveData.postValue(groupMovies(localMovies));
                });
            }
        });

        return moviesLiveData;
    }

    // Fetches all movies either from the API or local database
    public LiveData<Map<String, List<Movie>>> getAllMovies() {
        MutableLiveData<Map<String, List<Movie>>> moviesLiveData = new MutableLiveData<>();

        // Retrieve the token for API authentication
        String token = TokenManager.getInstance().getToken();

        if (token == null) {
            // If no token is available, fetch movies from the local database
            executorService.execute(() -> {
                List<Movie> localMovies = movieDao.index();
                moviesLiveData.postValue(groupMovies(localMovies));
            });
            return moviesLiveData;
        }

        // Make a network request to fetch all movies
        apiService.getAllMovies("Bearer " + token).enqueue(new Callback<Map<String, List<Movie>>>() {
            @Override
            public void onResponse(Call<Map<String, List<Movie>>> call, Response<Map<String, List<Movie>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Save movies into the database and update LiveData
                    executorService.execute(() -> {
                        List<Movie> movies = flattenMovies(response.body());
                        movieDao.insert(movies.toArray(new Movie[0]));
                        moviesLiveData.postValue(response.body());
                    });
                } else {
                    // On API failure, fetch movies from the database
                    executorService.execute(() -> {
                        List<Movie> localMovies = movieDao.index();
                        moviesLiveData.postValue(groupMovies(localMovies));
                    });
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<Movie>>> call, Throwable t) {
                // On network failure, fetch movies from the database
                executorService.execute(() -> {
                    List<Movie> localMovies = movieDao.index();
                    moviesLiveData.postValue(groupMovies(localMovies));
                });
            }
        });

        return moviesLiveData;
    }

    // Flattens a grouped map of movies into a single list
    private List<Movie> flattenMovies(Map<String, List<Movie>> groupedMovies) {
        List<Movie> movies = new ArrayList<>();
        for (List<Movie> movieList : groupedMovies.values()) {
            movies.addAll(movieList);
        }
        return movies;
    }

    // Groups a list of movies by the first category name
    private Map<String, List<Movie>> groupMovies(List<Movie> movies) {
        return movies.stream().collect(Collectors.groupingBy(movie -> movie.getCategories().get(0).getName()));
    }
}
