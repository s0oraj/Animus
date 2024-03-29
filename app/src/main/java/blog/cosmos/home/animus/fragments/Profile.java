package blog.cosmos.home.animus.fragments;

import static android.app.Activity.RESULT_OK;
import static blog.cosmos.home.animus.MainActivity.IS_SEARCHED_USER;
import static blog.cosmos.home.animus.MainActivity.USER_ID;
import static blog.cosmos.home.animus.utils.Constants.PREF_DIRECTORY;
import static blog.cosmos.home.animus.utils.Constants.PREF_NAME;
import static blog.cosmos.home.animus.utils.Constants.PREF_STORED;
import static blog.cosmos.home.animus.utils.Constants.PREF_URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import blog.cosmos.home.animus.AvatarMaker;
import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.chat.ChatActivity;
import blog.cosmos.home.animus.model.PostImageModel;

import com.google.firebase.storage.UploadTask;
import com.marsad.stylishdialogs.StylishAlertDialog;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends Fragment {

    boolean isMyProfile = true;
    String userUID;
    FirestoreRecyclerAdapter<PostImageModel, PostImageHolder> adapter;
    boolean isFollowed;
    DocumentReference userRef, myRef;
    List<Object> followersList, followingList, followingList_2;
    private TextView nameTv, toolbarNameTv, statusTv, followingCountTv, followersCountTv, postCountTv;
    private CircleImageView profileImage;
    private Button followBtn, startChatBtn;
    private RecyclerView recyclerView;

    private LinearLayout countLayout;
    private ImageButton editProfileBtn;
    private ImageView profileBackBtn;
    private FirebaseUser user;
    private FirebaseAuth auth;

    private Context mContext;
    int count;

    Bitmap userPhoto;





    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        OpenCVLoader.initDebug(); // if this isnt called then this error shows  java.lang.UnsatisfiedLinkError:   No implementation found for long org.opencv.core.Mat.n_Mat()




        myRef = FirebaseFirestore.getInstance().collection("Users")
                .document(user.getUid());


        if (IS_SEARCHED_USER) {
            isMyProfile = false;
            userUID = USER_ID;
           profileBackBtn.setVisibility(View.VISIBLE);
            loadData();

        } else {
            isMyProfile = true;
            userUID = user.getUid();
        }
        if (isMyProfile) {
            editProfileBtn.setVisibility(View.VISIBLE);
            followBtn.setVisibility(View.GONE);
            countLayout.setVisibility(View.VISIBLE);


            //Hide chat btn
            startChatBtn.setVisibility(View.GONE);


        } else {
            editProfileBtn.setVisibility(View.GONE);
            followBtn.setVisibility(View.VISIBLE);
       //     countLayout.setVisibility(View.GONE);
        }
        userRef = FirebaseFirestore.getInstance().collection("Users")
                .document(userUID);

        loadBasicData();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));


        loadPostImages();

        recyclerView.setAdapter(adapter);


        clickListener();

    }

    private void loadData() {
        myRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Tag_b", error.getMessage());
                    return;
                }

                if (value == null || !value.exists()) {
                    return;
                }


                followingList_2 = (List<Object>) value.get("following");
                if(followingList_2==null){
                    followingList_2=new ArrayList<>();
                }


            }
        });
    }

    private void clickListener() {

        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFollowed) {
                    followersList.remove(user.getUid()); // opposite user

                    followingList_2.remove(userUID); //us


                    final Map<String, Object> map_2 = new HashMap<>();
                    map_2.put("following", followingList_2);

                    Map<String, Object> map = new HashMap<>();
                    map.put("followers", followersList);

                    userRef.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                followBtn.setText("Follow");
                                myRef.update(map_2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(mContext, "Unfollowed", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Log.e("Tag_3", task.getException().getMessage());
                                        }
                                    }
                                });

                            } else {
                                Log.e("Tag", "" + task.getException().getMessage());
                            }
                        }
                    });


                } else {

                    createNotification();
                    followersList.add(user.getUid());
                    followingList_2.add(userUID);

                   final Map<String, Object> map_2 = new HashMap<>();
                    map_2.put("following", followingList_2);

                    Map<String, Object> map = new HashMap<>();
                    map.put("followers", followersList);

                    userRef.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                followBtn.setText("Unfollow");

                                myRef.update(map_2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(mContext, "Followed", Toast.LENGTH_SHORT).show();
                                        } else{
                                            Log.e("tag_3_1",task.getException().getMessage());
                                        }
                                    }
                                });

                            } else {
                                Log.e("Tag", "" + task.getException().getMessage());
                            }
                        }
                    });

                }
            }
        });

        assert getContext() != null;

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(getContext(), Profile.this);



                /*Bitmap bitmap =  userPhoto;



                AvatarMaker avatarMaker = new AvatarMaker(bitmap, profileImage
                        ,getActivity());
               profileImage.setImageBitmap(avatarMaker.getBitmap());

                 */
            }
        });

        startChatBtn.setOnClickListener(v -> {
            queryChat();
        });


        profileBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().onBackPressed();

            }
        });

    }


    void queryChat() {

        assert getContext() != null;
        StylishAlertDialog alertDialog = new StylishAlertDialog(getContext(), StylishAlertDialog.PROGRESS);
        alertDialog.setTitleText("Starting Chat...");
        alertDialog.setCancellable(false);
        alertDialog.show();

        CollectionReference reference = FirebaseFirestore.getInstance().collection("Messages");
        reference.whereArrayContains("uid", userUID)
                .get().addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        QuerySnapshot snapshot = task.getResult();

                        if (snapshot.isEmpty()) {
                            startChat(alertDialog);
                        } else {
                            //get chatId and pass
                            alertDialog.dismissWithAnimation();
                            for (DocumentSnapshot snapshotChat : snapshot) {

                                Intent intent = new Intent(getActivity(), ChatActivity.class);
                                intent.putExtra("uid", userUID);
                                intent.putExtra("id", snapshotChat.getId()); //return doc id
                                startActivity(intent);
                            }


                        }

                    } else
                        alertDialog.dismissWithAnimation();

                });

    }


    void startChat(StylishAlertDialog alertDialog) {


        CollectionReference reference = FirebaseFirestore.getInstance().collection("Messages");

        List<String> list = new ArrayList<>();

        list.add(0, user.getUid());
        list.add(1, userUID);

        String pushID = reference.document().getId();

        Map<String, Object> map = new HashMap<>();
        map.put("id", pushID);
        map.put("lastMessage", "Hi");
        map.put("time", FieldValue.serverTimestamp());
        map.put("uid", list);

        reference.document(pushID).update(map).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                reference.document(pushID).set(map);
            }
        });

        //todo - ---- - -- - -
        //Message

        CollectionReference messageRef = FirebaseFirestore.getInstance()
                .collection("Messages")
                .document(pushID)
                .collection("Messages");

        String messageID = messageRef.document().getId();

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("id", messageID);
        messageMap.put("message", "Hi");
        messageMap.put("senderID", user.getUid());
        messageMap.put("time", FieldValue.serverTimestamp());

        messageRef.document(messageID).set(messageMap);

        new Handler().postDelayed(() -> {

            alertDialog.dismissWithAnimation();

            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("uid", userUID);
            intent.putExtra("id", pushID);
            startActivity(intent);

        }, 3000);

    }


    private void init(View view) {

        mContext=getContext();
        profileBackBtn=  view.findViewById(R.id.profileBackBtn);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        assert getActivity() != null;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        nameTv = view.findViewById(R.id.nameTv);
        statusTv = view.findViewById(R.id.statusTV);
        toolbarNameTv = view.findViewById(R.id.toolbarNameTV);
        followersCountTv = view.findViewById(R.id.followersCountTv);
        followingCountTv = view.findViewById(R.id.followingCountTv);
        postCountTv = view.findViewById(R.id.postCountTv);
        profileImage = view.findViewById(R.id.profileImage);
        followBtn = view.findViewById(R.id.followBtn);
        recyclerView = view.findViewById(R.id.recyclerView);
        countLayout = view.findViewById(R.id.countLayout);
        editProfileBtn = view.findViewById(R.id.edit_profileImage);
        startChatBtn = view.findViewById(R.id.startChatBtn);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();



    }

    @SuppressLint("SetTextI18n")
    private void loadBasicData() {

        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.e("Tag 0", error.getMessage());
                    return;
                }

                assert value != null;
                if (value.exists()) {

                    String name = value.getString("name");
                    String status = value.getString("status");

                    final String profileURL = value.getString("profileImage");

                    nameTv.setText(name);
                    toolbarNameTv.setText(name);
                    statusTv.setText(status);

                    followersList = (List<Object>) value.get("followers");
                    followingList = (List<Object>) value.get("following");

                    if(followingList==null){
                        followingList=new ArrayList<>();
                    }
                    if(followersList==null){
                        followersList= new ArrayList<>();
                    }


                    followersCountTv.setText("" + followersList.size());
                    followingCountTv.setText("" + followingList.size());

                    try {


                        Glide.with(getContext().getApplicationContext())
                                .load(profileURL)
                                .placeholder(R.drawable.ic_person)
                                .circleCrop() // makes your profile pic round while you see it in tablayout
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                       Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                                       userPhoto = bitmap;
                                       storeProfileImage(bitmap, profileURL);

                                        return false;
                                    }
                                })
                                .timeout(6500)
                                .into(profileImage);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    if (followersList.contains(user.getUid())) {
                        followBtn.setText("Unfollow");
                        isFollowed = true;

                        startChatBtn.setVisibility(View.VISIBLE);
                    } else {
                        followBtn.setText("Follow");
                        isFollowed = false;

                        startChatBtn.setVisibility(View.GONE);


                    }

                }

            }
        });


    }

    private void storeProfileImage(Bitmap bitmap, String url){

        SharedPreferences preferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean isStored = preferences.getBoolean(PREF_STORED, false);
        String urlString = preferences.getString(PREF_URL, "");

        SharedPreferences.Editor editor = preferences.edit();

        if(isStored && urlString.equals(url)){
            return;
        }

        // Dont save photo or profile if its not current user
        if(IS_SEARCHED_USER)
            return;
        ContextWrapper contextWrapper = new ContextWrapper(getContext().getApplicationContext());

        File directory = contextWrapper.getDir("image_data", Context.MODE_PRIVATE);

        if(!directory.exists()){
            boolean isMade = directory.mkdirs();
            Log.d("Directory", String.valueOf(isMade));
        }


        File path = new File(directory, "profile.png");
        FileOutputStream outputStream = null;

        try{
            outputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } finally {
            try{
                assert outputStream !=null;
                outputStream.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        editor.putBoolean(PREF_STORED, true);
        editor.putString(PREF_URL, url);
        editor.putString(PREF_DIRECTORY, directory.getAbsolutePath());
        editor.apply();

    }

    private void loadPostImages() {


        DocumentReference reference = FirebaseFirestore.getInstance().collection("Users").document(userUID);

        Query query = reference.collection("Post Images");

        FirestoreRecyclerOptions<PostImageModel> options = new FirestoreRecyclerOptions.Builder<PostImageModel>()
                .setQuery(query, PostImageModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<PostImageModel, PostImageHolder>(options) {
            @NonNull
            @Override
            public PostImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_image_items, parent, false);


                return new PostImageHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PostImageHolder holder, int position, @NonNull PostImageModel model) {

                Glide.with(holder.itemView.getContext().getApplicationContext())
                        .load(model.getImageUrl())
                        .timeout(6500)
                        .into(holder.imageView);

                count= getItemCount();
                postCountTv.setText(""+ count);

            }

            @Override
            public int getItemCount() {

                return super.getItemCount();
            }
        };


    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            Uri uri = result.getUri();

            uploadImage(uri);


        }

    }

    public void uploadImage(Uri uri) {
       final StorageReference reference = FirebaseStorage.getInstance().getReference().child("Profile Images");

        reference.putFile(uri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            reference.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageURL = uri.toString();

                                            UserProfileChangeRequest.Builder request = new UserProfileChangeRequest.Builder();
                                            request.setPhotoUri(uri);

                                            user.updateProfile(request.build());

                                            Map<String, Object> map = new HashMap<>();
                                            map.put("profileImage", imageURL);

                                            FirebaseFirestore.getInstance().collection("Users")
                                                    .document(user.getUid())
                                                    .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {


                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                assert task.getException() != null;
                                                                Toast.makeText(getContext(), "Error: " + task.getException().getMessage(),
                                                                        Toast.LENGTH_SHORT).show();

                                                            }

                                                        }
                                                    });


                                        }
                                    });
                        } else {
                            assert task.getException() != null;
                            Toast.makeText(getContext(), "Error: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();


                        }
                    }
                });

    }


    void createNotification() {

        CollectionReference reference = FirebaseFirestore.getInstance().collection("Notifications");

        String id = reference.document().getId();
        Map<String, Object> map = new HashMap<>();
        map.put("time", FieldValue.serverTimestamp());
        map.put("notification", user.getDisplayName() + " followed you.");
        map.put("id", id);
        map.put("uid", userUID);
        map.put("userPhotoUrl",user.getPhotoUrl());


        reference.document(id).set(map);

    }
    private static class PostImageHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public PostImageHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);

        }
    }


}