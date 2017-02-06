package com.okandroid.share.weixin;

import android.support.annotation.CheckResult;

import com.okandroid.boot.AppContext;
import com.okandroid.boot.lang.Log;
import com.okandroid.share.ShareConfig;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * 微信登陆分享
 * Created by idonans on 2017/2/4.
 */
public final class ShareWeixinHelper implements Closeable {

    private IWXAPI mApi;
    private IWXListenerAdapter mListener;

    private static final GlobalWXAPIEventHandler sGlobalWXAPIEventHandler = new GlobalWXAPIEventHandler();

    public ShareWeixinHelper(IWXListener listener) {
        mApi = WXAPIFactory.createWXAPI(AppContext.getContext(), ShareConfig.getWeixinAppKey(), false);
        mApi.registerApp(ShareConfig.getWeixinAppKey());
        mListener = new IWXListenerAdapter();
        mListener.setOutListener(listener);
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
