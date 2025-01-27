package com.example.nog.ManagerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nog.ObjectClasses.TokenManager;
import com.example.nog.R;
import com.example.nog.connectionClasses.ApiClient;
import com.example.nog.connectionClasses.ApiService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteMovie extends Fragment {
    public DeleteMovie(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.delete_movie_form, container, false);

        EditText movieIdEt = rootView.findViewById(R.id.movieId);
        Button submitBtn = rootView.findViewById(R.id.submit);

        submitBtn.setOnClickListener(v -> validateAndSend(movieIdEt));
        return rootView;
    }

    private void validateAndSend(EditText movieIdEt){
        String idM = movieIdEt.getText().toString().trim();
        if (idM.isEmpty()){
            movieIdEt.setError("Movie Id is required");
            return;
        }
        sendToServer(idM);
    }

    private void sendToServer(String movieId){
        ApiService apiService = ApiClient.getApiService();
        TokenManager tokenManager= TokenManager.getInstance();
        String token = "Bearer " + tokenManager.getToken();

        Call<Void> call = apiService.deleteMovie(movieId,token);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,@NonNull Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(requireContext(), "Deleted!",Toast.LENGTH_SHORT).show();
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
