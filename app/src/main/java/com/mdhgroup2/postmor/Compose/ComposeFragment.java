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
                DTO dto = new DTO();
                dto.ID = id;
                dto.mvm = mainVM;
                new SetRecipientAsync().execute(dto);
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

    private class DTO{
        public MainActivityViewModel mvm;
        public int ID;
    }

    private class SetRecipientAsync extends AsyncTask<DTO, Void, Void>{

        @Override
        protected Void doInBackground(DTO... dto) {
            int ID = dto[0].ID;
            dto[0].mvm.chooseRecipientById(ID);

            return null;
        }
    }

}
