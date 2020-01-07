package com.mdhgroup2.postmor.Home;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.TextView;

import com.mdhgroup2.postmor.R;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    NavController navController;
    private View infoBar;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        updateInfobar();
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

        //display information on letters being sent on a top bar if relevant
        infoBar = view.findViewById(R.id.topInfoBar);
        updateInfobar();

        //Configure navigation buttons
        View boxButton = view.findViewById(R.id.boxButton);
        View composeButton = view.findViewById(R.id.composeButton);
        View contactsButton = view.findViewById(R.id.contactsButton);
        View settingsButton = view.findViewById(R.id.settingsButton);
        boxButton.setOnClickListener(Navigation.createNavigateOnClickListener(HomeFragmentDirections.actionHomeFragmentToBoxFragment()));
        composeButton.setOnClickListener(Navigation.createNavigateOnClickListener(HomeFragmentDirections.actionHomeFragmentToComposeFragment()));
        contactsButton.setOnClickListener(Navigation.createNavigateOnClickListener(HomeFragmentDirections.actionHomeFragmentToContactsFragment()));
        settingsButton.setOnClickListener(Navigation.createNavigateOnClickListener(HomeFragmentDirections.actionHomeFragmentToSettingsFragment()));
    }

    private void updateInfobar()
    {
        View view = getView();
        if( ! mViewModel.getOutgoingLetterCount().equals("0"))
        {
            TextView sendTime = view.findViewById(R.id.sendTime);
            sendTime.setText(mViewModel.getEmptyTime());
            TextView letterCount = getView().findViewById(R.id.nOfLetterToBeSent);
            letterCount.setText(mViewModel.getOutgoingLetterCount());
            infoBar.setVisibility(View.VISIBLE);
        } else
        {
            infoBar.setVisibility(View.GONE);
        }
    }

}
