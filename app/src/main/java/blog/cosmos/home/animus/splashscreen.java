package blog.cosmos.home.animus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class splashscreen extends AppCompatActivity {

    private static int SPLASH_TIMER=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent=new Intent(splashscreen.this, blog.cosmos.home.animus.MainActivity.class);
                startActivity(intent);
                finish();


            }
        },SPLASH_TIMER);





    }
}