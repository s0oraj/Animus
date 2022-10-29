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

import com.gowtham.library.utils.CompressOption;
import com.gowtham.library.utils.LogMessage;
import com.gowtham.library.utils.TrimType;
import com.gowtham.library.utils.TrimVideo;

public class StoryAddActivity extends AppCompatActivity {


    private static final int SELECT_VIDEO = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_add);


        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, SELECT_VIDEO);

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



    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK &&
                        result.getData() != null) {
                    Uri uri = Uri.parse(TrimVideo.getTrimmedVideoPath(result.getData()));
                  //  Log.d(TAG, "Trimmed path:: " + uri);

                } else{
                    //  LogMessage.v("videoTrimResultLauncher data is null");
                }

            });


}