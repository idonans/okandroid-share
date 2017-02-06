package com.okandroid.share.util;

import android.os.Bundle;
import android.text.TextUtils;

import com.okandroid.boot.lang.Log;
import com.okandroid.share.ShareHelper;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * Created by idonans on 2017/2/6.
 */

public class ShareUtil {

    private ShareUtil() {
    }

    public static class QQShareContent {

        public String title;
        public String content;
        /**
         * 点击链接
         */
        public String targetUrl;
        /**
         * 分享的图片，本地或者网络地址(本地图片有尺寸限制)
         */
        public String image;
    }

    private static Bundle convertQQShareContent(QQShareContent shareContent) {
        Bundle bundle = new Bundle();
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        if (!TextUtils.isEmpty(shareContent.title)) {
            bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareContent.title);
        }
        if (!TextUtils.isEmpty(shareContent.content)) {
            bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareContent.content);
        }
        if (!TextUtils.isEmpty(shareContent.targetUrl)) {
            bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareContent.targetUrl);
        }
        if (!TextUtils.isEmpty(shareContent.image)) {
            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareContent.image);
        }
        return bundle;
    }

    public static boolean shareToQQ(ShareHelper shareHelper, QQShareContent shareContent) {
        if (shareHelper == null) {
            return false;
        }

        if (shareContent == null) {
            return false;
        }

        Tencent tencent = shareHelper.getShareQQHelper().getTencent(shareHelper.getActivity());
        if (tencent == null) {
            return false;
        }

        tencent.shareToQQ(shareHelper.getActivity(), convertQQShareContent(shareContent), shareHelper.getShareQQHelper().getListener());
        return true;
    }

    public static class QzoneShareContent {

        public String title;
        public String content;
        /**
         * 点击链接
         */
        public String targetUrl;
        /**
         * 分享的图片，本地或者网络地址(本地图片有尺寸限制)
         */
        public String image;
    }

    private static Bundle convertQzoneShareContent(QzoneShareContent shareContent) {
        Bundle bundle = new Bundle();
        bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        if (!TextUtils.isEmpty(shareContent.title)) {
            bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareContent.title);
        }
        if (!TextUtils.isEmpty(shareContent.content)) {
            bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareContent.content);
        }
        if (!TextUtils.isEmpty(shareContent.targetUrl)) {
            bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareContent.targetUrl);
        }
        if (!TextUtils.isEmpty(shareContent.image)) {
            ArrayList<String> images = new ArrayList<>();
            images.add(shareContent.image);
            bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);
        }
        return bundle;
    }

    public static boolean shareToQzone(ShareHelper shareHelper, QzoneShareContent shareContent) {
        if (shareHelper == null) {
            return false;
        }

        if (shareContent == null) {
            return false;
        }

        Tencent tencent = shareHelper.getShareQQHelper().getTencent(shareHelper.getActivity());
        if (tencent == null) {
            return false;
        }

        tencent.shareToQzone(shareHelper.getActivity(), convertQzoneShareContent(shareContent), shareHelper.getShareQQHelper().getListener());
        return true;
    }

    public static class WeiboShareContent {

        public String title;
        public String content;
        /**
         * 点击链接
         */
        public String targetUrl;
        /**
         * jpeg 格式, 不能超过 32k
         */
        public byte[] image;
    }

    private static WebpageObject convertWeiboShareContent(WeiboShareContent shareContent) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        if (!TextUtils.isEmpty(shareContent.title)) {
            mediaObject.title = shareContent.title;
        }
        if (!TextUtils.isEmpty(shareContent.content)) {
            mediaObject.description = shareContent.content;
        }
        if (!TextUtils.isEmpty(shareContent.targetUrl)) {
            mediaObject.actionUrl = shareContent.targetUrl;
        }
        if (shareContent.image != null && shareContent.image.length > 0) {
            mediaObject.thumbData = shareContent.image;
        }
        return mediaObject;
    }

    public static boolean shareToWeibo(ShareHelper shareHelper, WeiboShareContent shareContent) {
        if (shareHelper == null) {
            return false;
        }

        if (shareContent == null) {
            return false;
        }

        IWeiboShareAPI api = shareHelper.getShareWeiboHelper().getIWeiboShareAPI(true);
        if (api == null) {
            return false;
        }

        WeiboMessage weiboMessage = new WeiboMessage();
        weiboMessage.mediaObject = convertWeiboShareContent(shareContent);
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        request.transaction = Utility.generateGUID();
        request.message = weiboMessage;
        return api.sendRequest(shareHelper.getActivity(), request);
    }

    public interface ShareListener {
        void onQQShareSuccess();

        void onQQShareFail();

        void onQQShareCancel();

        void onWeixinShareSuccess();

        void onWeixinShareFail();

        void onWeixinShareCancel();

        void onWeiboShareSuccess();

        void onWeiboShareFail();

        void onWeiboShareCancel();
    }

    public static ShareHelper.IShareListener newShareListener(final ShareListener shareListener) {
        final String TAG = "ShareUtil#newShareListener";

        return new ShareHelper.IShareListener() {
            @Override
            public void onQQComplete(Object o) {
                shareListener.onQQShareSuccess();
            }

            @Override
            public void onQQError(UiError uiError) {
                if (uiError == null) {
                    Log.d(TAG + " onQQError uiError is null");
                } else {
                    Log.d(TAG + " onQQError " + uiError.errorCode + " " + uiError.errorMessage + " " + uiError.errorDetail);
                }
                shareListener.onQQShareFail();
            }

            @Override
            public void onQQCancel() {
                shareListener.onQQShareCancel();
            }

            @Override
            public void onWeixinCallback(BaseResp baseResp) {
                // TODO
            }

            @Override
            public void onWeiboAuthComplete(Bundle bundle) {
                Log.d(TAG + " onWeiboAuthComplete always ignore here");
            }

            @Override
            public void onWeiboAuthException(WeiboException e) {
                Log.d(TAG + " onWeiboAuthException always ignore here");
            }

            @Override
            public void onWeiboAuthCancel() {
                Log.d(TAG + " onWeiboAuthCancel always ignore here");
            }

            @Override
            public void onWeiboShareCallback(BaseResponse baseResponse) {
                if (baseResponse == null) {
                    Log.d(TAG + " onWeiboShareCallback but baseResponse is null");
                    return;
                }

                switch (baseResponse.errCode) {
                    case WBConstants.ErrorCode.ERR_OK:
                        shareListener.onWeiboShareSuccess();
                        break;
                    case WBConstants.ErrorCode.ERR_FAIL:
                        shareListener.onWeiboShareFail();
                        break;
                    case WBConstants.ErrorCode.ERR_CANCEL:
                        shareListener.onWeiboShareCancel();
                        break;
                    default:
                        Log.d(TAG + " onWeiboShareCallback but baseResponse errCode invalid " + baseResponse.errCode);
                        break;
                }
            }
        };
    }

}
