package me.chanchangxing.cityselector;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by chenchangxing on 2017/12/25.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
