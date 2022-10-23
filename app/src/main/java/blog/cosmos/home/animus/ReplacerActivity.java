package blog.cosmos.home.animus;

import static blog.cosmos.home.animus.MainActivity.FROM_MAINACTIVITY_TO_PROFILEFRAGMENT;
import static blog.cosmos.home.animus.MainActivity.viewPager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import blog.cosmos.home.animus.fragments.Comment;
import blog.cosmos.home.animus.fragments.CreateAccountFragment;
import blog.cosmos.home.animus.fragments.ForgotPassword;
import blog.cosmos.home.animus.fragments.LoginFragment;
import blog.cosmos.home.animus.fragments.Profile;
import blog.cosmos.home.animus.fragments.Search;

import static blog.cosmos.home.animus.MainActivity.USER_ID;
import static blog.cosmos.home.animus.MainActivity.IS_SEARCHED_USER;

public class ReplacerActivity extends AppCompatActivity implements Search.OndataPass {


    private FrameLayout frameLayout;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replacer);


        frameLayout = findViewById(R.id.frameLayout);

        boolean isComment = getIntent().getBooleanExtra("isComment", false);

        if(isComment){
            setFragment(new Comment());
        }


        String desiredFragment = getIntent().getStringExtra("DesiredFragment");

        if(desiredFragment!=null)
        {
            if(desiredFragment.equals("search")){
                setFragment(new Search());
            } else if (desiredFragment.equals("otherUsersProfile")){
                setFragment(new Profile());
            }
            else{
                setFragment(new LoginFragment());
            }
        }
        else{
            setFragment(new LoginFragment());
        }



    }

    public void setFragment(Fragment fragment){

        currentFragment= fragment;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);


        if(fragment instanceof CreateAccountFragment){
            fragmentTransaction.addToBackStack(null);

        }

        if(fragment instanceof ForgotPassword){
            fragmentTransaction.addToBackStack(null);
        }


        if(fragment instanceof Profile){
            fragmentTransaction.addToBackStack(null);

        }

        if(fragment instanceof Comment){
            String id = getIntent().getStringExtra("id");
            String uid = getIntent().getStringExtra("uid");

            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("uid", uid);
            fragment.setArguments(bundle);
        }


        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();

    }


    @Override
    public void onChange(String uid) {

        //User is going from search fragment to profile fragment therefore we need to show user profile instead of our profile

        USER_ID = uid;
        IS_SEARCHED_USER = true;

        /*Intent intent = new Intent(ReplacerActivity.this, MainActivity.class);
        startActivity(intent);*/
        /*
        viewPager.setCurrentItem(4);
         */

        setFragment(new Profile());
      // finish();
    }


    @Override
    public void onBackPressed() {

        // If user had opened someones profile from search and then presses back, then IS_SEARCH_USER is set to false,
        // therefore when user opens his/her own profile (main activity profile tab) then he/she sees his own profile
        // and not the profile he previously search about (since IS_SEARCH_USER has a false value)
        if (currentFragment instanceof Profile) {
            //viewPager.setCurrentItem(0);
            IS_SEARCHED_USER = false;

            if(FROM_MAINACTIVITY_TO_PROFILEFRAGMENT){
                FROM_MAINACTIVITY_TO_PROFILEFRAGMENT=false;
                finish();
            }

        }
            super.onBackPressed();

    }
}