package blog.cosmos.home.animus;

import static blog.cosmos.home.animus.MainActivity.viewPager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import blog.cosmos.home.animus.fragments.CreateAccountFragment;
import blog.cosmos.home.animus.fragments.ForgotPassword;
import blog.cosmos.home.animus.fragments.LoginFragment;
import blog.cosmos.home.animus.fragments.Search;

import static blog.cosmos.home.animus.MainActivity.USER_ID;
import static blog.cosmos.home.animus.MainActivity.IS_SEARCHED_USER;

public class ReplacerActivity extends AppCompatActivity implements Search.OndataPass {


    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replacer);


        frameLayout = findViewById(R.id.frameLayout);

        Intent data = getIntent();
        String desiredFragment = data.getStringExtra("DesiredFragment");
        if(desiredFragment.equals("search")){
            setFragment(new Search());
        } else{
            setFragment(new LoginFragment());
        }




    }

    public void setFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);


        if(fragment instanceof CreateAccountFragment){
            fragmentTransaction.addToBackStack(null);

        }

        if(fragment instanceof ForgotPassword){
            fragmentTransaction.addToBackStack(null);
        }


        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();

    }


    @Override
    public void onChange(String uid) {
        USER_ID = uid;
        IS_SEARCHED_USER = true;
        viewPager.setCurrentItem(4);
        Intent intent = new Intent(ReplacerActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}