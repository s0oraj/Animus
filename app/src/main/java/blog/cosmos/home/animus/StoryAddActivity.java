package blog.cosmos.home.animus;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gowtham.library.utils.CompressOption;
import com.gowtham.library.utils.LogMessage;
import com.gowtham.library.utils.TrimType;
import com.gowtham.library.utils.TrimVideo;

import java.util.HashMap;
import java.util.Map;

public class StoryAddActivity extends AppCompatActivity {


    VideoView videoView;
    FirebaseUser user;

    private static final int SELECT_VIDEO = 101;

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK &&
                        result.getData() != null) {
                    Uri uri = Uri.parse(TrimVideo.getTrimmedVideoPath(result.getData()));

                    videoView.setVideoURI(uri);
                    videoView.start();

                    uploadVideoToStorage(uri);

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

     void uploadVideoToStorage(Uri uri){

         StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Stories");
         storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                 if(task.isSuccessful()){

                     task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                         @Override
                         public void onSuccess(Uri uri) {



                         }
                     });


                 } else{
                     String error = task.getException().getMessage();
                     Toast.makeText(StoryAddActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
                 }

             }
         });
     }

     void uploadVideoDataToFirestore(String url){
         CollectionReference reference = FirebaseFirestore.getInstance().collection("Users").document(user.getUid())
                 .collection("Stories");

         String id = reference.document().getId();

        Map<String, Object> map = new HashMap<>();
        map.put("videoUrl", url);
        map.put("id",id);
        map.put("uid",user.getUid());
        map.put("name",user.getDisplayName());

        reference.document(id)
                .set(map);

     }

    void init(){

    videoView = findViewById(R.id.videoView);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


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