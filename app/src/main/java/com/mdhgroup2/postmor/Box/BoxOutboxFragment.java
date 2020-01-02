package com.mdhgroup2.postmor.Box;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mdhgroup2.postmor.MainActivityViewModel;
import com.mdhgroup2.postmor.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class BoxOutboxFragment extends Fragment {
    private static final String VIEW_NUMBER = "section_number";

    private BoxViewModel boxViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private int index;


    public static BoxOutboxFragment newInstance(int index) {
        BoxOutboxFragment fragment = new BoxOutboxFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(VIEW_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boxViewModel = ViewModelProviders.of(this).get(BoxViewModel.class);
        this.index = 2;
        if (getArguments() != null) {
            index = getArguments().getInt(VIEW_NUMBER);
        }
        boxViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.box_outbox_fragment, container, false);
//        final TextView textView = view.findViewById(R.id.textViewOutbox);
//        final RecyclerView recyclerView = view.findViewById(R.id.boxRecyclerView);

        recyclerView = view.findViewById(R.id.boxRecyclerOutboxView);
        layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        final MainActivityViewModel mViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

        mAdapter = new BoxRecyclerViewAdapter(mViewModel.getMessageList());
        recyclerView.setAdapter(mAdapter);


//        boxViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return view;
    }
}