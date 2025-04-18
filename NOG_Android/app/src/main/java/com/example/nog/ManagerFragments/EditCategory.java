package com.example.nog.ManagerFragments;

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
import com.example.nog.ObjectClasses.Category;
import com.example.nog.ObjectClasses.TokenManager;
import com.example.nog.R;
import com.example.nog.connectionClasses.ApiClient;
import com.example.nog.connectionClasses.ApiService;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCategory extends Fragment {
    public EditCategory(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView =   inflater.inflate(R.layout.edit_category_form, container, false);

        // Getting view elements
        EditText categoryIdEt = rootView.findViewById(R.id.categoryId);
        EditText categoryNameEt = rootView.findViewById(R.id.categoryName);
        CheckBox promotedCb = rootView.findViewById(R.id.promotion);
        Button submitBtn = rootView.findViewById(R.id.submit);

        // setting submit button
        submitBtn.setOnClickListener(v -> checkValidAndSend(categoryIdEt, categoryNameEt,promotedCb));

        return rootView;
    }
    private void checkValidAndSend(EditText idEt,EditText nameEt, CheckBox promoted) {
        String nameC = nameEt.getText().toString().trim();
        String idC = idEt.getText().toString().trim();
        // Validating field
        if(idC.isEmpty()) {
            idEt.setError("Category Id is required");
        }
        if (nameC.isEmpty()) {
            nameEt.setError("Category name is required");
            return;
        }
        Category category = new Category(nameC, promoted.isChecked());
        sendToServer(idC, category);
    }

    private void sendToServer(String categoryId,Category category){
        ApiService apiService = ApiClient.getApiService();
        TokenManager tokenManager= TokenManager.getInstance();
        // Constructing token
        String token = "Bearer " + tokenManager.getToken();
        Call<Void> call = apiService.updateCategory(categoryId,token, category);

        // Calling the server to update category
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,@NonNull Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(requireContext(), "Updated!",Toast.LENGTH_SHORT).show();
                }else{
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
