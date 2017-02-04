package com.okandroid.share.weixin;

import android.content.Intent;
import android.os.Bundle;

import com.okandroid.boot.util.IOUtil;
import com.okandroid.share.app.BaseActivity;
import com.tencent.mm.sdk.openapi.IWXAPI;

/**
 * 与微信通信页
 * Created by idonans on 2017/2/4.
 */
public class ShareWXEntryActivity extends BaseActivity {

    private ShareWeixinHelper mShareWeixinHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShareWeixinHelper = new ShareWeixinHelper(null);
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
        IWXAPI api = mShareWeixinHelper.getApi();
        if (api != null) {
            api.handleIntent(intent, ShareWeixinHelper.getGlobalWXAPIEventHandler());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IOUtil.closeQuietly(mShareWeixinHelper);
    }

}
