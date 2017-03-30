package com.sample.share;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by idonans on 2017/2/3.
 */

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }

        AppInit.init(this);
    }

}
