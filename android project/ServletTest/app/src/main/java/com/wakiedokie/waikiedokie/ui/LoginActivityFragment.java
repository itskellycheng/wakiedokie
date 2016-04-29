package com.wakiedokie.waikiedokie.ui;

import com.wakiedokie.waikiedokie.R;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.wakiedokie.waikiedokie.util.database.DBHelper;
/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {

    DBHelper mydb;
    private CallbackManager mCallbackManager;

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            displayProfile(profile);
            mydb.insertMe(profile.getId(), profile.getFirstName(), profile.getLastName());
            Intent intent = new Intent(getActivity(), AlarmMainActivity.class);
            startActivity(intent);

        }




        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    public LoginActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        super.onCreate(savedInstanceState);
        mydb = new DBHelper(getActivity());
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, mCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayProfile(profile);
        mydb.insertMe(profile.getId(), profile.getFirstName(), profile.getLastName());
        Intent intent = new Intent(getActivity(), AlarmMainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void displayProfile(Profile profile) {
        if (profile != null) {
            String greetings = "Welcome! " + profile.getName();
            Toast.makeText(getActivity(), greetings, Toast.LENGTH_SHORT).show();
        }
    }


}
