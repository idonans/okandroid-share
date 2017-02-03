package com.okandroid.share;

/**
 * Created by idonans on 2017/2/3.
 */
public class Share {

    private static class InstanceHolder {
        private static final Share sInstance = new Share();
    }

    public static Share getInstance() {
        return InstanceHolder.sInstance;
    }

    private Share() {
    }

}