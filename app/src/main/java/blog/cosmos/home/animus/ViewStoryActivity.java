package blog.cosmos.home.animus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ViewStoryActivity extends AppCompatActivity {

    VideoView videoView;
    public static final String VIDEO_URL_KEY = "videoURL";
    public static final String VIDEO_NAME_KEY = "videoNAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);

        init();

        String url = getIntent().getStringExtra(VIDEO_URL_KEY);
        String name = getIntent().getStringExtra(VIDEO_NAME_KEY);

        if(url == null || url.isEmpty()){
            finish();
        }

        StorageReference reference = FirebaseStorage.getInstance().getReference().child("Stories/"+ name);

        try {
            File localFile = File.createTempFile("test",".mp4");

            reference.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {

                        videoView.setVideoPath(localFile.getPath());
                        videoView.start();

                    }).addOnFailureListener(e -> {
                        e.printStackTrace();
                        Toast.makeText(ViewStoryActivity.this, "Failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    });


        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    void init(){
        videoView = findViewById(R.id.videoView);
    }

}