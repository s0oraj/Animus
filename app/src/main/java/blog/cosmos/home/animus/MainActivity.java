package blog.cosmos.home.animus;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

/*
import com.alan.alansdk.AlanCallback;
import com.alan.alansdk.AlanConfig;
import com.alan.alansdk.button.AlanButton;
import com.alan.alansdk.events.EventCommand;
*/

import com.alan.alansdk.AlanCallback;
import com.alan.alansdk.AlanConfig;
import com.alan.alansdk.button.AlanButton;
import com.alan.alansdk.events.EventCommand;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.OpenCVLoader;

import java.util.HashMap;
import java.util.Map;

import blog.cosmos.home.animus.adapter.ViewPagerAdapter;
import blog.cosmos.home.animus.fragments.Add;
import blog.cosmos.home.animus.fragments.BottomSheetDialog;
import blog.cosmos.home.animus.fragments.Comment;
import blog.cosmos.home.animus.fragments.DialogFragment;
import blog.cosmos.home.animus.fragments.Home;
import blog.cosmos.home.animus.fragments.Notification;
import blog.cosmos.home.animus.fragments.Profile;
import blog.cosmos.home.animus.fragments.Search;

public class MainActivity extends AppCompatActivity implements Search.OndataPass {

    public static ViewPager viewPager;

    private AlanButton alanButton;
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

    FirebaseUser user;

    private ConstraintLayout mainScreenNavigationLayout;
    CoordinatorLayout activitymainlayout;

    public MainActivity()
    {
        if (!OpenCVLoader.initDebug())
        {
         //   System.Console.WriteLine("GG");
            System.console().writer().write("GG");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OpenCVLoader.initDebug(); // if this isnt called then this error shows  java.lang.UnsatisfiedLinkError:   No implementation found for long org.opencv.core.Mat.n_Mat()


        /*
       getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

         */

      //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        init();

    //    alanAiConfig();

        //addTabs();
        clickListener();
        setupViewPager();

    }


   private void alanAiConfig() {
        AlanConfig config = AlanConfig.builder().setProjectId("71728597873c05a4aa1c8279b12b57632e956eca572e1d8b807a3e2338fdd0dc/stage").build();
        alanButton = findViewById(R.id.alan_button);
        alanButton.initWithConfig(config);

        AlanCallback alanCallback = new AlanCallback() {
            /// Handle commands from Alan Studio
            @Override
            public void onCommand(final EventCommand eventCommand) {
                try {
                    JSONObject command = eventCommand.getData();
                    String commandName = command.getJSONObject("data").getString("command");
                    Log.d("AlanButton", "onCommand: commandName: " + commandName);
                } catch (JSONException e) {
                    Log.e("AlanButton", e.getMessage());
                }
            }
        };

        /// Register callbacks
        alanButton.registerCallback(alanCallback);
    }



    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();
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

                /*
              viewPager.setCurrentItem(2);
                IS_HOME_FRAGMENT=false;
                changeStatusBarColor();
                */

                GoogleSignInOptions gso;
                GoogleSignInClient gsc;

                gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                gsc= GoogleSignIn.getClient(MainActivity.this,gso);

                gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                      finish();
                        startActivity(new Intent(MainActivity.this,ReplacerActivity.class));
                    }
                });
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

               if (viewPager.getCurrentItem() == 4) {
                  IS_SEARCHED_USER = false;

                  viewPager.setCurrentItem(0);
                  IS_HOME_FRAGMENT=true;
                   changeStatusBarColor();

                 }
               else if(viewPager.getCurrentItem() == 3 ||
                    viewPager.getCurrentItem() == 2 ||
                    viewPager.getCurrentItem() == 1)
               {
                    viewPager.setCurrentItem(0);
                   IS_HOME_FRAGMENT=true;
                   changeStatusBarColor();

                } else{
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


    @Override
    protected void onPause() {
        updateStatus(false);
        super.onPause();
        /*
        IS_HOME_FRAGMENT = false;
        changeStatusBarColor();

         */

    }


    @Override
    protected void onResume() {
        super.onResume();
        updateStatus(true);
        IS_HOME_FRAGMENT = true;
        changeStatusBarColor();
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }


    void updateStatus(boolean status) {

        Map<String, Object> map = new HashMap<>();
        map.put("online", status);

        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(user.getUid())
                .update(map);
    }



}



