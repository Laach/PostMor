//package com.mdhgroup2.postmor.Register;
//
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProviders;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.navigation.NavOptions;
//import androidx.navigation.Navigation;
//import androidx.viewpager.widget.ViewPager;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.mdhgroup2.postmor.R;
//import com.mdhgroup2.postmor.database.repository.AccountRepository;
//
//import java.util.List;
//
//public class RegisterFragment extends Fragment {
//
//    private RegisterViewModel mViewModel;
//    private ViewPager viewPager;
//    private int currentPage = 0;
//    private int amountOfPages = 3;
//
//
//    public static RegisterFragment newInstance() {
//        return new RegisterFragment();
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        final View view = inflater.inflate(R.layout.register_fragment, container, false);
//        mViewModel = ViewModelProviders.of(getActivity()).get(RegisterViewModel.class);
//
//        //Checks if a submit has gone through as intended.
//        mViewModel.getResults().observe(this, new Observer<List<String>>() {
//            @Override
//            public void onChanged(List<String> strings) {
//                String temp = strings.get(0);
//                if(temp.equals("Ok")){
//                    Navigation.findNavController(view).navigate(R.id.homeFragment, null, new NavOptions.Builder().setPopUpTo(R.id.homeFragment,false).build());
//                }
//                else {
//                    Toast toast = Toast.makeText(getContext(), temp, Toast.LENGTH_SHORT);
//                    toast.show();
//                }
//            }
//        });
//
//        viewPager = view.findViewById(R.id.viewPager);
//        viewPager.setAdapter(new RegisterAdapter(this.getFragmentManager()));
//        viewPager.setOffscreenPageLimit(amountOfPages);
//
//        final Button nextFragment = view.findViewById(R.id.register_next_button);
//        final Button previousFragment = view.findViewById(R.id.register_prev_button);
//        final Button submit = view.findViewById(R.id.register_submit_button);
//        previousFragment.setVisibility(View.INVISIBLE);
//        submit.setVisibility(View.INVISIBLE);
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                currentPage = position;
//                updateNameAndAddress(view);
//                if(currentPage > 0 && currentPage < amountOfPages - 1){
//                    nextFragment.setVisibility(View.VISIBLE);
//                    previousFragment.setVisibility(View.VISIBLE);
//                    submit.setVisibility(View.INVISIBLE);
//                }
//                if(currentPage == amountOfPages - 1) {
//                    nextFragment.setVisibility(View.INVISIBLE);
//                    submit.setVisibility(View.VISIBLE);
//                }
//                if(currentPage == 0) {
//                    previousFragment.setVisibility(View.INVISIBLE);
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
//
//        nextFragment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View onView) {
//                currentPage++;
//                updateNameAndAddress(view);
//                previousFragment.setVisibility(View.VISIBLE);
//                if(currentPage == amountOfPages - 1) {
//                    nextFragment.setVisibility(View.INVISIBLE);
//                    submit.setVisibility(View.VISIBLE);
//                }
//                viewPager.setCurrentItem(currentPage,true);
//            }
//        });
//
//        previousFragment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View onView) {
//                currentPage--;
//                submit.setVisibility(View.INVISIBLE);
//                nextFragment.setVisibility(View.VISIBLE);
//                if(currentPage == 0) {
//                    previousFragment.setVisibility(View.INVISIBLE);
//                }
//                viewPager.setCurrentItem(currentPage, true);
//            }
//        });
//
//        submit.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View onView){
//                AccountRepository.PasswordStatus status = mViewModel.checkPasswordValidity();
//                if(status == AccountRepository.PasswordStatus.Ok){
//                    String validity = mViewModel.validateAccountInformation();
//                    if(validity.equals("True")){
//                        mViewModel.register();
//                    }
//                    else {
//                        Toast toast = Toast.makeText(getContext(), validity, Toast.LENGTH_SHORT);
//                        toast.show();
//                    }
//                }
//                else {
//                    Toast toast = Toast.makeText(getContext(), status.toString(), Toast.LENGTH_SHORT);
//                    toast.show();
//                }
//            }
//        });
//        return view;
//    }
//
//    private void updateNameAndAddress(View view){
//        TextView tv = view.findViewById(R.id.cardName);
//        if(tv != null) {
//            tv.setText(mViewModel.getAccountName());
//            tv = view.findViewById(R.id.cardAddress);
//            tv.setText(mViewModel.getAddress());
//        }
//    }
//}

