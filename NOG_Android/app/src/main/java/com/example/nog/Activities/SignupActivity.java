package com.example.nog.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.nog.R;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nog.connectionClasses.ApiClient;
import com.example.nog.connectionClasses.ApiService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        EditText userNameEditText = findViewById(R.id.userName);
        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);
        EditText phoneEditText = findViewById(R.id.phone);
        Button submitButton = findViewById(R.id.submit);
        Button toLogInBtn = findViewById(R.id.toLogInBtn);
        Button uploadImg = findViewById(R.id.uploadProfileImage);

        // Initialize image picker launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                    }
                }
        );

        toLogInBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
        });
        uploadImg.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        submitButton.setOnClickListener(v -> validateAndSubmit(userNameEditText, emailEditText, passwordEditText, phoneEditText));
    }

    // Checking the the fields are valid
    private void validateAndSubmit(EditText userName, EditText email, EditText password, EditText phone) {
        String userNameValue = userName.getText().toString().trim();
        String emailValue = email.getText().toString().trim();
        String passwordValue = password.getText().toString().trim();
        String phoneValue = phone.getText().toString().trim();

        if (userNameValue.isEmpty()) {
            userName.setError("Username is required");
            return;
        }

        if (emailValue.isEmpty()) {
            email.setError("Email is required");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            email.setError("Invalid email address");
            return;
        }

        if (passwordValue.isEmpty() || !isPasswordValid(passwordValue)) {
            password.setError("Password must be at least 8 characters, include a letter and a number");
            return;
        }

        if (!phoneValue.startsWith("05") || !phoneValue.matches("\\d{10}")) {
            phone.setError("Phone number must be exactly 10 digits and start with 05 (e.g., 0541234567)");
            return;
        }

        submitUserToServer(userNameValue, emailValue, passwordValue, phoneValue);
    }

    // Validate Password field
    private boolean isPasswordValid(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Za-z].*") && password.matches(".*\\d.*");
    }

    private void submitUserToServer(String userNameValue, String emailValue, String passwordValue, String phoneValue) {
        ApiService apiService = ApiClient.getApiService();
        RequestBody userNameBody = RequestBody.create(MediaType.parse("text/plain"), userNameValue);
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), emailValue);
        RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), passwordValue);
        RequestBody phoneBody = RequestBody.create(MediaType.parse("text/plain"), phoneValue);

        MultipartBody.Part imagePart = null;
        if (selectedImageUri != null) {
            String filePath = getPathFromUri(selectedImageUri);
            if (filePath != null){
                File file = new File(filePath);
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
                imagePart = MultipartBody.Part.createFormData("ProfileImage", file.getName(), fileBody);
            }
        }

        Call<Void> call = apiService.addUser(userNameBody, emailBody, passwordBody, phoneBody, imagePart);

        // Sending request to the server
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    // Success massage
                    Toast.makeText(SignupActivity.this, "Sign Up Successful! You can now log in!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, "Failed to sign up: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(SignupActivity.this, "Error: Please check your connection and try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getPathFromUri(Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return filePath;
    }
}