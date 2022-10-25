package blog.cosmos.home.animus.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


    RelativeLayout rootLayout;


    RecyclerView recyclerView;
    private EditText userMsgEdt;
    private  boolean isScrollToLastRequired = false;
    CommentAdapter commentAdapter;

    List<CommentModel> list;

    float rootLayoutY=0;
    private float oldY = 0;
    private float baseLayoutPosition = 0;
    private float defaultViewHeight = 0;
    private boolean isScrollingUp = false;
    private boolean isScrollingDown = false;


    @Override
    public int getTheme() {

         return R.style.NoBackgroundDialogTheme2;


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View view = inflater.inflate(R.layout.dialog_fragment,container,false);
        userMsgEdt = view.findViewById(R.id.commentET);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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

        rootLayout = view1.findViewById(R.id.linearDialogLayout);
        rootLayout.setOnTouchListener(this);

        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                defaultViewHeight = rootLayout.getHeight();
            }
        });


        recyclerView = view1.findViewById(R.id.commentRecyclerView);
        list= new ArrayList<>();
        commentAdapter = new CommentAdapter(getContext(), list);
        recyclerView.setAdapter(commentAdapter);
        userMsgEdt = view1.findViewById(R.id.commentET);

        //Setup editText behavior for opening soft keyboard
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

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


        userMsgEdt.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(userMsgEdt, 0);
            }
        }, 300);
        userMsgEdt.requestFocus();



    }
    public boolean onTouch(View view, MotionEvent event) {

        // Get finger position on screen
        final int Y = (int) event.getRawY();

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


}