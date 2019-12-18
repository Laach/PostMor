package com.mdhgroup2.postmor.Register;

import com.mdhgroup2.postmor.Register.register1;
import com.mdhgroup2.postmor.Register.register2;
import com.mdhgroup2.postmor.Register.register3;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class RegisterAdapter extends FragmentPagerAdapter {
    public RegisterAdapter (FragmentManager mgr){
        super(mgr);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        if(position == 0)
            return (register1.newInstance(position));

        if(position == 1)
            return  (register2.newInstance(position));

        if(position == 2)
            return  (register3.newInstance(position));
        else
            return (register1.newInstance(position));
    }

    @Override
    public int getCount(){
        return(3);
    }
}
