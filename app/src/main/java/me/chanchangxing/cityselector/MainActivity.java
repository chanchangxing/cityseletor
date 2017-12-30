package me.chanchangxing.cityselector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import me.chanchangxing.selector.CitySelector;
import me.chanchangxing.selector.db.Bean;
import me.chanchangxing.selector.ui.OnMoveListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CitySelector
                        .init(MainActivity.this, R.id.parent)
                        .setCurrentItem("140000", "140200")
                        .addListener(new OnMoveListener() {
                            @Override
                            public void onMove(Bean province, Bean city, Bean district) {
//                                Log.e("city", "province =" + province.getFullName() + " city = " + city.getFullName() + " district = " + district.getFullName());
                            }
                        })
                        .build();

            }
        });
    }
}
