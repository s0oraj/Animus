package blog.cosmos.home.animus;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;
import static blog.cosmos.home.animus.utils.Constants.PREF_DIRECTORY;
import static blog.cosmos.home.animus.utils.Constants.PREF_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import blog.cosmos.home.animus.adapter.ViewPagerAdapter;
import blog.cosmos.home.animus.fragments.Add;
import blog.cosmos.home.animus.fragments.Home;
import blog.cosmos.home.animus.fragments.Notification;
import blog.cosmos.home.animus.fragments.Profile;
import blog.cosmos.home.animus.fragments.Search;

public class MainActivity extends AppCompatActivity implements Search.OndataPass {

    ViewPagerAdapter viewPagerAdapter;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;





    public static ViewPager viewPager;

    public static boolean IS_HOME_FRAGMENT = true;



    Home homeFragment;
    Search searchFragment;
    Add addFragment;
    Notification notificationFragment;
    Profile profileFragment;
    MenuItem prevMenuItem;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        //addTabs();
        clickListener();
        setupViewPager();

       // findViewById(R.id.constrainLayout).setBackground(getResources().getDrawable(com.marsad.stylishdialogs.R.color.float_transparent));

    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
                viewPager.setCurrentItem(2);
                IS_HOME_FRAGMENT=false;
                changeStatusBarColor();
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


    public static String USER_ID;
    public static boolean IS_SEARCHED_USER = false;
    public static boolean FROM_MAINACTIVITY_TO_PROFILEFRAGMENT=false;

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
            viewPager.setCurrentItem(0);
            IS_SEARCHED_USER = false;
        } else {
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


}