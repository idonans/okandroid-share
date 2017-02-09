package com.okandroid.share.util;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.okandroid.boot.lang.Log;
import com.okandroid.share.ShareHelper;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by idonans on 2017/2/4.
 */

public class AuthUtil {

    private AuthUtil() {
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
        req.state = UUID.randomUUID().toString();
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

        void onWeiboAuthSuccess(@NonNull WeiboAuthInfo info);

        void onWeiboAuthFail();

        void onWeiboAuthCancel();
    }

    public static class QQAuthInfo {
        public String ret;
        public String pay_token;
        public String pf;
        public String query_authority_cost;
        public String authority_cost;
        public String expires_in;
        public String openid;
        public String pfkey;
        public String msg;
        public String login_cost;
        public String access_token;
    }

    public static class WeiboAuthInfo {
        public String access_token;
        public String refresh_token;
        public String expires_in;
        public String uid;
        public String remind_in;
    }

    public static ShareHelper.IShareListener newAuthListener(final AuthListener authListener) {
        final String TAG = "AuthUtil#newAuthListener";

        return new ShareHelper.IShareListener() {
            @Override
            public void onQQComplete(Object o) {
                if (o instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) o;
                    QQAuthInfo info = new QQAuthInfo();
                    info.ret = jsonObject.optString("ret");
                    info.pay_token = jsonObject.optString("pay_token");
                    info.pf = jsonObject.optString("pf");
                    info.query_authority_cost = jsonObject.optString("query_authority_cost");
                    info.authority_cost = jsonObject.optString("authority_cost");
                    info.expires_in = jsonObject.optString("expires_in");
                    info.openid = jsonObject.optString("openid");
                    info.pfkey = jsonObject.optString("pfkey");
                    info.msg = jsonObject.optString("msg");
                    info.login_cost = jsonObject.optString("login_cost");
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
                if (bundle != null) {
                    WeiboAuthInfo info = new WeiboAuthInfo();
                    info.access_token = String.valueOf(bundle.get("access_token"));
                    info.refresh_token = String.valueOf(bundle.get("refresh_token"));
                    info.expires_in = String.valueOf(bundle.get("expires_in"));
                    info.uid = String.valueOf(bundle.get("uid"));
                    info.remind_in = String.valueOf(bundle.get("remind_in"));
                    authListener.onWeiboAuthSuccess(info);
                } else {
                    Log.d(TAG + " onWeiboAuthComplete but bundle is null");
                }
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
                Log.d(TAG + " onWeiboShareCallback always ignore here");
            }
        };
    }

}
