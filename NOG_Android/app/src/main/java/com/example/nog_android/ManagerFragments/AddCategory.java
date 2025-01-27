package com.example.nog_android.ManagerFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nog_android.R;

public class AddCategory extends Fragment {
    public AddCategory(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("AddCategoryFragment", "AddCategory Fragment loaded");
        return inflater.inflate(R.layout.add_category_form, container, false);
    }

}
