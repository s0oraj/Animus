package blog.cosmos.home.animus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import java.net.MalformedURLException;
import java.net.URL;

import blog.cosmos.home.animus.R;

public class PostViewActivity extends AppCompatActivity {

    ImageView profileBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        profileBackBtn = findViewById(R.id.profileBackBtn);
        profileBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        Intent intent = getIntent();
        String action = intent.getAction();
        Uri uri = intent.getData();

        String scheme = uri.getScheme();
        String host = uri.getHost();
        String path = uri.getPath();
        String query = uri.getQuery();


        //    URL url = new URL(scheme + "://" + host+ path.replace("Post Images", "Post%20Images")+"?"+query);


            FirebaseStorage.getInstance().getReference().child(uri.getLastPathSegment())
                    .getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            ImageView imageView = findViewById(R.id.imageView);

                            Glide.with(PostViewActivity.this)
                                    .load(uri.toString())
                                    .timeout(6500)
                                    .into(imageView);
                        }
                    });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity( new Intent(PostViewActivity.this, MainActivity.class));
        } else{
            startActivity(new Intent(PostViewActivity.this, ReplacerActivity.class));
        }
    }
}