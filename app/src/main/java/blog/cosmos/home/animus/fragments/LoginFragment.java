package blog.cosmos.home.animus.fragments;

import static blog.cosmos.home.animus.fragments.CreateAccountFragment.EMAIL_REGEX;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import blog.cosmos.home.animus.MainActivity;
import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.ReplacerActivity;


public class LoginFragment extends Fragment {

    private static final int RC_SIGN_IN = 1;
    private RelativeLayout rlnotif;
    private TextView textnotif;
    private EditText emailEt, passwordEt;
    private TextView signUpTv, forgotPasswordTv;
    private Button loginBtn, googleSignUpBtn;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private boolean showOneTapUI = true;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private Bundle mSavedInstanceState;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSavedInstanceState = savedInstanceState;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);



        clickListener();

    }

    private void init(View view) {


        emailEt = view.findViewById(R.id.emailET);
        passwordEt = view.findViewById(R.id.passwordET);
        loginBtn = view.findViewById(R.id.recoverBtn);
        googleSignUpBtn = view.findViewById(R.id.googleSignInBtn);
        signUpTv = view.findViewById(R.id.signUpTV);
        forgotPasswordTv = view.findViewById(R.id.forgotTV);
        progressBar = view.findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        rlnotif =view.findViewById(R.id.rlnotif);
        textnotif=view.findViewById(R.id.textnotif);

    }

    private void clickListener(){

        forgotPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ReplacerActivity) getActivity()).setFragment(new ForgotPassword());
            }
        });

        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ReplacerActivity) getActivity()).setFragment(new CreateAccountFragment());
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEt.getText().toString();
                String password = passwordEt.getText().toString();

                if (email.isEmpty() || email.matches(EMAIL_REGEX)){

                    emailEt.setError("Invalid email");
                    notif("Please input a valid email");
                    return;

                }

                if(password.isEmpty() || password.length() < 6){

                    passwordEt.setError("Input 6 digit valid password ");
                    notif("Please input a 6 digit valid password");

                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    FirebaseUser user = auth.getCurrentUser();
                                    if(user.isEmailVerified()){
                                        Toast.makeText(getContext(), "Please verify your email", Toast.LENGTH_SHORT).show();
                                    }

                                    sendUserToMainActivity();

                                }else{
                                    String exception = "Error "+ task.getException().getMessage();
                                    Toast.makeText(getContext(), exception , Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);


                                }

                            }
                        });


            }
        });

        googleSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();

                progressBar.setVisibility(View.VISIBLE);
            }
        });


    }



    private void googleSignIn() {
       Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);





        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = auth.getCurrentUser();
                            updateUi(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                        }

                    }
                });

    }


    private void updateUi(FirebaseUser user){


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());

        List<String> list = new ArrayList<>();
        List<String> list1 = new ArrayList<>();

        Map<String,Object> map = new HashMap<>();

        if(account.getDisplayName()!=null){
        map.put("name", account.getDisplayName());}
        map.put("email", account.getEmail());
        map.put("profileImage", String.valueOf(account.getPhotoUrl()));
        map.put("uid", user.getUid());
        map.put("following",list1);
        map.put("followers", list);
        map.put("status"," ");


        FirebaseFirestore.getInstance().collection("Users").document(user.getUid())
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        if(task.isSuccessful()){
                            assert getActivity()!=null;
                            progressBar.setVisibility(View.GONE);
                            sendUserToMainActivity();

                        }else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Error "+task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }
    private void  sendUserToMainActivity(){

        if(getActivity() == null)
            return;

        progressBar.setVisibility(View.GONE);

        startActivity(new Intent(getContext().getApplicationContext(), MainActivity.class));

        getActivity().finish();

    }



    public void notif(String text) {
        rlnotif.setVisibility(View.VISIBLE);
        textnotif.setText(text);

        new Handler().postDelayed(() -> rlnotif.setVisibility(View.GONE), 3000);
    }

}


