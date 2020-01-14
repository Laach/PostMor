package com.mdhgroup2.postmor.Compose;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mdhgroup2.postmor.Home.HomeFragmentDirections;
import com.mdhgroup2.postmor.MainActivityViewModel;
import com.mdhgroup2.postmor.R;

public class ComposeFragment extends Fragment {

    private ComposeViewModel mViewModel;
    private MainActivityViewModel mainVM;
    NavController navController;


    public static ComposeFragment newInstance() {
        return new ComposeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        /*
        int id = 0;
        try{
            id = getArguments().getInt("id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(id != 0){
            mainVM.chooseRecipientById(id);
        }*/

        View view = inflater.inflate(R.layout.compose_fragment, container, false);
        return view;
    }

    @Override
    public void onDestroy() {

        mainVM.removeRecipient();
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ComposeViewModel.class);
        mainVM = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);


        if(getArguments() != null){
            int id = getArguments().getInt("id");
            if(id != 0){
                mainVM.chooseRecipientById(id);
            }
        }

        View view = getView();
        navController = Navigation.findNavController(view);

        Button typeButton = view.findViewById(R.id.typeButton);
        Button handButton = view.findViewById(R.id.handButton);
        typeButton.setOnClickListener(Navigation.createNavigateOnClickListener(ComposeFragmentDirections.actionComposeFragmentToCompose2Typed()));
        handButton.setOnClickListener(Navigation.createNavigateOnClickListener(ComposeFragmentDirections.actionComposeFragmentToCompose2Handwritten()));
        // TODO: Use the ViewModel
    }

}
