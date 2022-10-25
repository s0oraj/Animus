package blog.cosmos.home.animus;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

import blog.cosmos.home.animus.adapter.ViewPagerAdapter;
import blog.cosmos.home.animus.fragments.Add;
import blog.cosmos.home.animus.fragments.Comment;
import blog.cosmos.home.animus.fragments.DialogFragment;
import blog.cosmos.home.animus.fragments.Home;
import blog.cosmos.home.animus.fragments.MainScreenFragment;
import blog.cosmos.home.animus.fragments.Notification;
import blog.cosmos.home.animus.fragments.Profile;
import blog.cosmos.home.animus.fragments.Search;

public class MainActivity extends AppCompatActivity implements Search.OndataPass {

    public static ViewPager viewPager;

    public static String USER_ID;
    public static boolean IS_SEARCHED_USER = false;
    public static boolean FROM_MAINACTIVITY_TO_PROFILEFRAGMENT=false;
    public static boolean IS_HOME_FRAGMENT = true;

    private FrameLayout frameLayout;

    ViewPagerAdapter viewPagerAdapter;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;


    Home homeFragment;
    Search searchFragment;
    Add addFragment;
    Notification notificationFragment;
    Profile profileFragment;
    MenuItem prevMenuItem;


    private ConstraintLayout mainScreenNavigationLayout;
    CoordinatorLayout activitymainlayout;



    View popupView;
    private int previousFingerPosition = 0;
    private int baseLayoutPosition = 0;
    private int defaultViewHeight;

