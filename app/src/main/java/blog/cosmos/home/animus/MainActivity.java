package blog.cosmos.home.animus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView text;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.textView);
        count =0;

    }

    public void onButtonClick(View view) {
        count++;
        text.setText("Button Clicked!\nButton has been clicked for "+count+" times.");

    }
}