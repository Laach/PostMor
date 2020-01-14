package com.mdhgroup2.postmor.Home;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.mdhgroup2.postmor.MainActivityViewModel;
import com.mdhgroup2.postmor.R;

import java.util.concurrent.ExecutionException;

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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);
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
        final View view = getView();

        final MainActivityViewModel mainViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

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
        GetInfobarInfo gii = new GetInfobarInfo();
        ProfileInfo pi = null;
        try {
            pi = gii.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TextView sendTime = view.findViewById(R.id.sendTime);
        TextView letterCount = getView().findViewById(R.id.nOfLetterToBeSent);
        TextView nameText = view.findViewById(R.id.nameTextView);
        TextView addressText = view.findViewById(R.id.addressTextView);
        ImageView profilePicture = view.findViewById(R.id.profilePictureImageView);

        sendTime.setText(pi.sendTime);
        letterCount.setText(pi.nOfLetters);
        nameText.setText(pi.name);
        addressText.setText(pi.address);
        if(pi.profilePicture == null)
        {
            profilePicture.setImageResource(R.drawable.anon_profile);
        } else{
            profilePicture.setImageBitmap(pi.profilePicture);
        }
        infoBar.setVisibility(View.VISIBLE);
    }

    private class ProfileInfo{
        public String name;
        public String address;
        public String sendTime;
        public String nOfLetters;
        public Bitmap profilePicture;
    }

    private class GetInfobarInfo extends AsyncTask<Void, Void, ProfileInfo> {
        @Override
        protected ProfileInfo doInBackground(Void... v){
            ProfileInfo result = new ProfileInfo();
            try{
                result.name = mViewModel.getOwnName();
                result.address = mViewModel.getOwnAddress();
                result.nOfLetters = mViewModel.getOutgoingLetterCount();
                result.sendTime = mViewModel.getEmptyTime();
                result.profilePicture = mViewModel.getOwnProfilePicture();
            }catch (Exception e){
                result.name = "";
                result.address = "";
                result.nOfLetters = "";
                result.sendTime = "";
                result.profilePicture = null;
            }
            return result;
        }
    }

}
