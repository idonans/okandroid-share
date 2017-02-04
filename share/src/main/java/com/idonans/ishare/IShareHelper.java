package com.idonans.ishare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.idonans.ishare.qq.IShareQQHelper;
import com.idonans.ishare.weibo.IShareWeiboHelper;
import com.idonans.ishare.weixin.IShareWeixinHelper;
import com.okandroid.boot.util.IOUtil;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by idonans on 2017/2/4.
 */
public class IShareHelper implements Closeable {

    private Activity mActivity;
    private IShareQQHelper mIShareQQHelper;
    private IShareWeixinHelper mIShareWeixinHelper;
    private IShareWeiboHelper mIShareWeiboHelper;

    public IShareHelper(@NonNull Activity activity, @NonNull IShareListener listener) {
        mActivity = activity;

        if (hasConfigQQ()) {
            mIShareQQHelper = new IShareQQHelper(new IShareQQUiListenerAdapter(listener));
        }

        if (hasConfigWeixin()) {
            mIShareWeixinHelper = new IShareWeixinHelper(new IShareWeixinListenerAdapter(listener));
        }

        if (hasConfigWeibo()) {
            mIShareWeiboHelper = new IShareWeiboHelper(activity,
                    new IShareWeiboAuthListenerAdapter(listener),
                    new IShareWeiboShareListenerAdapter(listener));
        }
    }

    public Activity getActivity() {
        return mActivity;
    }

    public IShareQQHelper getIShareQQHelper() {
        return mIShareQQHelper;
    }

    public IShareWeixinHelper getIShareWeixinHelper() {
        return mIShareWeixinHelper;
    }

    public IShareWeiboHelper getIShareWeiboHelper() {
        return mIShareWeiboHelper;
    }

    public void resume() {
        if (mIShareWeixinHelper != null) {
            mIShareWeixinHelper.resume();
        }

        if (mIShareWeiboHelper != null) {
            mIShareWeiboHelper.resume();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mIShareQQHelper != null) {
            if (mIShareQQHelper.onActivityResult(requestCode, resultCode, data)) {
                return;
            }
        }

        if (mIShareWeiboHelper != null) {
            mIShareWeiboHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void close() throws IOException {
        mActivity = null;
        IOUtil.closeQuietly(mIShareQQHelper);
        IOUtil.closeQuietly(mIShareWeixinHelper);
        IOUtil.closeQuietly(mIShareWeiboHelper);
    }

    private static class IShareQQUiListenerAdapter implements IUiListener {

        @NonNull
        private final IShareListener mOutListener;

        private IShareQQUiListenerAdapter(@NonNull IShareListener outListener) {
            mOutListener = outListener;
        }

        @Override
        public void onComplete(Object o) {
            mOutListener.onQQComplete(o);
        }

        @Override
        public void onError(UiError uiError) {
            mOutListener.onQQError(uiError);
        }

        @Override
        public void onCancel() {
            mOutListener.onQQCancel();
        }
    }

    private static class IShareWeixinListenerAdapter implements IShareWeixinHelper.IWXListener {

        @NonNull
        private final IShareListener mOutListener;

        private IShareWeixinListenerAdapter(@NonNull IShareListener outListener) {
            mOutListener = outListener;
        }

        @Override
        public void onWXCallback(BaseResp baseResp) {
            mOutListener.onWeixinCallback(baseResp);
        }
    }

    private static class IShareWeiboAuthListenerAdapter implements WeiboAuthListener {

        @NonNull
        private final IShareListener mOutListener;

        private IShareWeiboAuthListenerAdapter(@NonNull IShareListener outListener) {
            mOutListener = outListener;
        }

        @Override
        public void onComplete(Bundle bundle) {
            mOutListener.onWeiboAuthComplete(bundle);
        }

        @Override
        public void onWeiboException(WeiboException e) {
            mOutListener.onWeiboAuthException(e);
        }

        @Override
        public void onCancel() {
            mOutListener.onWeiboAuthCancel();
        }
    }

    private static class IShareWeiboShareListenerAdapter implements IShareWeiboHelper.WeiboShareListener {

        @NonNull
        private final IShareListener mOutListener;

        private IShareWeiboShareListenerAdapter(@NonNull IShareListener outListener) {
            mOutListener = outListener;
        }

        @Override
        public void onWeiboShareCallback(BaseResponse baseResponse) {
            mOutListener.onWeiboShareCallback(baseResponse);
        }
    }

    public interface IShareListener {

        void onQQComplete(Object o);

        void onQQError(UiError uiError);

        void onQQCancel();

        void onWeixinCallback(BaseResp baseResp);

        void onWeiboAuthComplete(Bundle bundle);

        void onWeiboAuthException(WeiboException e);

        void onWeiboAuthCancel();

        void onWeiboShareCallback(BaseResponse baseResponse);
    }

    private static boolean hasConfigQQ() {
        return !TextUtils.isEmpty(IShareConfig.getQQAppId());
    }

    private static boolean hasConfigWeixin() {
        return !TextUtils.isEmpty(IShareConfig.getWeixinAppKey());
    }

    private static boolean hasConfigWeibo() {
        return !TextUtils.isEmpty(IShareConfig.getWeiboAppKey());
    }

    public static class SimpleIShareListener implements IShareListener {

        @Override
        public void onQQComplete(Object o) {
            // ignore
        }

        @Override
        public void onQQError(UiError uiError) {
            // ignore
        }

        @Override
        public void onQQCancel() {
            // ignore
        }

        @Override
        public void onWeixinCallback(BaseResp baseResp) {
            // ignore
        }

        @Override
        public void onWeiboAuthComplete(Bundle bundle) {
            // ignore
        }

        @Override
        public void onWeiboAuthException(WeiboException e) {
            // ignore
        }

        @Override
        public void onWeiboAuthCancel() {
            // ignore
        }

        @Override
        public void onWeiboShareCallback(BaseResponse baseResponse) {
            // ignore
        }
    }

}
