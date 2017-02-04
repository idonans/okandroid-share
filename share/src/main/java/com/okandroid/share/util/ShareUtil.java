package com.okandroid.share.util;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.okandroid.boot.lang.Log;
import com.okandroid.share.ShareHelper;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * Created by idonans on 2017/2/4.
 */

public class ShareUtil {

    private ShareUtil() {
    }

    public static boolean requestQQAuth(ShareHelper shareHelper) {
        if (shareHelper == null) {
            return false;
        }

        Tencent tencent = shareHelper.getShareQQHelper().getTencent(shareHelper.getActivity());
        if (tencent == null) {
            return false;
        }

        tencent.login(shareHelper.getActivity(), "get_simple_userinfo", shareHelper.getShareQQHelper().getListener());
        return true;
    }

    public static boolean requestWeixinAuth(ShareHelper shareHelper) {
        if (shareHelper == null) {
            return false;
        }

        IWXAPI api = shareHelper.getShareWeixinHelper().getApi();
        if (api == null) {
            return false;
        }

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = shareHelper.getShareWeixinHelper().getState();
        api.sendReq(req);
        return true;
    }

    public static boolean requestWeiboAuth(ShareHelper shareHelper) {
        if (shareHelper == null) {
            return false;
        }

        SsoHandler ssoHandler = shareHelper.getShareWeiboHelper().getSsoHandler();
        if (ssoHandler == null) {
            return false;
        }
        ssoHandler.authorize(shareHelper.getShareWeiboHelper().getListener());
        return true;
    }

    public interface AuthListener {
        void onQQAuthSuccess(@NonNull QQAuthInfo info);

        void onQQAuthFail();

        void onQQAuthCancel();

        void onWeixinAuthSuccess();

        void onWeixinAuthFail();

        void onWeixinAuthCancel();

        void onWeiboAuthSuccess();

        void onWeiboAuthFail();

        void onWeiboAuthCancel();
    }

    public static class QQAuthInfo {
        public int ret;
        public String pay_token;
        public String pf;
        public int query_authority_cost;
        public int authority_cost;
        public long expires_in;
        public String openid;
        public String pfkey;
        public String msg;
        public int login_cost;
        public String access_token;
    }

    public static ShareHelper.IShareListener newAuthListener(final AuthListener authListener) {
        final String TAG = "ShareUtil#newAuthListener";

        return new ShareHelper.IShareListener() {
            @Override
            public void onQQComplete(Object o) {
                if (o instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) o;
                    QQAuthInfo info = new QQAuthInfo();
                    info.ret = jsonObject.optInt("ret");
                    info.pay_token = jsonObject.optString("pay_token");
                    info.pf = jsonObject.optString("pf");
                    info.query_authority_cost = jsonObject.optInt("query_authority_cost");
                    info.authority_cost = jsonObject.optInt("authority_cost");
                    info.expires_in = jsonObject.optLong("expires_in");
                    info.openid = jsonObject.optString("openid");
                    info.pfkey = jsonObject.optString("pfkey");
                    info.msg = jsonObject.optString("msg");
                    info.login_cost = jsonObject.optInt("login_cost");
                    info.access_token = jsonObject.optString("access_token");
                    authListener.onQQAuthSuccess(info);
                } else {
                    Log.d(TAG + " onQQComplete but not JSONObject " + o);
                }
            }

            @Override
            public void onQQError(UiError uiError) {
                if (uiError == null) {
                    Log.d(TAG + " onQQError uiError is null");
                } else {
                    Log.d(TAG + " onQQError " + uiError.errorCode + " " + uiError.errorMessage + " " + uiError.errorDetail);
                }
                authListener.onQQAuthFail();
            }

            @Override
            public void onQQCancel() {
                authListener.onQQAuthCancel();
            }

            @Override
            public void onWeixinCallback(BaseResp baseResp) {
                // TODO
            }

            @Override
            public void onWeiboAuthComplete(Bundle bundle) {
                // TODO
            }

            @Override
            public void onWeiboAuthException(WeiboException e) {
                authListener.onWeiboAuthFail();
            }

            @Override
            public void onWeiboAuthCancel() {
                authListener.onWeiboAuthCancel();
            }

            @Override
            public void onWeiboShareCallback(BaseResponse baseResponse) {
                // ignore
            }
        };
    }

}
