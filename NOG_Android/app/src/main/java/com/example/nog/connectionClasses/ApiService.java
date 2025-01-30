package com.example.nog.connectionClasses;

import com.example.nog.ObjectClasses.Category;
import com.example.nog.ObjectClasses.Movie;
import com.example.nog.ObjectClasses.TokenManager;


import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    @Multipart
    @POST("users")
    Call<Void> addUser(
            @Part("UserName") RequestBody userName,
            @Part("Email") RequestBody email,
            @Part("Password") RequestBody password,
            @Part("Phone") RequestBody phone,
            @Part MultipartBody.Part profileImage
    );

    @POST("tokens")
    @Headers("Content-Type: application/json")
    Call<TokenManager> logIn(@Body LoginRequest loginRequest);

    @GET("movies")
    Call<Map<String,List<Movie>>> getPromotedMovies(@Header("Authorization") String token);

    @POST("categories")
    @Headers("Content-Type: application/json")
    Call<Void> addCategory(@Header("Authorization") String token, @Body Category category);

    @PATCH("categories/{id}")
    @Headers("Content-Type: application/json")
    Call<Void> updateCategory(@Path("id") String categoryId, @Header("Authorization") String token, @Body Category category);

    @GET("users/{id}")
    Call<UserResponse> getUser(@Path("id") String id);

    @DELETE("categories/{id}")
    Call<Void> deleteCategory(@Path("id") String categoryId, @Header("Authorization") String token);

    @Multipart
    @POST("movies")
    Call<Void> addMovie(
            @Header("Authorization") String token,
            @Part("Title") RequestBody title,
            @Part("ReleaseDate") RequestBody releaseDate,
            @Part("Categories") RequestBody categories,
            @Part MultipartBody.Part MovieImage,
            @Part MultipartBody.Part Film
    );

    @Multipart
    @PUT("movies/{id}")
    Call<Void> updateMovie(
            @Header("Authorization") String token,
            @Path("id") String movieId,
            @Part("Title") RequestBody title,
            @Part("ReleaseDate") RequestBody releaseDate,
            @Part("Categories") RequestBody categories,
            @Part MultipartBody.Part MovieImage,
            @Part MultipartBody.Part Film
    );

    @DELETE("movies/{id}")
    Call<Void> deleteMovie(@Path("id") String movieId, @Header("Authorization") String token);

    @GET("movies/categories")
    Call<Map<String,List<Movie>>> getAllMovies(@Header("Authorization") String token);

    @GET("movies/{id}/recommend")
    Call<List<Movie>> getRecommendations(@Path("id") String movieId, @Header("Authorization") String token);

    @POST("movies/{id}/recommend")
    Call<Void> addAsWatched(@Path("id") String movieId, @Header("Authorization") String token);

    @GET("movies/search/{query}")
    Call<List<Movie>> query(@Path("query") String query, @Header("Authorization") String token);
}
