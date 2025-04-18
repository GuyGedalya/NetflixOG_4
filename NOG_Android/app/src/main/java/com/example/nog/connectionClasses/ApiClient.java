package com.example.nog.connectionClasses;

import retrofit2.Retrofit;

public class ApiClient {
    private static final String BASE_URL = "http://YourIp:3001/api/";
    private static final String BASE_URL_MOVIE = "http://YourIp:3001/";
    private static ApiService apiService;

    public static ApiService getApiService() {
        if (apiService == null) {
            Retrofit retrofit = RetrofitClient.getClient(BASE_URL);
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
    public static String getFullMovieUrl(String relativePath) {
        return BASE_URL_MOVIE + relativePath;
    }


}
