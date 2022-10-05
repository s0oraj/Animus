package blog.cosmos.home.animus;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

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

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_search));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_add));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_heart));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_heart_fill));

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
                    case 4:
                        tabLayout.getTabAt(4).setIcon(R.drawable.ic_launcher)
                                .getIcon().setColorFilter(
                                        ContextCompat.getColor(context, R.color.black),
                                        PorterDuff.Mode.SRC_IN);
                        break;
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {

                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_search);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_add);
                        break;
                    case 3:
                        tabLayout.getTabAt(3).setIcon(R.drawable.ic_heart);
                        break;
                    case 4:
                        tabLayout.getTabAt(4).setIcon(R.drawable.ic_heart_fill);
                        break;

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
                    case 4:
                        tabLayout.getTabAt(4).setIcon(R.drawable.ic_launcher)
                                .getIcon().setColorFilter(
                                        ContextCompat.getColor(context, R.color.black),
                                        PorterDuff.Mode.SRC_IN);
                        break;

                }

            }
        });

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