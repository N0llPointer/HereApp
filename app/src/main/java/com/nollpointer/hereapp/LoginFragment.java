package com.nollpointer.hereapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;


public class LoginFragment extends Fragment {


    private View mainView;

    private TextView registerLink;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_login, container, false);
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
