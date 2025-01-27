package com.example.nog_android;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.nog_android.ManagerFragments.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class ManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        Log.d("ManagerActivity", "ViewPager current position: " + viewPager.getCurrentItem());

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            Log.d("TabLayoutMediator", "Tab clicked at position: " + position);
            switch (position) {
                case 0:
                    tab.setText("Add Category");
                    tab.setContentDescription("Tab for adding a category");
                    break;
                case 1:
                    tab.setText("Edit Category");
                    tab.setContentDescription("Tab for editing a category");
                    break;
                case 2:
                    tab.setText("Delete Category");
                    tab.setContentDescription("Tab for deleting a category");
                    break;
                case 3:
                    tab.setText("Add Movie");
                    tab.setContentDescription("Tab for adding a movie");
                    break;
                case 4:
                    tab.setText("Edit Movie");
                    tab.setContentDescription("Tab for editing a movie");
                    break;
                case 5:
                    tab.setText("Delete Movie");
                    tab.setContentDescription("Tab for deleting a movie");
                    break;
                default:
                    tab.setText("Unknown");
                    break;
            }
        }).attach();

        Log.d("ManagerActivity", "ViewPager current position: " + viewPager.getCurrentItem());

    }
}