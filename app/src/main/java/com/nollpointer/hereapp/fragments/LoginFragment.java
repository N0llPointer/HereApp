package com.nollpointer.hereapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.nollpointer.hereapp.MainActivity;
import com.nollpointer.hereapp.R;


public class LoginFragment extends Fragment {


    private View mainView;

    private TextView registerLink;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_login, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initializeViews();
        return mainView;
    }

    private void initializeViews(){
        registerLink = mainView.findViewById(R.id.link_signup);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout,new RegisterFragment()).addToBackStack(null).commit();
            }
        });

        Button loginButton = mainView.findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showMapsFragment();
            }
        });
    }

}
