package blog.cosmos.home.animus;

import static blog.cosmos.home.animus.utils.Constants.PREF_DIRECTORY;
import static blog.cosmos.home.animus.utils.Constants.PREF_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import blog.cosmos.home.animus.adapter.ViewPagerAdapter;
import blog.cosmos.home.animus.fragments.Search;

public class MainActivity extends AppCompatActivity implements Search.OndataPass {

    ViewPagerAdapter pagerAdapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();

        addTabs();


    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
    }

    private void addTabs() {

        tabLayout.addTab(tabLayout.newTab().setText("Home").setIcon(R.drawable.ic_home));
        tabLayout.getTabAt(0).getIcon().setColorFilter(
                        ContextCompat.getColor(this, R.color.colorGreyDark),
                        PorterDuff.Mode.SRC_IN);


        tabLayout.addTab(tabLayout.newTab().setText("Search").setIcon(R.drawable.ic_search));
        tabLayout.getTabAt(1).getIcon().setColorFilter(
                ContextCompat.getColor(this, R.color.colorGreyDark),
                PorterDuff.Mode.SRC_IN);


        tabLayout.addTab(tabLayout.newTab().setText("Add").setIcon(R.drawable.ic_add));
        tabLayout.getTabAt(2).getIcon().setColorFilter(
                ContextCompat.getColor(this, R.color.colorGreyDark),
                PorterDuff.Mode.SRC_IN);


        tabLayout.addTab(tabLayout.newTab().setText("Notif").setIcon(R.drawable.ic_heart));
        tabLayout.getTabAt(3).getIcon().setColorFilter(
                ContextCompat.getColor(this, R.color.colorGreyDark),
                PorterDuff.Mode.SRC_IN);


        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String directory = preferences.getString(PREF_DIRECTORY, "");

        Bitmap bitmap = loadProfileImage(directory);
        Drawable drawable= new BitmapDrawable(getResources(),bitmap);


        if(drawable.isVisible()){
            tabLayout.addTab(tabLayout.newTab().setIcon(drawable).setText("Profile"));
        } else{
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_person));
        }





        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_fill)
                .getIcon().setColorFilter(
                        ContextCompat.getColor(this, R.color.black),
                        PorterDuff.Mode.SRC_IN);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Context context = MainActivity.this;


                switch (tab.getPosition()) {

                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_fill)
                                .getIcon().setColorFilter(
                                        ContextCompat.getColor(context, R.color.black),
                                        PorterDuff.Mode.SRC_IN);
                        if(MainActivity.this.getWindow().getStatusBarColor()==getResources().getColor(R.color.white)){
                            MainActivity.this.getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent2Dark));
                        }
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_search)
                                .getIcon().setColorFilter(
                                        ContextCompat.getColor(context, R.color.black),
                                        PorterDuff.Mode.SRC_IN);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_add)
                                .getIcon().setColorFilter(
                                        ContextCompat.getColor(context, R.color.black),
                                        PorterDuff.Mode.SRC_IN);
                        break;
                    case 3:
                        tabLayout.getTabAt(3).setIcon(R.drawable.ic_heart_fill)
                                .getIcon().setColorFilter(
                                        ContextCompat.getColor(context, R.color.black),
                                        PorterDuff.Mode.SRC_IN);
                        break;
                 /*   case 4:
                        tabLayout.getTabAt(4).setIcon(R.drawable.ic_launcher)
                                .getIcon().setColorFilter(
                                        ContextCompat.getColor(context, R.color.black),
                                        PorterDuff.Mode.SRC_IN);
                        break; */
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {

                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home).getIcon()
                                .setColorFilter(  ContextCompat.getColor(MainActivity.this, R.color.colorGreyDark),
                                PorterDuff.Mode.SRC_IN);
                        if(MainActivity.this.getWindow().getStatusBarColor()==getResources().getColor(R.color.colorAccent2Dark)){
                            MainActivity.this.getWindow().setStatusBarColor(getResources().getColor(R.color.white));
                        }
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_search).getIcon()
                                .setColorFilter(  ContextCompat.getColor(MainActivity.this, R.color.colorGreyDark),
                                        PorterDuff.Mode.SRC_IN);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_add).getIcon()
                                .setColorFilter(  ContextCompat.getColor(MainActivity.this, R.color.colorGreyDark),
                                        PorterDuff.Mode.SRC_IN);
                        break;
                    case 3:
                        tabLayout.getTabAt(3).setIcon(R.drawable.ic_heart).getIcon()
                                .setColorFilter(  ContextCompat.getColor(MainActivity.this, R.color.colorGreyDark),
                                        PorterDuff.Mode.SRC_IN);
                        break;
                   /* case 4:
                        tabLayout.getTabAt(4).setIcon(R.drawable.ic_heart_fill);
                        break; */

                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                Context context = MainActivity.this;
                switch (tab.getPosition()) {

                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_fill)
                                .getIcon().setColorFilter(
                                        ContextCompat.getColor(context, R.color.black),
                                        PorterDuff.Mode.SRC_IN);
                        if(MainActivity.this.getWindow().getStatusBarColor()==getResources().getColor(R.color.white)){
                            MainActivity.this.getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent2Dark));
                        }
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_search)
                                .getIcon().setColorFilter(
                                        ContextCompat.getColor(context, R.color.black),
                                        PorterDuff.Mode.SRC_IN);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_add)
                                .getIcon().setColorFilter(
                                        ContextCompat.getColor(context, R.color.black),
                                        PorterDuff.Mode.SRC_IN);
                        break;
                    case 3:
                        tabLayout.getTabAt(3).setIcon(R.drawable.ic_heart_fill)
                                .getIcon().setColorFilter(
                                        ContextCompat.getColor(context, R.color.black),
                                        PorterDuff.Mode.SRC_IN);
                        break;
                 /*   case 4:
                        tabLayout.getTabAt(4).setIcon(R.drawable.ic_launcher)
                                .getIcon().setColorFilter(
                                        ContextCompat.getColor(context, R.color.black),
                                        PorterDuff.Mode.SRC_IN);
                        break; */

                }

            }
        });

    }

    private Bitmap loadProfileImage(String directory){
        try{
            File file = new File(directory, "profile.png");

            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));

           // return BitmapFactory.decodeStream(new FileInputStream(file));
            return bitmap;
        } catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }
    public static String USER_ID;
    public static boolean IS_SEARCHED_USER = false;
    @Override
    public void onChange(String uid) {

        USER_ID = uid;
        IS_SEARCHED_USER = true;

        viewPager.setCurrentItem(4);
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





}