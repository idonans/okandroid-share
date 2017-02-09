package com.okandroid.share.weixin;

import android.content.Intent;
import android.os.Bundle;

import com.okandroid.boot.AppContext;
import com.okandroid.share.ShareConfig;
import com.okandroid.share.app.ShareActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 与微信通信页
 * Created by idonans on 2017/2/4.
 */
public class ShareWXEntryActivity extends ShareActivity implements IWXAPIEventHandler {

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
            api.handleIntent(intent, this);
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
        ShareWeixinHelper.getGlobalWXAPIEventHandler().onReq(baseReq);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        ShareWeixinHelper.getGlobalWXAPIEventHandler().onResp(baseResp);
    }

}
