package blog.cosmos.home.animus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;

public class ExampleActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        int primary = getResources().getColor(R.color.colorPrimary);
        int secondary = getResources().getColor(R.color.teal_200);
        Slidr.attach(this, primary, secondary);
    }


}