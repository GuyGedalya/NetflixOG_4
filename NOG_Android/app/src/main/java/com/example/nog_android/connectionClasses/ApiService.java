package com.example.nog_android.connectionClasses;

import com.example.nog_android.Entities.Movie;
import com.example.nog_android.Entities.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("users")
    @Headers("Content-Type: application/json")
    Call<Void> addUser(@Body User user);

    @POST("tokens")
    @Headers("Content-Type: application/json")
    Call<TokenResponse> logIn(@Body LoginRequest loginRequest);

    @GET("movies")
    Call<Map<String,List<Movie>>> getPromotedMovies(@Header("Authorization") String token);

    @GET("movies/{id}")
    Call<Movie> getMovieById(@Path("id") String movieId, @Header("Authorization") String token);

    @GET("movies/categories")
    Call<Map<String,List<Movie>>> getAllMovies(@Header("Authorization") String token);

    @GET("movies/{id}/recommend")
    Call<List<Movie>> getRecommendations(@Path("id") String movieId, @Header("Authorization") String token);
}
