package blog.cosmos.home.animus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrListener;
import com.r0adkll.slidr.model.SlidrPosition;

import blog.cosmos.home.animus.fragments.Add;
import blog.cosmos.home.animus.fragments.Home;

public class ExampleActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        int primary = getResources().getColor(R.color.colorPrimary);
        int secondary = getResources().getColor(R.color.teal_200);
        SlidrConfig.Builder builder = new SlidrConfig.Builder();
        builder =  builder.primaryColor(getResources().getColor(R.color.colorPrimary));
        builder = builder.secondaryColor(getResources().getColor(R.color.teal_200));
        builder = builder.position(SlidrPosition.LEFT );
        builder = builder.position(SlidrPosition.RIGHT );
        builder = builder.position(SlidrPosition.TOP );
        builder = builder.position(SlidrPosition.BOTTOM );
        builder = builder.position(SlidrPosition.VERTICAL );
        builder = builder.position(SlidrPosition.HORIZONTAL );

        builder = builder
                .sensitivity(1f)
                .scrimColor(Color.BLACK)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400)
                .distanceThreshold(0.25f)
                .edge(true).edge(false)
                .edgeSize(0.18f) ;// The % of the screen that counts as the edge, default 18%
        builder       = builder.listener(new SlidrListener() {
            @Override
            public void onSlideStateChanged(int state) {

            }

            @Override
            public void onSlideChange(float percent) {

            }

            @Override
            public void onSlideOpened() {

            }

            @Override
            public boolean onSlideClosed() {
                return false;
            }
        });

        SlidrConfig config =      builder.build();

       SlidrInterface slidrInterface = Slidr.attach(ExampleActivity.this, config);

       slidrInterface.lock();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new Add())
                .commit();
    }


}