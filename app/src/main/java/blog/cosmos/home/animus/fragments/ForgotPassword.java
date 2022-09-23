package blog.cosmos.home.animus.fragments;

import static blog.cosmos.home.animus.fragments.CreateAccountFragment.EMAIL_REGEX;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.ReplacerActivity;


public class ForgotPassword extends Fragment {


    private TextView loginTv;
    private Button recoverBtn;
    private EditText emailEt;

    private RelativeLayout rlnotif;
    private TextView textnotif;


    private FirebaseAuth auth;


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

        clickListener();


    }

    private void init(View view){
        loginTv = view.findViewById(R.id.loginTv);
        emailEt = view.findViewById(R.id.emailET);
        recoverBtn = view.findViewById(R.id.recoverBtn);


        auth = FirebaseAuth.getInstance();

    }

    private void clickListener(){


        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ReplacerActivity)getActivity()).setFragment( new LoginFragment());
            }
        });

        recoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEt.getText().toString();

                if(email.isEmpty() || !email.matches(EMAIL_REGEX)){
                    emailEt.setError("Input valid email");
                    notif("Please input valid email");
                    return;

                }

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(),"Password reset email sent successfully",
                                            Toast.LENGTH_SHORT).show();
                                    notif("Password reset email sent successfully");
                                    emailEt.setText(" ");
                                } else{

                                    String errMsg = task.getException().getMessage();
                                    Toast.makeText(getContext(), "Error: "+ errMsg, Toast.LENGTH_SHORT).show();


                                }
                            }
                        });



            }
        });

    }

    public void notif(String text) {
        rlnotif.setVisibility(View.VISIBLE);
        textnotif.setText(text);

        new Handler().postDelayed(() -> rlnotif.setVisibility(View.GONE), 3000);
    }



}