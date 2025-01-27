package com.example.nog.ManagerFragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

// This class sets an adapter for moving through forms in the manager page
public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        // Switching between tabs
        switch (position) {
            case 1:
                return new EditCategory();
            case 2:
                return new DeleteCategory();
            case 3:
                return new AddMovie();
            case 4:
                return new EditMovie();
            case 5:
                return new DeleteMovie();
            default:
                return new AddCategory();
        }
    }

    @Override
    public int getItemCount() {
        return 6;  // Number of forms (Fragments)
    }
}
