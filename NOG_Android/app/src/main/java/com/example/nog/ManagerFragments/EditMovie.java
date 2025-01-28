package com.example.nog.ManagerFragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nog.ObjectClasses.TokenManager;
import com.example.nog.R;
import com.example.nog.connectionClasses.ApiClient;
import com.example.nog.connectionClasses.ApiService;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMovie extends Fragment {
    public EditMovie() {
    }

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<Intent> videoPickerLauncher;
    private Uri selectedImageUri;

    private Uri selectedFilmUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edit_movie_form, container, false);

        Button addCategoryBtn = rootView.findViewById(R.id.addCategoryButton);
        EditText categoryInput = rootView.findViewById(R.id.categoryInput);
        ChipGroup chipGroup = rootView.findViewById(R.id.chipGroup);

        EditText movieIdEt = rootView.findViewById(R.id.movieId);
        EditText movieTitleEt = rootView.findViewById(R.id.movieTitle);
        EditText dateEt = rootView.findViewById(R.id.movieRelease);
        Button uploadMovieImgBtn = rootView.findViewById(R.id.movieImage);
        Button uploadFilmBtn = rootView.findViewById(R.id.Film);
        Button submitBtn = rootView.findViewById(R.id.submit);

        addCategoryBtn.setOnClickListener(v -> addToChipGroup(categoryInput, chipGroup));

        // Initialize picker launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                    }
                }
        );

        // Initialize picker launcher
        videoPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedFilmUri = result.getData().getData();
                    }
                }
        );

        uploadMovieImgBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        uploadFilmBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("video/*");
            videoPickerLauncher.launch(intent);
        });

        submitBtn.setOnClickListener(v -> validateAndSubmit(movieIdEt, movieTitleEt, dateEt, chipGroup));

        return rootView;
    }

    private void validateAndSubmit(EditText movieIdEt, EditText titleEt, EditText dateEt, ChipGroup chipGroup) {
        String id = movieIdEt.getText().toString().trim();
        String title = titleEt.getText().toString().trim();
        String date = dateEt.getText().toString().trim();

        if (id.isEmpty()) {
            movieIdEt.setError("Movie Id is required");
            return;
        }
        if (title.isEmpty()) {
            titleEt.setError("Title is required");
            return;
        }
        if (date.isEmpty() || !isValidDateFormat(date)) {
            dateEt.setError("Date must be in format YYYY-MM-DD (with dashes)");
            return;
        }

        if (chipGroup.getChildCount() == 0) {
            Toast.makeText(requireContext(), "Please include category", Toast.LENGTH_SHORT).show();
        }
        // Check if both image and film are selected
        if (selectedImageUri == null) {
            Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedFilmUri == null) {
            Toast.makeText(requireContext(), "Please select a film", Toast.LENGTH_SHORT).show();
            return;
        }
        List<String> categories = getCategoriesFromChipGroup(chipGroup);
        submitToServer(id, title, date, categories);
    }

    private void submitToServer(String idValue,String titleValue, String date, List<String> categoryList) {
        ApiService apiService = ApiClient.getApiService();
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), titleValue);
        RequestBody releaseDate = RequestBody.create(MediaType.parse("text/plain"), date);

        // Converting the list to a json requestBody
        Gson gson = new Gson();
        String jsonCategories = gson.toJson(categoryList);
        RequestBody categories = RequestBody.create(MediaType.parse("application/json"), jsonCategories);

        MultipartBody.Part imagePart = null;
        MultipartBody.Part filmPart = null;
        FileManager fileManager = new FileManager(requireContext());

        // Getting RequestBody of image
        String imgPath = fileManager.getPathFromUri(selectedImageUri);
        if (imgPath != null) {
            File file = new File(imgPath);
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
            imagePart = MultipartBody.Part.createFormData("MovieImage", file.getName(), fileBody);
        }
        // Getting RequestBody of film
        String filmPath = fileManager.getPathFromUri(selectedFilmUri);
        if (filmPath != null) {
            File file = new File(filmPath);
            RequestBody fileBody = RequestBody.create(MediaType.parse("video/*"), file);
            filmPart = MultipartBody.Part.createFormData("Film", file.getName(), fileBody);
        }
        TokenManager tokenManager = TokenManager.getInstance();
        // Constructing token
        String token = "Bearer " + tokenManager.getToken();
        Call<Void> call = apiService.updateMovie(token, idValue , title, releaseDate, categories, imagePart, filmPart);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Updated!", Toast.LENGTH_SHORT).show();
                } else {

                    // If response is un successful, pop up server's error
                    try (ResponseBody errorBody = response.errorBody()) {
                        String errorMessage = errorBody != null ? errorBody.string() : "Unknown error";
                        // Print full response details for debugging
                        Toast.makeText(requireContext(), "Film file might be too large", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(requireContext(), "Error reading error message", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Error: Please check your connection and try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidDateFormat(String date) {
        return date.matches("^\\d{4}-\\d{2}-\\d{2}$"); // YYYY-MM-DD format
    }

    private void addToChipGroup(EditText categoryInput, ChipGroup chipGroup) {
        String category = categoryInput.getText().toString().trim();
        // Not adding an empty option
        if (category.isEmpty()) {
            return;
        }
        if (checkIfExists(category, chipGroup)) {
            Toast.makeText(requireContext(), "Category already exists!", Toast.LENGTH_SHORT).show();
        } else {
            // Adding ship to chip group
            Chip chip = new Chip(requireContext());
            chip.setText(category);
            chip.setCloseIconVisible(true); // Showing option to remove
            chip.setOnCloseIconClickListener(v -> chipGroup.removeView(chip));
            chipGroup.addView(chip);
            categoryInput.setText("");
        }
    }

    private Boolean checkIfExists(String category, ChipGroup chipGroup) {
        boolean exists = false;
        // Going over all the chips
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.getText().toString().equalsIgnoreCase(category)) {
                break;
            }
        }
        return exists;
    }

    private List<String> getCategoriesFromChipGroup(ChipGroup chipGroup) {
        List<String> categories = new ArrayList<>();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            categories.add(chip.getText().toString());
        }
        return categories;
    }
}
