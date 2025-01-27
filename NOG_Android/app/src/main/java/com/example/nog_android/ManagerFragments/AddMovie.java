package com.example.nog_android.ManagerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nog_android.R;
import com.google.android.material.chip.ChipGroup;

public class AddMovie extends Fragment {
    public AddMovie(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_movie_form, container, false);

        Button addCategoryBtn = rootView.findViewById(R.id.addCategoryButton);
        EditText categoryInput = rootView.findViewById(R.id.categoryInput);
        ChipGroup chipGroup = rootView.findViewById(R.id.chipGroup);

        addCategoryBtn.setOnClickListener( view -> {
            String category = categoryInput.getText().toString().trim();

        });

        return rootView;
    }
}
