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
    private final ApiService apiService;
    private final MovieDao movieDao;
    private final ExecutorService executorService;

    public MovieRepository(MovieDao movieDao) {
        this.apiService = ApiClient.getApiService();
        this.movieDao = movieDao;
        this.executorService = Executors.newSingleThreadExecutor(); // Executor for background tasks
    }

    public LiveData<Map<String, List<Movie>>> getPromotedMovies() {
        MutableLiveData<Map<String, List<Movie>>> moviesLiveData = new MutableLiveData<>();

        // Getting the token:
        String token = TokenManager.getInstance().getToken();

        if (token == null) {
            // If token is null, fetch from the database
            executorService.execute(() -> {
                List<Movie> localMovies = movieDao.index();
                moviesLiveData.postValue(groupMovies(localMovies));
            });
            return moviesLiveData;
        }

        // Call for the API with the Token:
        apiService.getPromotedMovies("Bearer " + token).enqueue(new Callback<Map<String, List<Movie>>>() {
            @Override
            public void onResponse(Call<Map<String, List<Movie>>> call, Response<Map<String, List<Movie>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Saving the data into ROOM in the background
                    executorService.execute(() -> {
                        List<Movie> movies = flattenMovies(response.body());
                        movieDao.insert(movies.toArray(new Movie[0]));
                        moviesLiveData.postValue(response.body()); // Update LiveData
                    });
                } else {
                    // Fetching data from the database on API failure
                    executorService.execute(() -> {
                        List<Movie> localMovies = movieDao.index();
                        moviesLiveData.postValue(groupMovies(localMovies));
                    });
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<Movie>>> call, Throwable t) {
                // Fetching data from the database on API failure
                executorService.execute(() -> {
                    List<Movie> localMovies = movieDao.index();
                    moviesLiveData.postValue(groupMovies(localMovies));
                });
            }
        });

        return moviesLiveData;
    }

    public LiveData<Map<String, List<Movie>>> getAllMovies() {
        MutableLiveData<Map<String, List<Movie>>> moviesLiveData = new MutableLiveData<>();

        // Getting the token:
        String token = TokenManager.getInstance().getToken();

        if (token == null) {
            // If token is null, fetch from the database
            executorService.execute(() -> {
                List<Movie> localMovies = movieDao.index();
                moviesLiveData.postValue(groupMovies(localMovies));
            });
            return moviesLiveData;
        }

        // Call for the API with the Token:
        apiService.getAllMovies("Bearer " + token).enqueue(new Callback<Map<String, List<Movie>>>() {
            @Override
            public void onResponse(Call<Map<String, List<Movie>>> call, Response<Map<String, List<Movie>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Saving the data into ROOM in the background
                    executorService.execute(() -> {
                        List<Movie> movies = flattenMovies(response.body());
                        movieDao.insert(movies.toArray(new Movie[0]));
                        moviesLiveData.postValue(response.body()); // Update LiveData
                    });
                } else {
                    // Fetching data from the database on API failure
                    executorService.execute(() -> {
                        List<Movie> localMovies = movieDao.index();
                        moviesLiveData.postValue(groupMovies(localMovies));
                    });
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<Movie>>> call, Throwable t) {
                // Fetching data from the database on API failure
                executorService.execute(() -> {
                    List<Movie> localMovies = movieDao.index();
                    moviesLiveData.postValue(groupMovies(localMovies));
                });
            }
        });

        return moviesLiveData;
    }


    private List<Movie> flattenMovies(Map<String, List<Movie>> groupedMovies) {
        List<Movie> movies = new ArrayList<>();
        for (List<Movie> movieList : groupedMovies.values()) {
            movies.addAll(movieList);
        }
        return movies;
    }

    private Map<String, List<Movie>> groupMovies(List<Movie> movies) {
        return movies.stream().collect(Collectors.groupingBy(movie -> movie.getCategories().get(0).getName()));
    }
}
