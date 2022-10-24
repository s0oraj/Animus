package blog.cosmos.home.animus.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import blog.cosmos.home.animus.MainActivity;
import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.adapter.CommentAdapter;
import blog.cosmos.home.animus.model.CommentModel;


public class Comment extends Fragment {


    Activity activity;
    EditText commentEt;
    ImageButton sendBtn;
    RecyclerView recyclerView;

    CommentAdapter commentAdapter;
    private EditText userMsgEdt;

    private  boolean isScrollToLastRequired = false;

    List<CommentModel> list;

    FirebaseUser user;

    String id,uid;

    CollectionReference reference;

    public Comment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);



    /*    activity.getWindow()
                .setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); */

        Log.d("LOG",uid + "\n\n" + id);

          reference = FirebaseFirestore.getInstance().collection("Users")
                .document(uid)
                .collection("Post Images")
                .document(id)
                .collection("Comments");

        loadCommentData();

        clickListener();
    }

    private void clickListener() {

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = commentEt.getText().toString();

                if (comment.isEmpty() || comment.equals(" ")) {
                    Toast.makeText(getContext(), "Can not send empty comment", Toast.LENGTH_SHORT).show();
                    return;
                }


                String commentID = reference.document().getId();

                Map<String, Object> map = new HashMap<>();
                map.put("uid", user.getUid());
                map.put("comment", comment);
                map.put("commentID", commentID);
                map.put("postID", id);
                map.put("name", user.getDisplayName());
                map.put("profileImageUrl", user.getPhotoUrl().toString());


                reference.document(commentID)
                        .set(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    commentEt.setText("");
                                } else {
                                    Toast.makeText(getContext(), "Failed to comment: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        });





            }
        });




    }

    private void loadCommentData() {

        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    return;
                }

                if(value == null){
                    return;
                }

                for(DocumentSnapshot snapshot: value){
                    CommentModel model = snapshot.toObject(CommentModel.class);
                    list.add(model);
                }
                commentAdapter.notifyDataSetChanged();
            }
        });

    }

    private void init(View view1) {

        activity= getActivity();

        commentEt = view1.findViewById(R.id.commentET);
        sendBtn = view1.findViewById(R.id.sendBtn);
        recyclerView = view1.findViewById(R.id.commentRecyclerView);

        user= FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list= new ArrayList<>();
        commentAdapter = new CommentAdapter(getContext(), list);
        recyclerView.setAdapter(commentAdapter);

        userMsgEdt = view1.findViewById(R.id.commentET);

        if(getArguments() == null){
            return;
        }

        id = getArguments().getString("id");
        uid = getArguments().getString("uid");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);



        //Setup editText behavior for opening soft keyboard
        userMsgEdt.setOnTouchListener((view, motionEvent) -> {
            InputMethodManager keyboard = (InputMethodManager) getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (keyboard != null) {
                isScrollToLastRequired = linearLayoutManager.findLastVisibleItemPosition() == commentAdapter.getItemCount() - 1;
                keyboard.showSoftInput(view1.findViewById(R.id.sendBtn), InputMethodManager.SHOW_FORCED);
            }
            return false;
        });
        //Executes recycler view scroll if required.
        recyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom && isScrollToLastRequired) {
                recyclerView.postDelayed(() -> recyclerView.scrollToPosition(
                        recyclerView.getAdapter().getItemCount() - 1), 100);
            }
        });


    }
}