package com.idonans.ishare.weixin;

import android.support.annotation.CheckResult;

import com.idonans.ishare.IShareConfig;
import com.okandroid.boot.AppContext;
import com.okandroid.boot.lang.Log;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.UUID;

/**
 * 微信登陆分享
 * Created by idonans on 2017/2/4.
 */
public final class IShareWeixinHelper implements Closeable {

    private IWXAPI mApi;
    private IWXListenerAdapter mListener;
    private final String mState;

    private static final GlobalWXAPIEventHandler sGlobalWXAPIEventHandler = new GlobalWXAPIEventHandler();

    public IShareWeixinHelper(IWXListener listener) {
        mApi = WXAPIFactory.createWXAPI(AppContext.getContext(), IShareConfig.getWeixinAppKey(), false);
        mApi.registerApp(IShareConfig.getWeixinAppKey());
        mListener = new IWXListenerAdapter();
        mListener.setOutListener(listener);
        mState = UUID.randomUUID().toString();
    }

    public void resume() {
        sGlobalWXAPIEventHandler.setListenerProxy(mListener);
    }

    /**
     * 如果没有安装微信客户端，或者微信客户端版本不支持，将返回 null.
     *
     * @return
     */
    @CheckResult
    public IWXAPI getApi() {
        if (mApi.isWXAppSupportAPI()) {
            return mApi;
        } else {
            return null;
        }
    }

    public String getState() {
        return mState;
    }

    @Override
    public void close() throws IOException {
        mListener.setOutListener(null);
    }

    public interface IWXListener {
        void onWXCallback(BaseResp baseResp);
    }

    private static class IWXListenerAdapter implements IWXListener {

        private IWXListener mOutListener;

        public void setOutListener(IWXListener outListener) {
            mOutListener = outListener;
        }

        @Override
        public void onWXCallback(BaseResp baseResp) {
            if (mOutListener != null) {
                mOutListener.onWXCallback(baseResp);
            }
        }
    }

    public static IWXAPIEventHandler getGlobalWXAPIEventHandler() {
        return sGlobalWXAPIEventHandler;
    }

    private static final class GlobalWXAPIEventHandler implements IWXAPIEventHandler {

        private static final String TAG = "IShareWeixinHelper$GlobalWXAPIEventHandler";
        private IWXListener mListenerProxy;
        private BaseResp mPendingBaseResp;

        public void setListenerProxy(IWXListener listenerProxy) {
            mListenerProxy = listenerProxy;
            if (mPendingBaseResp != null) {
                mListenerProxy.onWXCallback(mPendingBaseResp);
                mPendingBaseResp = null;
            }
        }

        @Override
        public void onReq(BaseReq baseReq) {
            Log.d(TAG + " onReq " + baseReq);
        }

        @Override
        public void onResp(BaseResp baseResp) {
            Log.d(TAG + " onResp " + baseResp);
            if (mListenerProxy != null) {
                mPendingBaseResp = null;
                mListenerProxy.onWXCallback(baseResp);
            } else {
                mPendingBaseResp = baseResp;
            }
        }
    }

}
