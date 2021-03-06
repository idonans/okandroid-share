package com.okandroid.share.qq;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.okandroid.boot.AppContext;
import com.okandroid.share.ShareConfig;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.Closeable;
import java.io.IOException;

/**
 * QQ 登陆分享，Qzone 分享
 * Created by idonans on 2017/2/4.
 */
public final class ShareQQHelper implements Closeable {

    private final Tencent mTencent;
    private final IUiListenerAdapter mListener;

    public ShareQQHelper(IUiListener listener) {
        mTencent = Tencent.createInstance(ShareConfig.getQQAppId(), AppContext.getContext());
        mListener = new IUiListenerAdapter();
        mListener.setOutListener(listener);
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_LOGIN:
            case Constants.REQUEST_QQ_SHARE:
            case Constants.REQUEST_QZONE_SHARE: {
                Tencent.onActivityResultData(requestCode, resultCode, data, mListener);
                return true;
            }
            default: {
                return false;
            }
        }
    }

    /**
     * 如果没有安装 QQ 客户端，或者 QQ 版本不支持，将返回 null.
     *
     * @param activity
     * @return
     */
    @CheckResult
    public Tencent getTencent(Activity activity) {
        if (mTencent.isSupportSSOLogin(activity)) {
            return mTencent;
        } else {
            return null;
        }
    }

    @NonNull
    public IUiListener getListener() {
        return mListener;
    }

    @Override
    public void close() throws IOException {
        mListener.setOutListener(null);
    }

    private static class IUiListenerAdapter implements IUiListener {

        private IUiListener mOutListener;

        public void setOutListener(IUiListener outListener) {
            mOutListener = outListener;
        }

        @Override
        public void onComplete(Object o) {
            if (mOutListener != null) {
                mOutListener.onComplete(o);
            }
        }

        @Override
        public void onError(UiError uiError) {
            if (mOutListener != null) {
                mOutListener.onError(uiError);
            }
        }

        @Override
        public void onCancel() {
            if (mOutListener != null) {
                mOutListener.onCancel();
            }
        }
    }

}
