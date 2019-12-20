package com.mdhgroup2.postmor.Home;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.mdhgroup2.postmor.R;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    NavController navController;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View view = getView();
        navController = Navigation.findNavController(view);
        Button boxButton = view.findViewById(R.id.boxButton);
        Button composeButton = view.findViewById(R.id.composeButton);
        Button contactsButton = view.findViewById(R.id.contactsButton);
        ImageButton settingsButton = view.findViewById(R.id.settingsButton);
        boxButton.setOnClickListener(Navigation.createNavigateOnClickListener(HomeFragmentDirections.actionHomeFragmentToBoxFragment()));
        composeButton.setOnClickListener(Navigation.createNavigateOnClickListener(HomeFragmentDirections.actionHomeFragmentToComposeFragment()));
        contactsButton.setOnClickListener(Navigation.createNavigateOnClickListener(HomeFragmentDirections.actionHomeFragmentToContactsFragment()));
        settingsButton.setOnClickListener(Navigation.createNavigateOnClickListener(HomeFragmentDirections.actionHomeFragmentToSettingsFragment()));
        // TODO: Use the ViewModel
    }

}
