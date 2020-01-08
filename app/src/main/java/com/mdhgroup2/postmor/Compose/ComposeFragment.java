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
    NavController navController;


    public static ComposeFragment newInstance() {
        return new ComposeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        return inflater.inflate(R.layout.compose_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ComposeViewModel.class);


        if(getArguments() != null){
            final MainActivityViewModel mainViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
            int id = getArguments().getInt("id");
            DTO dto = new DTO();
            dto.ID = id;
            dto.mvm = mainViewModel;
            new SetRecipientAsync().execute(dto);
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
