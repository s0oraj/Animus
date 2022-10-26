package blog.cosmos.home.animus.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.adapter.CommentAdapter;
import blog.cosmos.home.animus.model.CommentModel;

/**
 *  Source https://stackoverflow.com/questions/27246981/android-floating-activity-with-dismiss-on-swipe
 *  a combination of two answers were used here. Answers from Gaëtan Maisse and Zain
 */
public class DialogFragment extends androidx.fragment.app.DialogFragment implements View.OnTouchListener{

    Toolbar rootLayout;
    View viewReference;
    float rootLayoutY=0;
    private float oldY = 0;
    private float baseLayoutPosition = 0;
    private float defaultViewHeight = 0;
    private boolean isScrollingUp = false;
    private boolean isScrollingDown = false;



    public float rawY;

    RecyclerView recyclerView;
    Activity activity;
    EditText commentEt;
    ImageButton sendBtn;
    FirebaseUser user;
    String id,uid;
    CollectionReference reference;


    private EditText userMsgEdt;
    private  boolean isScrollToLastRequired = false;
    CommentAdapter commentAdapter;
    List<CommentModel> list;

    public DialogFragment() {
        // Required empty public constructor
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View view = inflater.inflate(R.layout.dialog_fragment,container,false);
        userMsgEdt = view.findViewById(R.id.commentET);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        view.setBackgroundResource(R.drawable.rounded_dialog);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onViewCreated(@NonNull View view1, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view1, savedInstanceState);

        init(view1);
        clickListener(view1);

        reference = FirebaseFirestore.getInstance().collection("Users")
                .document(uid)
                .collection("Post Images")
                .document(id)
                .collection("Comments");

        loadCommentData();


    }



    private void init(View view) {


        activity= getActivity();

        commentEt = view.findViewById(R.id.commentET);
        sendBtn = view.findViewById(R.id.sendBtn);

        user= FirebaseAuth.getInstance().getCurrentUser();



        viewReference= view;
        rootLayout = view.findViewById(R.id.toolbar);
        recyclerView = view.findViewById(R.id.commentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list= new ArrayList<>();
        commentAdapter = new CommentAdapter(getContext(), list);
        recyclerView.setAdapter(commentAdapter);
        userMsgEdt = view.findViewById(R.id.commentET);


        if(getArguments() == null){
            return;
        }

        id = getArguments().getString("id");
        uid = getArguments().getString("uid");




    }
    private void clickListener(View view1) {
        rootLayout.setOnTouchListener(this);

        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                defaultViewHeight = rootLayout.getHeight();
            }
        });


        setUpKeyboard();

        userMsgEdt.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(userMsgEdt, 0);
            }
        }, 300);
        userMsgEdt.requestFocus();


        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return setUpSwipe(view,motionEvent);
            }
        });

    }

    private boolean setUpSwipe(View view, MotionEvent motionEvent){
        // Get finger position on screen
        final int Y = (int)rawY;

        // Switch on motion event type
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                // save default base layout height
                defaultViewHeight = rootLayout.getHeight();

                // Init finger and view position
                oldY = Y;
                baseLayoutPosition = (int) rootLayout.getY();
                break;

            case MotionEvent.ACTION_UP:


                defaultViewHeight = rootLayout.getHeight();
                if (rootLayoutY >= defaultViewHeight / 2) {
                    dismiss();
                    return true;
                }

                // If user was doing a scroll up
                if(isScrollingUp){
                    // Reset baselayout position
                    rootLayout.setY(0);
                    // We are not in scrolling up mode anymore
                    isScrollingUp = false;
                }

                // If user was doing a scroll down
                if(isScrollingDown){
                    // Reset baselayout position
                    rootLayout.setY(0);
                    // Reset base layout size
                    rootLayout.getLayoutParams().height = (int) defaultViewHeight;
                    rootLayout.requestLayout();
                    // We are not in scrolling down mode anymore
                    isScrollingDown = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:


                rootLayoutY = Math.abs(rootLayout.getY());
                rootLayout.setY( rootLayout.getY() + (Y - oldY));

                if(oldY > Y){
                    if(!isScrollingUp) isScrollingUp = true;
                } else{
                    if(!isScrollingDown) isScrollingDown = true;
                }
                oldY = Y;

                break;
        }
        return true;
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

    public boolean onTouch(View view, MotionEvent event) {

        // Get finger position on screen
        final int Y = (int) event.getRawY();
        rawY= Y;

        // Switch on motion event type
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                // save default base layout height
                defaultViewHeight = rootLayout.getHeight();

                // Init finger and view position
                oldY = Y;
                baseLayoutPosition = (int) rootLayout.getY();
                break;

            case MotionEvent.ACTION_UP:


                defaultViewHeight = rootLayout.getHeight();
                if (rootLayoutY >= defaultViewHeight / 2) {
                    dismiss();
                    return true;
                }

                // If user was doing a scroll up
                if(isScrollingUp){
                    // Reset baselayout position
                    rootLayout.setY(0);
                    // We are not in scrolling up mode anymore
                    isScrollingUp = false;
                }

                // If user was doing a scroll down
                if(isScrollingDown){
                    // Reset baselayout position
                    rootLayout.setY(0);
                    // Reset base layout size
                    rootLayout.getLayoutParams().height = (int) defaultViewHeight;
                    rootLayout.requestLayout();
                    // We are not in scrolling down mode anymore
                    isScrollingDown = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:


                rootLayoutY = Math.abs(rootLayout.getY());
                rootLayout.setY( rootLayout.getY() + (Y - oldY));

                if(oldY > Y){
                    if(!isScrollingUp) isScrollingUp = true;
                } else{
                    if(!isScrollingDown) isScrollingDown = true;
                }
                oldY = Y;

                break;
        }
        return true;
    }

    private void setUpKeyboard(){
        //Setup editText behavior for opening soft keyboard
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

        userMsgEdt.setOnTouchListener((view, motionEvent) -> {
            InputMethodManager keyboard = (InputMethodManager) getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (keyboard != null) {
                isScrollToLastRequired = linearLayoutManager.findLastVisibleItemPosition() == commentAdapter.getItemCount() - 1;
                keyboard.showSoftInput(viewReference.findViewById(R.id.sendBtn), InputMethodManager.SHOW_FORCED);
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

    @Override
    public int getTheme() {
        return R.style.NoBackgroundDialogTheme;

    }

    @Override
    public void onResume() {
        super.onResume();
       setUpKeyboard();
    }
}