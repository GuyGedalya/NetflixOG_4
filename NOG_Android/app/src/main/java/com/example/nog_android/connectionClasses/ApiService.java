package com.example.nog_android.connectionClasses;

import com.example.nog_android.ObjectClasses.Movie;
import com.example.nog_android.ObjectClasses.TokenManager;
import com.example.nog_android.ObjectClasses.User;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface ApiService {
    @POST("users")
    @Headers("Content-Type: application/json")
    Call<Void> addUser(@Body User user);

    @POST("tokens")
    @Headers("Content-Type: application/json")
    Call<TokenManager> logIn(@Body LoginRequest loginRequest);

    @GET("movies")
    Call<Map<String,List<Movie>>> getPromotedMovies(@Header("Authorization") String token);

    @GET("{Path}")
    @Streaming
    Call<ResponseBody> downloadFile(@Header("Authorization") String token, @Path("Path") String pathToFile);


    @GET("movies/{id}")
    Call<Movie> getMovieById(@Path("id") String movieId, @Header("Authorization") String token);

    @GET("movies/categories")
    Call<Map<String,List<Movie>>> getAllMovies(@Header("Authorization") String token);

    @GET("movies/{id}/recommend")
    Call<List<Movie>> getRecommendations(@Path("id") String movieId, @Header("Authorization") String token);

    @POST("movies/{id}/recommend")
    Call<Void> addAsWatched(@Path("id") String movieId, @Header("Authorization") String token, String userId);
}
