package com.sample.share.module.third.signin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.okandroid.boot.lang.Log;
import com.okandroid.boot.util.IOUtil;
import com.okandroid.boot.util.ViewUtil;
import com.okandroid.share.ShareHelper;
import com.okandroid.share.util.AuthUtil;
import com.sample.share.R;
import com.sample.share.app.BaseActivity;

/**
 * Created by idonans on 2017/2/4.
 */

public class ThirdSignInActivity extends BaseActivity {

    public static Intent startIntent(Context context) {
        Intent starter = new Intent(context, ThirdSignInActivity.class);
        return starter;
    }

    private ShareHelper mShareHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShareHelper = new ShareHelper(this, mAuthListener);

        setContentView(R.layout.sample_activity_third_sign_in);

        View signInWithQQ = ViewUtil.findViewByID(this, R.id.qq);
        signInWithQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithQQ();
            }
        });

        View signInWithWeixin = ViewUtil.findViewByID(this, R.id.weixin);
        signInWithWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithWeixin();
            }
        });

        View signInWithWeibo = ViewUtil.findViewByID(this, R.id.weibo);
        signInWithWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithWeibo();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mShareHelper != null) {
            mShareHelper.resume();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mShareHelper != null) {
            mShareHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        IOUtil.closeQuietly(mShareHelper);
    }

    private boolean signInWithQQ() {
        if (!isAppCompatResumed()) {
            return false;
        }

        return AuthUtil.requestQQAuth(mShareHelper);
    }

    private boolean signInWithWeixin() {
        if (!isAppCompatResumed()) {
            return false;
        }

        return AuthUtil.requestWeixinAuth(mShareHelper);
    }

    private boolean signInWithWeibo() {
        if (!isAppCompatResumed()) {
            return false;
        }

        return AuthUtil.requestWeiboAuth(mShareHelper);
    }

    private ShareHelper.IShareListener mAuthListener = AuthUtil.newAuthListener(new AuthUtil.AuthListener() {

        private static final String TAG = "ThirdSignInActivity#mAuthListener";

        @Override
        public void onQQAuthSuccess(@NonNull AuthUtil.QQAuthInfo info) {
            Log.d(TAG + " onQQAuthSuccess access_token: " + info.access_token);
        }

        @Override
        public void onQQAuthFail() {
            Log.d(TAG + " onQQAuthFail");
        }

        @Override
        public void onQQAuthCancel() {
            Log.d(TAG + " onQQAuthCancel");
        }

        @Override
        public void onWeixinAuthSuccess() {
            Log.d(TAG + " onWeixinAuthSuccess");
        }

        @Override
        public void onWeixinAuthFail() {
            Log.d(TAG + " onWeixinAuthFail");
        }

        @Override
        public void onWeixinAuthCancel() {
            Log.d(TAG + " onWeixinAuthCancel");
        }

        @Override
        public void onWeiboAuthSuccess(@NonNull AuthUtil.WeiboAuthInfo info) {
            Log.d(TAG + " onWeiboAuthSuccess access_token: " + info.access_token);
        }

        @Override
        public void onWeiboAuthFail() {
            Log.d(TAG + " onWeiboAuthFail");
        }

        @Override
        public void onWeiboAuthCancel() {
            Log.d(TAG + " onWeiboAuthCancel");
        }
    });

}
