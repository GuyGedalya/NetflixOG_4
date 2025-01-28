package com.example.nog.Activities;

import android.os.Bundle;
import androidx.viewpager2.widget.ViewPager2;

import com.example.nog.R;
import com.example.nog.ManagerFragments.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class ManagerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        // We have a tab view
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        // Setting adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Tab description
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

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_manager;
    }
}