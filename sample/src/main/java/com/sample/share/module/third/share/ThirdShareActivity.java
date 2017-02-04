package com.sample.share.module.third.share;

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
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.tauth.UiError;

/**
 * Created by idonans on 2017/2/4.
 */

public class ThirdShareActivity extends BaseActivity {

    public static Intent startIntent(Context context) {
        Intent starter = new Intent(context, ThirdShareActivity.class);
        return starter;
    }


    private ShareHelper mShareHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShareHelper = new ShareHelper(this, new ShareAdapter());

        setContentView(R.layout.sample_activity_third_share);

        View shareWithQQ = ViewUtil.findViewByID(this, R.id.qq);
        shareWithQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWithQQ();
            }
        });

        View shareWithWeixin = ViewUtil.findViewByID(this, R.id.weixin);
        shareWithWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWithWeixin();
            }
        });

        View shareWithWeibo = ViewUtil.findViewByID(this, R.id.weibo);
        shareWithWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWithWeibo();
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

    private boolean shareWithQQ() {
        if (!isAppCompatResumed()) {
            return false;
        }

        return true;
    }

    private boolean shareWithWeixin() {
        if (!isAppCompatResumed()) {
            return false;
        }

        return true;
    }

    private boolean shareWithWeibo() {
        if (!isAppCompatResumed()) {
            return false;
        }

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
