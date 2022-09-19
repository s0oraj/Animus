package blog.cosmos.home.animus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    TextView nameOfUser;
    TextView emailOfUser;

    Button signOutButton;
    ImageView userImage;
    int count;

    private FirebaseAuth auth;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    GoogleSignInAccount acct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameOfUser = findViewById(R.id.nameOfUser);
        emailOfUser = findViewById(R.id.emailOfUser);
        signOutButton = findViewById(R.id.signOutButton);
        userImage = findViewById(R.id.userImage);
        count =0;

       auth = FirebaseAuth.getInstance();
       updateUI(auth.getCurrentUser());

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

    }



    private void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(MainActivity.this,FragmentReplacerActivity.class));
            }
        });

    }

    public void onButtonClick(View view) {
        count++;
        nameOfUser.setText("Button Clicked!\nButton has been clicked for "+count+" times.");

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {

        gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc= GoogleSignIn.getClient(this,gso);

        acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            String userName= acct.getDisplayName();
            String userEmail = acct.getEmail();
            Uri personImageUri = acct.getPhotoUrl();

            nameOfUser.setText(userName);
            emailOfUser.setText(userEmail);

            Picasso.get().load(personImageUri).into(userImage);

        }
        else{
            startActivity(new Intent(MainActivity.this, FragmentReplacerActivity.class));

           finish();
        }

    }
}