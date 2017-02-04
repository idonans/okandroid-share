package com.sample.share.module.third.signin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.okandroid.boot.util.IOUtil;
import com.okandroid.boot.util.ViewUtil;
import com.okandroid.share.ShareHelper;
import com.sample.share.R;
import com.sample.share.app.BaseActivity;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

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
        mShareHelper = new ShareHelper(this, new ShareAdapter());

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

        if (!isAppCompatResumed()) {
            return false;
        }

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

    private class ShareAdapter implements ShareHelper.IShareListener {

        @Override
        public void onQQComplete(Object o) {

        }

        @Override
        public void onQQError(UiError uiError) {

        }

        @Override
        public void onQQCancel() {

        }

        @Override
        public void onWeixinCallback(BaseResp baseResp) {

        }

        @Override
        public void onWeiboAuthComplete(Bundle bundle) {

        }

        @Override
        public void onWeiboAuthException(WeiboException e) {

        }

        @Override
        public void onWeiboAuthCancel() {

        }

        @Override
        public void onWeiboShareCallback(BaseResponse baseResponse) {

        }
    }

}
