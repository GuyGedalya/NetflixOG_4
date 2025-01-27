package com.example.nog_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nog_android.ObjectClasses.TokenManager;
import com.example.nog_android.connectionClasses.ApiClient;
import com.example.nog_android.connectionClasses.ApiService;
import com.example.nog_android.connectionClasses.LoginRequest;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // getting view elements
        EditText userNameEditText = findViewById(R.id.userName);
        EditText passwordEditText = findViewById(R.id.password);
        Button submitBtn = findViewById(R.id.submitBtn);
        Button toSignUp = findViewById(R.id.toSignUpBtn);

        // Setting submit listener
        toSignUp.setOnClickListener(v-> {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
            finish();
        });

        submitBtn.setOnClickListener(v -> logIn(userNameEditText, passwordEditText));
    }

    private void logIn (EditText userNameEt, EditText passwordEt) {
        // Getting input from edit texts
        String userNameValue = userNameEt.getText().toString().trim();
        String passwordValue = passwordEt.getText().toString().trim();

        ApiService apiService = ApiClient.getApiService();
        LoginRequest loginRequest = new LoginRequest(userNameValue, passwordValue);
        Call<TokenManager> call = apiService.logIn(loginRequest);

        // Calling the server
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TokenManager> call, @NonNull Response<TokenManager> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LogInActivity.this, "LogIn Successful!" , Toast.LENGTH_SHORT).show();
                    TokenManager tokenManager = response.body();
                    // if we got a token
                    if(tokenManager != null) {
                        TokenManager.getInstance().setToken(tokenManager.getToken());
                        TokenManager.getInstance().setUser(tokenManager.getUser());
                        Intent intent = new Intent(LogInActivity.this, ManagerActivity.class);
                        startActivity(intent);
                        finish();
                    } else{
                        // If response unsuccessful, popping up error message
                        try (ResponseBody errorBody = response.errorBody()) {
                            String errorMessage = errorBody != null ? errorBody.string() : "Unknown error";
                            Toast.makeText(LogInActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(LogInActivity.this, "Error reading error message", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<TokenManager> call, @NonNull Throwable t) {
                Toast.makeText(LogInActivity.this, "Error: Please check your connection and try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}