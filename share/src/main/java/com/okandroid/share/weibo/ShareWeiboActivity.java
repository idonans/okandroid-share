package com.okandroid.share.weibo;

import android.content.Intent;
import android.os.Bundle;

import com.okandroid.share.ShareConfig;
import com.okandroid.share.app.ShareActivity;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;

/**
 * 接收微博分享的响应结果
 * Created by idonans on 2017/2/4.
 */
public class ShareWeiboActivity extends ShareActivity implements IWeiboHandler.Response {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if (ShareConfig.hasConfigWeibo()) {
            IWeiboShareAPI api = WeiboShareSDK.createWeiboAPI(this, ShareConfig.getWeiboAppKey(), false);
            api.handleWeiboResponse(intent, this);
        }
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        ShareWeiboHelper.getGlobalWeiboHandlerResponseAdapter().onResponse(baseResponse);
    }

}
