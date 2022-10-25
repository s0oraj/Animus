package blog.cosmos.home.animus.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import blog.cosmos.home.animus.R;


public class DialogFragment2 extends androidx.fragment.app.DialogFragment implements View.OnTouchListener{


    LinearLayout rootLayout;


    float rootLayoutY=0;


    private float oldY = 0;
    private float baseLayoutPosition = 0;
    private float defaultViewHeight = 0;
    private boolean isScrollingUp = false;
    private boolean isScrollingDown = false;

   /* private int previousFingerPosition = 0;
    private int baseLayoutPosition = 0;
    private int defaultViewHeight;

    private boolean isClosing = false;
    private boolean isScrollingUp = false;
    private boolean isScrollingDown = false; */

    private boolean isClosing = false;
    @Override
    public int getTheme() {

         return R.style.NoBackgroundDialogTheme;


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.dialog_fragment,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootLayout = view.findViewById(R.id.linearDialogLayout);
        rootLayout.setOnTouchListener(this);

        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                defaultViewHeight = rootLayout.getHeight();
            }
        });



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


    public void closeUpAndDismissDialog(int currentPosition){
        isClosing = true;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(rootLayout, "y", currentPosition, -rootLayout.getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener()
        {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }

        });
        positionAnimator.start();
    }

    public void closeDownAndDismissDialog(int currentPosition){
        isClosing = true;
        Display display = this.getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(rootLayout, "y", currentPosition, screenHeight+rootLayout.getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener()
        {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
               dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }

        });
        positionAnimator.start();
    }
}