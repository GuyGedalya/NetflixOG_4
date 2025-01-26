package com.example.nog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class NavigationMenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_menu, container, false);

        NavigationView navigationView = view.findViewById(R.id.navigation_view);
        DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer_layout);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(getActivity(), HomePageActivity.class));
            } else if (id == R.id.nav_categories) {
                startActivity(new Intent(getActivity(), CategoryPageActivity.class));
            } else if (id == R.id.nav_manager) {
                // Handle manager navigation
            } else if (id == R.id.nav_search) {
                // Handle search navigation
            }

            drawerLayout.closeDrawers();
            return true;
        });

        return view;
    }
}
