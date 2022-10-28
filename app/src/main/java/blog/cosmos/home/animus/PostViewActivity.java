package blog.cosmos.home.animus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;

import blog.cosmos.home.animus.R;

public class PostViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);



        Intent intent = getIntent();
        String action = intent.getAction();
        Uri uri = intent.getData();

        String scheme = uri.getScheme();
        String host = uri.getHost();
        String path = uri.getPath();
        String query = uri.getQuery();

        try {
            URL url = new URL(scheme + "://" + host+ path.replace("Post Images", "Post%20Images")+"?"+query);

            ImageView imageView = findViewById(R.id.imageView);

            Glide.with(PostViewActivity.this)
                    .load(url.toString())
                    .timeout(6500)
                    .into(imageView);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }
}