    private boolean isClosing = false;
    private boolean isScrollingUp = false;
    private boolean isScrollingDown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        //addTabs();
        clickListener();
        setupViewPager();

    }
    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        popupView = findViewById(R.id.popUp);

        viewPager = findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(true); // set this to false later if you only want the float button to respond and not area just below it (where add button was previously located)

        //tabLayout = findViewById(R.id.tabLayout);
        floatingActionButton = findViewById(R.id.fab);

    }

    private void clickListener(){
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home_navigation_icon:l:
                            viewPager.setCurrentItem(0);
                                IS_HOME_FRAGMENT=true;
                                changeStatusBarColor();
                                return true;
                            case R.id.search_navigation_icon:
                                viewPager.setCurrentItem(1);
                                IS_HOME_FRAGMENT=false;
                                changeStatusBarColor();
                                return true;
                            case R.id.add_navigation_icon:
                                viewPager.setCurrentItem(2);
                                IS_HOME_FRAGMENT=false;
                                changeStatusBarColor();
                                return true;
                            case R.id.notification_navigation_icon:
                                viewPager.setCurrentItem(3);
                                IS_HOME_FRAGMENT=false;
                                changeStatusBarColor();
                                return true;
                            case R.id.profile_navigation_icon:
                                viewPager.setCurrentItem(4);
                                IS_HOME_FRAGMENT=false;
                                changeStatusBarColor();
                                return true;

                        }
                        return false;
                    }


                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {



            }

            @Override
            public void onPageSelected(int position) {
              /*  if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
               */

                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.home_navigation_icon).setChecked(true);
                        IS_HOME_FRAGMENT=true;
                        changeStatusBarColor();
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.search_navigation_icon).setChecked(true);
                        IS_HOME_FRAGMENT=false;
                        changeStatusBarColor();
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.add_navigation_icon).setChecked(true);
                        IS_HOME_FRAGMENT=false;
                        changeStatusBarColor();
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.notification_navigation_icon).setChecked(true);
                        IS_HOME_FRAGMENT=false;
                        changeStatusBarColor();
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.profile_navigation_icon).setChecked(true);
                        IS_HOME_FRAGMENT=false;
                        changeStatusBarColor();
                        break;



                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* viewPager.setCurrentItem(2);
                IS_HOME_FRAGMENT=false;
                changeStatusBarColor();

                */

                //onShowPopup(popupView);


               //startActivity(new Intent(MainActivity.this, PopUpActivity.class));

                DialogFragment dialogFragment=new DialogFragment();
                dialogFragment.show(getSupportFragmentManager(),"My  Fragment");
            }
        });
    }



    private void setupViewPager()
    {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        homeFragment=new Home();
        searchFragment=new Search();
        addFragment=new Add();
        notificationFragment= new Notification();
        profileFragment= new Profile();
        viewPagerAdapter.addFragment(homeFragment);
        viewPagerAdapter.addFragment(searchFragment);
        viewPagerAdapter.addFragment(addFragment);
        viewPagerAdapter.addFragment(notificationFragment);
        viewPagerAdapter.addFragment(profileFragment);
        viewPager.setAdapter(viewPagerAdapter);
    }


    public void setFragment(Fragment fragment){

        Fragment currentFragment= fragment;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);




        if(fragment instanceof MainScreenFragment){
            fragmentTransaction.addToBackStack(null);

        }



        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();

    }


    public void setCommentFragment(Fragment fragment, Bundle bundle){

        Fragment currentFragment= fragment;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

        if(fragment instanceof Comment){
            String id = bundle.getString("id");
            String uid = bundle.getString("uid");

            Bundle bundle1 = new Bundle();
            bundle1.putString("id", id);
            bundle1.putString("uid", uid);
            fragment.setArguments(bundle1);
        }

        if(fragment instanceof Comment){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(frameLayout.getId(), fragment);


      //  activitymainlayout.setVisibility(View.INVISIBLE);
      //  activitymainlayout.setForeground(getResources().getDrawable(R.color.white));

        fragmentTransaction.commit();
        IS_HOME_FRAGMENT=false;
        changeStatusBarColor();


    }


    @Override
    public void onChange(String uid) {

        USER_ID = uid;
        IS_SEARCHED_USER = true;

        // viewPager.setCurrentItem(4);

        Intent intent = new Intent(MainActivity.this, ReplacerActivity.class);
        intent.putExtra("DesiredFragment","otherUsersProfile");
        FROM_MAINACTIVITY_TO_PROFILEFRAGMENT= true;
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() ==1) {
               if (viewPager.getCurrentItem() == 4) {
                  IS_SEARCHED_USER = false;

                  viewPager.setCurrentItem(0);
                  IS_HOME_FRAGMENT=true;
                   changeStatusBarColor();


                 } else if(viewPager.getCurrentItem() == 3 ||
                    viewPager.getCurrentItem() == 2 ||
                    viewPager.getCurrentItem() == 1){
                    viewPager.setCurrentItem(0);
                   IS_HOME_FRAGMENT=true;
                   changeStatusBarColor();

                } else{
                   finish();
                    }


        }else{
            super.onBackPressed();
        }


    }

    public void changeStatusBarColor() {

        View decorView = MainActivity.this.getWindow().getDecorView();

        if (IS_HOME_FRAGMENT) {

            MainActivity.this.getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent2Dark));
            // Draw light icons on a dark background color
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        } else {
            MainActivity.this.getWindow().setStatusBarColor(getResources().getColor(R.color.white));

            // Draw dark icons on a light background color
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

    }

    /*
    @Override
    protected void onPause() {
        super.onPause();
        IS_HOME_FRAGMENT = false;
        changeStatusBarColor();
    }
    */

    @Override
    protected void onResume() {
        super.onResume();
        IS_HOME_FRAGMENT = true;
        changeStatusBarColor();
    }

    // call this method when required to show popup
    public void onShowPopup(View v){

        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      //  View popup = v;

        // inflate the custom popup layout
       View inflatedView = layoutInflater.inflate(R.layout.popup_layout, null,false);
        // find the ListView in the popup layout
        ListView listView = (ListView)inflatedView.findViewById(R.id.commentsListView);
        LinearLayout headerView = (LinearLayout)inflatedView.findViewById(R.id.headerLayout);
        // get device size
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
//        mDeviceHeight = size.y;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;


        // fill the data to the list items
        setSimpleList(listView);


        // set height depends on the device size
        PopupWindow popWindow = new PopupWindow(inflatedView, width,height-50, true );
        // set a background drawable with rounders corners
        popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_bg));

        popWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        popWindow.setAnimationStyle(R.style.PopupAnimation);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
               /* if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    popWindow.dismiss();
                } */

                // Get finger position on screen
                final int Y = (int) event.getRawY();

                // Switch on motion event type
                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        // save default base layout height
                        defaultViewHeight = v.getHeight();

                        // Init finger and view position
                        previousFingerPosition = Y;
                        baseLayoutPosition = (int) popupView.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        // If user was doing a scroll up
                        if(isScrollingUp){
                            // Reset baselayout position
                            popupView.setY(0);
                            // We are not in scrolling up mode anymore
                            isScrollingUp = false;
                        }

                        // If user was doing a scroll down
                        if(isScrollingDown){
                            // Reset baselayout position
                            popupView.setY(0);
                            // Reset base layout size
                            popupView.getLayoutParams().height = defaultViewHeight;
                            popupView.requestLayout();
                            // We are not in scrolling down mode anymore
                            isScrollingDown = false;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(!isClosing){
                            int currentYPosition = (int) popupView.getY();

                            // If we scroll up
                            if(previousFingerPosition >Y){
                                // First time android rise an event for "up" move
                                if(!isScrollingUp){
                                    isScrollingUp = true;
                                }

                                // Has user scroll down before -> view is smaller than it's default size -> resize it instead of change it position
                                if(popupView.getHeight()<defaultViewHeight){
                                    popupView.getLayoutParams().height = popupView.getHeight() - (Y - previousFingerPosition);
                                    popupView.requestLayout();
                                }
                                else {
                                    // Has user scroll enough to "auto close" popup ?
                                    if ((baseLayoutPosition - currentYPosition) > defaultViewHeight / 4) {
                                       closeUpAndDismissDialog(currentYPosition);
                                       // popWindow.dismiss();
                                        return true;
                                    }

                                    //
                                }
                                popupView.setY(popupView.getY() + (Y - previousFingerPosition));

                            }
                            // If we scroll down
                            else{

                                // First time android rise an event for "down" move
                                if(!isScrollingDown){
                                    isScrollingDown = true;
                                }

                                // Has user scroll enough to "auto close" popup ?
                                if (Math.abs(baseLayoutPosition - currentYPosition) > defaultViewHeight / 2)
                                {
                                    closeDownAndDismissDialog(currentYPosition);
                                    //popWindow.dismiss();
                                    return true;
                                }

                                // Change base layout size and position (must change position because view anchor is top left corner)
                                popupView.setY(popupView.getY() + (Y - previousFingerPosition));
                                popupView.getLayoutParams().height = popupView.getHeight() - (Y - previousFingerPosition);
                                popupView.requestLayout();
                            }

                            // Update position
                            previousFingerPosition = Y;
                        }
                        break;
                }





                return true;
            }
        });

       // findViewById(R.id.popUp).setVisibility(View.VISIBLE);
        // show the popup at bottom of the screen and set some margin at bottom ie,
        popWindow.showAtLocation(v, Gravity.BOTTOM, 0,100);
    }
    void setSimpleList(ListView listView){

        ArrayList<String> contactsList = new ArrayList<String>();

        for (int index = 0; index < 10; index++) {
            contactsList.add("I am @ index " + index + " today " + Calendar.getInstance().getTime().toString());
        }

       // listView.setAdapter(new ArrayAdapter<String>(MainActivity.this,
           //     R.layout.popup_list_item, android.R.id.text1, contactsList));
    }


    public void closeUpAndDismissDialog(int currentPosition){
        isClosing = true;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(popupView, "y", currentPosition, -popupView.getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener()
        {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                finish();
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
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(popupView, "y", currentPosition, screenHeight+popupView.getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener()
        {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                finish();
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



