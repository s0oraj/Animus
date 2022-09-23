package blog.cosmos.home.animus.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import blog.cosmos.home.animus.R;


public class ForgotPassword extends Fragment {


    private TextView loginTv;
    private Button recoverBtn;
    private EditText emailEt;

    public ForgotPassword() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);


    }

    private void init(View view){
        loginTv = view.findViewById(R.id.loginTv);
        emailEt = view.findViewById(R.id.emailET);
        recoverBtn = view.findViewById(R.id.recoverBtn);


    }





}