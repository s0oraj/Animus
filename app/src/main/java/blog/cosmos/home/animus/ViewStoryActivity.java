package blog.cosmos.home.animus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ViewStoryActivity extends AppCompatActivity {

    public static final String VIDEO_URL_KEY = "videoURL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);

        String url = getIntent().getStringExtra(VIDEO_URL_KEY);




    }
}