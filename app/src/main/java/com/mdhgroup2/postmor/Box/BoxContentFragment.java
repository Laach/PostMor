package com.mdhgroup2.postmor.Box;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mdhgroup2.postmor.MainActivityViewModel;
import com.mdhgroup2.postmor.R;
import com.mdhgroup2.postmor.database.DTO.MsgCard;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class BoxContentFragment extends Fragment {
    private static final String VIEW_NUMBER = "section_number";
    private RecyclerView recyclerView;
    private BoxRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private int boxIndex;
    private int id = 0;

    public static BoxContentFragment newInstance(int index) {
        BoxContentFragment fragment = new BoxContentFragment(index);
        Bundle bundle = new Bundle();
        bundle.putInt(VIEW_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    public BoxContentFragment(int index){
        boxIndex = index;
    }

    public BoxContentFragment(int index, int ID){
        boxIndex = index;
        id = ID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            boxIndex = getArguments().getInt(VIEW_NUMBER);
        }

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.box_content_fragment, container, false);
        final MainActivityViewModel mViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
//        final TextView textView = view.findViewById(R.id.textViewAll);
//        boxViewModel.setIndex(index);

        recyclerView = view.findViewById(R.id.boxRecyclerAllView);
        layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);

        GetMessageListAsync background = new GetMessageListAsync(mViewModel, boxIndex, id);
        List<MsgCard> list;
        try {
            list = background.execute().get();
        }
        catch (InterruptedException | ExecutionException e){
            list = null;
        }
        mAdapter = new BoxRecyclerViewAdapter(list);

        recyclerView.setAdapter(mAdapter);

//        boxViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return view;
    }


    private class GetMessageListAsync extends AsyncTask<Void, Void, List<MsgCard>>{

        private MainActivityViewModel mvm;
        private int boxIndex;
        private int id;

        public GetMessageListAsync(MainActivityViewModel m, int boxindex, int userId){
            mvm = m;
            boxIndex = boxindex;
            id = userId;
        }

        @Override
        protected List<MsgCard> doInBackground(Void... voids) {
            if(id == 0){
                return mvm.getMessageList(boxIndex);
            }
            return mvm.getMessageList(boxIndex, id);
        }
    }
}