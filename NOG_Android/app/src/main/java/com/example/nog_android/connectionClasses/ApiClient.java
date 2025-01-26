package com.example.nog_android.connectionClasses;

import retrofit2.Retrofit;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.0.21:3001/api/";
    private static ApiService apiService;

    public static ApiService getApiService() {
        if (apiService == null) {
            Retrofit retrofit = RetrofitClient.getClient(BASE_URL);
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
}
