package blog.cosmos.home.animus.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.adapter.ViewPagerAdapter;


import static blog.cosmos.home.animus.MainActivity.IS_HOME_FRAGMENT;
import static blog.cosmos.home.animus.MainActivity.viewPager;


public class MainScreenFragment extends Fragment {

    ViewPagerAdapter viewPagerAdapter;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;





    Home homeFragment;
    Search searchFragment;
    Add addFragment;
    Notification notificationFragment;
    Profile profileFragment;
    MenuItem prevMenuItem;

    Activity activity;

    private FrameLayout frameLayout;
    private ConstraintLayout mainScreenNavigationLayout;
    CoordinatorLayout activitymainlayout;




    public MainScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        init(view);

        //addTabs();
        clickListener();
        setupViewPager();

    }

    private void init(View view) {
        activity= getActivity();


        mainScreenNavigationLayout = view.findViewById(R.id.mainScreenNavigationLayout);



        viewPager = view.findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView)view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(true); // set this to false later if you only want the float button to respond and not area just below it (where add button was previously located)

        //tabLayout = findViewById(R.id.tabLayout);
        floatingActionButton = view.findViewById(R.id.fab);

    }


    private void clickListener(){
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home_navigation_icon:
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
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
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

    public void changeStatusBarColor() {

        View decorView = activity.getWindow().getDecorView();

        if (IS_HOME_FRAGMENT) {

            activity.getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent2Dark));
            // Draw light icons on a dark background color
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        } else {
            activity.getWindow().setStatusBarColor(getResources().getColor(R.color.white));

            // Draw dark icons on a light background color
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

    }

}