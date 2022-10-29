package blog.cosmos.home.animus;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import com.gowtham.library.utils.CompressOption;
import com.gowtham.library.utils.LogMessage;
import com.gowtham.library.utils.TrimType;
import com.gowtham.library.utils.TrimVideo;

public class StoryAddActivity extends AppCompatActivity {


    VideoView videoView;

    private static final int SELECT_VIDEO = 101;

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK &&
                        result.getData() != null) {
                    Uri uri = Uri.parse(TrimVideo.getTrimmedVideoPath(result.getData()));

                    videoView.setVideoURI(uri);
                    videoView.start();

                } else{
                    //  LogMessage.v("videoTrimResultLauncher data is null");
                    Toast.makeText(this, "Data is null", Toast.LENGTH_SHORT).show();
                    finish();

                }

            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_add);

        init();

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, SELECT_VIDEO);

    }

    void init(){

    videoView = findViewById(R.id.videoView);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == SELECT_VIDEO){

            Uri uri = data.getData();

            TrimVideo.activity(String.valueOf(uri))
                    .setCompressOption(new CompressOption())
                    .setTrimType(TrimType.MIN_MAX_DURATION)
                    .setMinToMax(5,30)
                    .setHideSeekBar(true)
                    .start(this,startForResult);
        }


    }






}