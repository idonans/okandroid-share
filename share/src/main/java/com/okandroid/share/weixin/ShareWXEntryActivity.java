package com.okandroid.share.weixin;

import android.content.Intent;
import android.os.Bundle;

import com.okandroid.boot.AppContext;
import com.okandroid.share.ShareConfig;
import com.okandroid.share.app.BaseActivity;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 与微信通信页
 * Created by idonans on 2017/2/4.
 */
public class ShareWXEntryActivity extends BaseActivity {

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
        if (ShareConfig.hasConfigWeixin()) {
            IWXAPI api = WXAPIFactory.createWXAPI(AppContext.getContext(), ShareConfig.getWeixinAppKey(), false);
            api.handleIntent(intent, ShareWeixinHelper.getGlobalWXAPIEventHandler());
        }
    }

}
