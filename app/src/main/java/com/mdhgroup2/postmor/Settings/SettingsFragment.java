package com.mdhgroup2.postmor.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mdhgroup2.postmor.About.AboutFragment;
import com.mdhgroup2.postmor.MainActivityViewModel;
import com.mdhgroup2.postmor.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.preferences, rootKey);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        final MainActivityViewModel mViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        Preference changePassword = findPreference("changePassword");
        Preference about = findPreference("about");
        Preference signOut = findPreference("sign_out");

        mViewModel.amILoggedIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Boolean result = aBoolean;
                if (!result){
                    Navigation.findNavController(getView()).navigate(R.id.action_settingsFragment_to_signInFragment);
                }
            }
        });

        mViewModel.checkLoginStatus();

        changePassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Navigation.findNavController(getView()).navigate(SettingsFragmentDirections.actionSettingsFragmentToPasswordDialogFragment());
                return true;
            }
        });

        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Navigation.findNavController(getView()).navigate(SettingsFragmentDirections.actionSettingsFragmentToAboutFragment());
                return true;
            }
        });

        signOut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mViewModel.logOut();
                Navigation.findNavController(getView()).navigate(R.id.action_settingsFragment_to_signInFragment);
                return true;
            }
        });
    }
}
