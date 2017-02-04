package com.idonans.ishare.weibo;

import android.content.Intent;
import android.os.Bundle;

import com.okandroid.boot.lang.Log;
import com.okandroid.boot.util.IOUtil;
import com.okandroid.share.app.BaseActivity;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;

/**
 * 接收微博分享的响应结果
 * Created by idonans on 2017/2/4.
 */
public class IShareWeiboActivity extends BaseActivity implements IWeiboHandler.Response {

    private static final String TAG = "IShareWeiboActivity";
    private IShareWeiboHelper mIShareWeiboHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIShareWeiboHelper = new IShareWeiboHelper(this, null, null);
        handleIntent(getIntent());
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
        finish();
    }

    private void handleIntent(Intent intent) {
        IWeiboShareAPI api = mIShareWeiboHelper.getIWeiboShareAPI(false);
        if (api != null) {
            api.handleWeiboResponse(intent, this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IOUtil.closeQuietly(mIShareWeiboHelper);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        Log.d(TAG + " onResponse " + baseResponse);

        IWeiboHandler.Response handler = IShareWeiboHelper.getGlobalWeiboHandlerResponseAdapter();
        handler.onResponse(baseResponse);
    }

}
