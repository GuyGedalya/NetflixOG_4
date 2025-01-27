package com.example.nog_android.ManagerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nog_android.ObjectClasses.Category;
import com.example.nog_android.ObjectClasses.TokenManager;
import com.example.nog_android.R;
import com.example.nog_android.connectionClasses.ApiClient;
import com.example.nog_android.connectionClasses.ApiService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCategory extends Fragment {
    public AddCategory(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflating the Add category view
        View rootView =  inflater.inflate(R.layout.add_category_form, container, false);

        // Get elements
        EditText categoryNameEt = rootView.findViewById(R.id.categoryName);
        CheckBox promotedCb = rootView.findViewById(R.id.promotion);
        Button submitBtn = rootView.findViewById(R.id.submit);

        // Setting submit listener
        submitBtn.setOnClickListener(v -> checkValidAndSend(categoryNameEt,promotedCb));

        return rootView;
    }

    private void checkValidAndSend(EditText nameEt, CheckBox promoted) {
        String nameC = nameEt.getText().toString().trim();
        // validation
        if (nameC.isEmpty()) {
            nameEt.setError("Category name is required");
            return;
        }
        Category category = new Category(nameC, promoted.isChecked());
        sendToServer(category);
    }

    // Sending category for the server to add
    private void sendToServer(Category category){
        ApiService apiService = ApiClient.getApiService();
        TokenManager tokenManager= TokenManager.getInstance();
        // Constructing token
        String token = "Bearer " + tokenManager.getToken();
        // Constructing call
        Call<Void> call = apiService.addCategory(token, category);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,@NonNull Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(requireContext(), "Added!",Toast.LENGTH_SHORT).show();
                }else{
                    // If response is un successful, pop up server's error
                    try (ResponseBody errorBody = response.errorBody()) {
                        String errorMessage = errorBody != null ? errorBody.string() : "Unknown error";
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(requireContext(), "Error reading error message", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Error: Please check your connection and try again.",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
