package com.example.nog.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nog.R;
import com.example.nog.ObjectClasses.TokenManager;
import com.example.nog.connectionClasses.ApiClient;
import com.example.nog.connectionClasses.ApiService;
import com.example.nog.connectionClasses.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

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

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TokenManager> call, @NonNull Response<TokenManager> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // LogIn successful
                    TokenManager tokenManager = response.body();

                    TokenManager.getInstance().setToken(tokenManager.getToken());
                    TokenManager.getInstance().setUser(tokenManager.getUser());
                    Toast.makeText(LogInActivity.this, "LogIn Successful!", Toast.LENGTH_SHORT).show();

                    // Navigate to ManagerActivity
                    Intent intent = new Intent(LogInActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // LogIn failed: show server error message
                    try (ResponseBody errorBody = response.errorBody()) {
                        String errorMessage;
                        if (errorBody != null) {
                            String rawError = errorBody.string();

                            try{
                                JSONObject jsonObject = new JSONObject(rawError);
                                errorMessage = jsonObject.optString("error", rawError);
                            } catch (JSONException e) {
                                errorMessage = rawError;
                            }
                            Toast.makeText(LogInActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(LogInActivity.this, "Empty error response from server", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(LogInActivity.this, "Error reading error message", Toast.LENGTH_SHORT).show();
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