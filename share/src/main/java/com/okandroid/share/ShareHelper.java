package com.okandroid.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.okandroid.boot.util.IOUtil;
import com.okandroid.share.qq.ShareQQHelper;
import com.okandroid.share.weibo.ShareWeiboHelper;
import com.okandroid.share.weixin.ShareWeixinHelper;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import java.io.Closeable;
import java.io.IOException;

/**
 * 使用 {@link ShareConfig} 配置必要的参数, 未配置参数的平台将不能使用.
 * <p>
 * <pre>
 * 使用时，还需要添加必要的配置
 * 在 Manifest 中正确的 package 下新建并添加声明
 *
 * 微信平台{@code
 *
 * <activity android:name="***.wxapi.WXEntryActivity" // extends ShareWXEntryActivity
 *     android:exported="true"
 *     android:noHistory="true">
 *     <intent-filter>
 *         <action android:name="android.intent.action.VIEW"/>
 *         <category android:name="android.intent.category.DEFAULT"/>
 *         <data android:scheme="***"/>
 *     </intent-filter>
 * </activity>}
 *
 * QQ 平台{@code
 *
 * <activity android:name="com.tencent.tauth.AuthActivity"
 *     android:exported="true"
 *     android:noHistory="true">
 *     <intent-filter>
 *         <action android:name="android.intent.action.VIEW"/>
 *         <category android:name="android.intent.category.DEFAULT"/>
 *         <category android:name="android.intent.category.BROWSABLE"/>
 *         <data android:scheme="tencent***"/>
 *     </intent-filter>
 * </activity>}
 * </pre>
 * Created by idonans on 2017/2/4.
 */
public class ShareHelper implements Closeable {

    private Activity mActivity;
    private ShareQQHelper mShareQQHelper;
    private ShareWeixinHelper mShareWeixinHelper;
    private ShareWeiboHelper mShareWeiboHelper;

    public ShareHelper(@NonNull Activity activity, @NonNull IShareListener listener) {
        mActivity = activity;

        if (ShareConfig.hasConfigQQ()) {
            mShareQQHelper = new ShareQQHelper(new IShareQQUiListenerAdapter(listener));
        }

        if (ShareConfig.hasConfigWeixin()) {
            mShareWeixinHelper = new ShareWeixinHelper(new IShareWeixinListenerAdapter(listener));
        }

        if (ShareConfig.hasConfigWeibo()) {
            mShareWeiboHelper = new ShareWeiboHelper(activity,
                    new IShareWeiboAuthListenerAdapter(listener),
                    new IShareWeiboShareListenerAdapter(listener));
        }
    }

    public Activity getActivity() {
        return mActivity;
    }

    public ShareQQHelper getShareQQHelper() {
        return mShareQQHelper;
    }

    public ShareWeixinHelper getShareWeixinHelper() {
        return mShareWeixinHelper;
    }

    public ShareWeiboHelper getShareWeiboHelper() {
        return mShareWeiboHelper;
    }

    public void resume() {
        if (mShareWeixinHelper != null) {
            mShareWeixinHelper.resume();
        }

        if (mShareWeiboHelper != null) {
            mShareWeiboHelper.resume();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mShareQQHelper != null) {
            if (mShareQQHelper.onActivityResult(requestCode, resultCode, data)) {
                return;
            }
        }

        if (mShareWeiboHelper != null) {
            mShareWeiboHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void close() throws IOException {
        mActivity = null;
        IOUtil.closeQuietly(mShareQQHelper);
        IOUtil.closeQuietly(mShareWeixinHelper);
        IOUtil.closeQuietly(mShareWeiboHelper);
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

    private static class IShareWeixinListenerAdapter implements ShareWeixinHelper.IWXListener {

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

    private static class IShareWeiboShareListenerAdapter implements ShareWeiboHelper.WeiboShareListener {

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
