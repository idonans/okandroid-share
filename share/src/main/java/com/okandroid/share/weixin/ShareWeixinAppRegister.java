package com.okandroid.share.weixin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.okandroid.boot.AppContext;
import com.okandroid.share.ShareConfig;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by idonans on 2017/2/4.
 */

public class ShareWeixinAppRegister extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ShareConfig.hasConfigWeixin()) {
            IWXAPI api = WXAPIFactory.createWXAPI(AppContext.getContext(), ShareConfig.getWeixinAppKey(), false);
            api.registerApp(ShareConfig.getWeixinAppKey());
        }

    }

}
