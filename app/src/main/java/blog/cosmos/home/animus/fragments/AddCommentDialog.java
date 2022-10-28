package blog.cosmos.home.animus.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onViewCreated(@NonNull View view1, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view1, savedInstanceState);


    }


}