package com.sample.share.module.third.share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.okandroid.boot.lang.Log;
import com.okandroid.boot.util.IOUtil;
import com.okandroid.boot.util.ViewUtil;
import com.okandroid.share.ShareHelper;
import com.okandroid.share.util.ShareUtil;
import com.sample.share.R;
import com.sample.share.app.BaseActivity;

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
        mShareHelper = new ShareHelper(this, mShareListener);

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

        ShareUtil.QQShareContent shareContent = new ShareUtil.QQShareContent();
        shareContent.title = "qq share title";
        shareContent.content = "qq share content";
        shareContent.image = "https://avatars3.githubusercontent.com/u/4043830?v=3&s=460";
        shareContent.targetUrl = "https://github.com/idonans/okandroid-share";
        ShareUtil.shareToQQ(mShareHelper, shareContent);
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

    private ShareHelper.IShareListener mShareListener = ShareUtil.newShareListener(new ShareUtil.ShareListener() {

        private static final String TAG = "ThirdShareActivity#mShareListener";

        @Override
        public void onQQShareSuccess() {
            Log.d(TAG + " onQQShareSuccess");
        }

        @Override
        public void onQQShareFail() {
            Log.d(TAG + " onQQShareFail");
        }

        @Override
        public void onQQShareCancel() {
            Log.d(TAG + " onQQShareCancel");
        }

        @Override
        public void onWeixinShareSuccess() {
            Log.d(TAG + " onWeixinShareSuccess");
        }

        @Override
        public void onWeixinShareFail() {
            Log.d(TAG + " onWeixinShareFail");
        }

        @Override
        public void onWeixinShareCancel() {
            Log.d(TAG + " onWeixinShareCancel");
        }

        @Override
        public void onWeiboShareSuccess() {
            Log.d(TAG + " onWeiboShareSuccess");
        }

        @Override
        public void onWeiboShareFail() {
            Log.d(TAG + " onWeiboShareFail");
        }

        @Override
        public void onWeiboShareCancel() {
            Log.d(TAG + " onWeiboShareCancel");
        }
    });

}
