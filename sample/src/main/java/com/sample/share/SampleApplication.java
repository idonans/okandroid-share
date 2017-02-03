package com.sample.share;

import android.app.Application;

/**
 * Created by idonans on 2017/2/3.
 */

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppInit.init(this);
    }

}
