package com.idonans.ishare.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.idonans.ishare.IShareConfig;
import com.okandroid.boot.lang.Log;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 微博登陆分享
 * Created by idonans on 2017/2/4.
 */
public final class IShareWeiboHelper implements Closeable {

    private final Activity mActivity;
    private final WeiboAuthListenerAdapter mListener;
    private final AuthInfo mAuthInfo;
    private final SsoHandler mSsoHandler;

    private static final GlobalWeiboHandlerResponseAdapter sGlobalWeiboHandlerResponseAdapter = new GlobalWeiboHandlerResponseAdapter();
    private final IWeiboShareAPI mIWeiboShareAPI;
    private final WeiboShareListenerAdapter mShareListenerAdapter;

    public IShareWeiboHelper(Activity activity, WeiboAuthListener listener, WeiboShareListener shareListener) {
        mActivity = activity;
        mListener = new WeiboAuthListenerAdapter();
        mListener.setOutListener(listener);

        mAuthInfo = new AuthInfo(activity, IShareConfig.getWeiboAppKey(), IShareConfig.getWeiboRedirectUrl(), null);
        mSsoHandler = new SsoHandler(activity, mAuthInfo);

        // fix sdk bug: sso 客户端授权时，如果当前 app 被回收，需要恢复参数. 注意需要避免这些参数被混淆
        try {
            {
                Field field = SsoHandler.class.getDeclaredField("mSSOAuthRequestCode");
                field.setAccessible(true);
                field.setInt(mSsoHandler, 32973);
            }
            {
                Field field = SsoHandler.class.getDeclaredField("mAuthListener");
                field.setAccessible(true);
                field.set(mSsoHandler, getListener());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        mShareListenerAdapter = new WeiboShareListenerAdapter();
        mShareListenerAdapter.setOutListener(shareListener);
        mIWeiboShareAPI = WeiboShareSDK.createWeiboAPI(activity, IShareConfig.getWeiboAppKey(), false);
        mIWeiboShareAPI.registerApp();
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void resume() {
        sGlobalWeiboHandlerResponseAdapter.setListenerProxy(mShareListenerAdapter);
    }

    public SsoHandler getSsoHandler() {
        return mSsoHandler;
    }

    @NonNull
    public WeiboAuthListenerAdapter getListener() {
        return mListener;
    }

    /**
     * 指定是否校验微博客户端，如果需要校验而没有安装微博客户端，或者微博客户端版本不支持，将返回 null.
     */
    @CheckResult
    public IWeiboShareAPI getIWeiboShareAPI(boolean needWeiboApp) {
        if (needWeiboApp) {
            if (mIWeiboShareAPI.isWeiboAppSupportAPI()) {
                return mIWeiboShareAPI;
            } else {
                return null;
            }
        } else {
            return mIWeiboShareAPI;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
    }

    @Override
    public void close() throws IOException {
        mListener.setOutListener(null);
        mShareListenerAdapter.setOutListener(null);
    }

    public interface WeiboShareListener {
        void onWeiboShareCallback(BaseResponse baseResponse);
    }

    private static class WeiboAuthListenerAdapter implements WeiboAuthListener {

        private WeiboAuthListener mOutListener;

        public void setOutListener(WeiboAuthListener outListener) {
            mOutListener = outListener;
        }

        @Override
        public void onComplete(Bundle bundle) {
            if (mOutListener != null) {
                mOutListener.onComplete(bundle);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            if (mOutListener != null) {
                mOutListener.onWeiboException(e);
            }
        }

        @Override
        public void onCancel() {
            if (mOutListener != null) {
                mOutListener.onCancel();
            }
        }

    }

    private static class WeiboShareListenerAdapter implements WeiboShareListener {

        private WeiboShareListener mOutListener;

        public void setOutListener(WeiboShareListener outListener) {
            mOutListener = outListener;
        }

        @Override
        public void onWeiboShareCallback(BaseResponse baseResponse) {
            if (mOutListener != null) {
                mOutListener.onWeiboShareCallback(baseResponse);
            }
        }
    }

    public static GlobalWeiboHandlerResponseAdapter getGlobalWeiboHandlerResponseAdapter() {
        return sGlobalWeiboHandlerResponseAdapter;
    }

    private static class GlobalWeiboHandlerResponseAdapter implements IWeiboHandler.Response {

        private static final String TAG = "IShareWeiboHelper$GlobalWeiboHandlerResponseAdapter";
        private WeiboShareListener mListenerProxy;
        private BaseResponse mPendingBaseResponse;

        public void setListenerProxy(WeiboShareListener listenerProxy) {
            mListenerProxy = listenerProxy;
            if (mPendingBaseResponse != null) {
                mListenerProxy.onWeiboShareCallback(mPendingBaseResponse);
                mPendingBaseResponse = null;
            }
        }

        @Override
        public void onResponse(BaseResponse baseResponse) {
            Log.d(TAG + " onResponse " + baseResponse);
            if (mListenerProxy != null) {
                mPendingBaseResponse = null;
                mListenerProxy.onWeiboShareCallback(baseResponse);
            } else {
                mPendingBaseResponse = baseResponse;
            }
        }

    }

}
