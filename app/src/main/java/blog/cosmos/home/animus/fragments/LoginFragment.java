package blog.cosmos.home.animus.fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static blog.cosmos.home.animus.fragments.CreateAccountFragment.EMAIL_REGEX;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import blog.cosmos.home.animus.MainActivity;
import blog.cosmos.home.animus.R;


public class LoginFragment extends Fragment {


    private EditText emailEt, passwordEt;
    private TextView signUpTv, forgotPasswordTv;
    private Button loginBtn, googleSignUpBtn;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;



    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        mAuth = FirebaseAuth.getInstance();

        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build();


        clickListener();

    }

    private void init(View view) {


        emailEt = view.findViewById(R.id.emailET);
        passwordEt = view.findViewById(R.id.passwordET);
        loginBtn = view.findViewById(R.id.loginBtn);
        googleSignUpBtn = view.findViewById(R.id.googleSignInBtn);
        signUpTv = view.findViewById(R.id.signUpTV);
        forgotPasswordTv = view.findViewById(R.id.forgotTV);
        progressBar = view.findViewById(R.id.progressBar);



    }

    private void clickListener(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEt.getText().toString();
                String password = passwordEt.getText().toString();

                if (email.isEmpty() || email.matches(EMAIL_REGEX)){

                    emailEt.setError("Input valid email");
                    return;

                }

                if(password.isEmpty() || password.length() < 6){

                    passwordEt.setError("Input 6 digit valid password ");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if(user.isEmailVerified()){
                                        Toast.makeText(getContext(), "Please verify your email", Toast.LENGTH_SHORT).show();
                                    }

                                    sendUserToMainActivity();

                                }else{
                                    String exception = "Error "+ task.getException().getMessage();
                                    Toast.makeText(getContext(), exception , Toast.LENGTH_SHORT).show();


                                }

                            }
                        });


            }
        });

        googleSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

    }

    private void  sendUserToMainActivity(){

        if(getActivity() == null)
            return;

        startActivity(new Intent(getContext().getApplicationContext(), MainActivity.class));

        getActivity().finish();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    if (idToken !=  null) {
                        // Got an ID token from Google. Use it to authenticate
                        // with Firebase.
                        Log.d(TAG, "Got ID token.");

                        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                        mAuth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInWithCredential:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            updateUI(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                                            updateUI(null);
                                        }
                                    }
                                });

                    }
                } catch (ApiException e) {
                    // ...
                }
                break;
        }
    }
}


