package com.idonans.ishare.weixin.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.idonans.ishare.weixin.IShareWeixinHelper;
import com.okandroid.boot.util.IOUtil;
import com.okandroid.share.app.BaseActivity;
import com.tencent.mm.sdk.openapi.IWXAPI;

/**
 * 与微信通信页
 * Created by idonans on 2017/2/4.
 */
public class IShareWXEntryActivity extends BaseActivity {

    private IShareWeixinHelper mIShareWeixinHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIShareWeixinHelper = new IShareWeixinHelper(null);
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
        IWXAPI api = mIShareWeixinHelper.getApi();
        if (api != null) {
            api.handleIntent(intent, IShareWeixinHelper.getGlobalWXAPIEventHandler());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IOUtil.closeQuietly(mIShareWeixinHelper);
    }

}
