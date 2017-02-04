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
import com.okandroid.share.util.ShareUtil;
import com.sample.share.R;
import com.sample.share.app.BaseActivity;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.Tencent;

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

        if (mShareHelper == null) {
            return false;
        }

        Tencent tencent = mShareHelper.getShareQQHelper().getTencent(this);
        if (tencent == null) {
            return false;
        }

        tencent.login(this, "get_simple_userinfo", mShareHelper.getShareQQHelper().getListener());
        return true;
    }

    private boolean signInWithWeixin() {
        if (!isAppCompatResumed()) {
            return false;
        }

        if (mShareHelper == null) {
            return false;
        }

        IWXAPI api = mShareHelper.getShareWeixinHelper().getApi();
        if (api == null) {
            return false;
        }

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = mShareHelper.getShareWeixinHelper().getState();
        api.sendReq(req);
        return true;
    }

    private boolean signInWithWeibo() {
        if (!isAppCompatResumed()) {
            return false;
        }

        if (mShareHelper == null) {
            return false;
        }

        SsoHandler ssoHandler = mShareHelper.getShareWeiboHelper().getSsoHandler();
        if (ssoHandler == null) {
            return false;
        }
        ssoHandler.authorize(mShareHelper.getShareWeiboHelper().getListener());
        return true;
    }

    private ShareHelper.IShareListener mAuthListener = ShareUtil.newAuthListener(new ShareUtil.AuthListener() {

        private static final String TAG = "ThirdSignInActivity#mAuthListener";

        @Override
        public void onQQAuthSuccess(@NonNull ShareUtil.QQAuthInfo info) {
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

        }

        @Override
        public void onWeixinAuthFail() {

        }

        @Override
        public void onWeixinAuthCancel() {

        }

        @Override
        public void onWeiboAuthSuccess() {

        }

        @Override
        public void onWeiboAuthFail() {

        }

        @Override
        public void onWeiboAuthCancel() {

        }
    });

}
