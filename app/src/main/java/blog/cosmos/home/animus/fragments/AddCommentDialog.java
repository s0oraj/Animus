package blog.cosmos.home.animus.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.adapter.CommentAdapter;
import blog.cosmos.home.animus.model.CommentModel;

public class AddCommentDialog extends BottomSheetDialogFragment {

    RelativeLayout rootLayout;
    View viewReference;
    float rootLayoutY=0;
    private float oldY = 0;
    private float baseLayoutPosition = 0;
    private float defaultViewHeight = 0;
    private boolean isScrollingUp = false;
    private boolean isScrollingDown = false;



    RecyclerView recyclerView;
    Activity activity;
    EditText commentEt;
    ImageButton sendBtn;
    ImageView dialogBackButton;
    FirebaseUser user;
    String id,uid;
    CollectionReference reference;


    private EditText userMsgEdt;
    private  boolean isScrollToLastRequired = false;
    CommentAdapter commentAdapter;
    List<CommentModel> list;

    public AddCommentDialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.add_comment_layout,
                container, false);
        userMsgEdt = view.findViewById(R.id.commentET);


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onViewCreated(@NonNull View view1, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view1, savedInstanceState);

        init();
        clickListener();


    }

    private void clickListener() {
        userMsgEdt.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(userMsgEdt, 0);
            }
        }, 300);
        userMsgEdt.requestFocus();


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                String comment = commentEt.getText().toString();
                commentEt.setText("");

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
                map.put("timestamp", FieldValue.serverTimestamp());


                reference.document(commentID)
                        .set(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    commentEt.setText("");
                                    dismiss();
                                    Toast.makeText(getContext(), "Comment added " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();



                                } else {
                                    commentEt.setText(comment);
                                    Toast.makeText(getContext(), "Failed to comment: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        });





            }
        });
    }

    private void init() {
        user= FirebaseAuth.getInstance().getCurrentUser();
        commentEt = getView().findViewById(R.id.commentET);
        userMsgEdt = getView().findViewById(R.id.commentET);
        sendBtn = getView().findViewById(R.id.sendBtn);
        id = getArguments().getString("id");
        uid = getArguments().getString("uid");
        reference = FirebaseFirestore.getInstance().collection("Users")
                .document(uid)
                .collection("Post Images")
                .document(id)
                .collection("Comments");
    }


    private void setUpKeyboard(){
       //Setup editText behavior for opening soft keyboard
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);



        /*
        userMsgEdt.setOnTouchListener((view, motionEvent) -> {
            InputMethodManager keyboard = (InputMethodManager) getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (keyboard != null) {
                keyboard.showSoftInput(getView().findViewById(R.id.sendBtn), InputMethodManager.SHOW_FORCED);
            }
            return false;
        });


         */

    }

    @Override
    public void onResume() {
        super.onResume();
        setUpKeyboard();
    }


}