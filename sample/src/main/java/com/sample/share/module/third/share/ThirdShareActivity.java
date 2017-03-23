package com.sample.share.module.third.share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.okandroid.boot.lang.Log;
import com.okandroid.boot.thread.Threads;
import com.okandroid.boot.util.AvailableUtil;
import com.okandroid.boot.util.IOUtil;
import com.okandroid.boot.util.ImageUtil;
import com.okandroid.boot.util.ViewUtil;
import com.okandroid.share.ShareHelper;
import com.okandroid.share.util.ShareUtil;
import com.sample.share.R;
import com.sample.share.app.BaseActivity;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by idonans on 2017/2/4.
 */

public class ThirdShareActivity extends BaseActivity {

    public static Intent startIntent(Context context) {
        Intent starter = new Intent(context, ThirdShareActivity.class);
        return starter;
    }

    private static final String TAG = "ThirdShareActivity";
    private ShareHelper mShareHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShareHelper = new ShareHelper(this, mShareListener);

        setContentView(R.layout.sample_activity_third_share);

        View shareWithQQ = ViewUtil.findViewByID(this, R.id.qq);
        shareWithQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWithQQ();
            }
        });

        View shareWithQzone = ViewUtil.findViewByID(this, R.id.qzone);
        shareWithQzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWithQzone();
            }
        });

        View shareWithWeixin = ViewUtil.findViewByID(this, R.id.weixin);
        shareWithWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWithWeixin();
            }
        });

        final View shareWithWeixinTimeline = ViewUtil.findViewByID(this, R.id.weixin_timeline);
        shareWithWeixinTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWithWeixinTimeline();
            }
        });

        View shareWithWeibo = ViewUtil.findViewByID(this, R.id.weibo);
        shareWithWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWithWeibo();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mShareHelper != null) {
            mShareHelper.resume();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mShareHelper != null) {
            mShareHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        IOUtil.closeQuietly(mShareHelper);
    }

    private boolean shareWithQQ() {
        if (!isAppCompatResumed()) {
            return false;
        }

        ShareUtil.QQShareContent shareContent = new ShareUtil.QQShareContent();
        shareContent.title = "qq share title";
        shareContent.content = "qq share content";
        shareContent.image = "https://avatars3.githubusercontent.com/u/4043830?v=3&s=460";
        shareContent.targetUrl = "https://github.com/idonans/okandroid-share";
        return ShareUtil.shareToQQ(mShareHelper, shareContent);
    }

    private boolean shareWithQzone() {
        if (!isAppCompatResumed()) {
            return false;
        }

        ShareUtil.QzoneShareContent shareContent = new ShareUtil.QzoneShareContent();
        shareContent.title = "qzone share title";
        shareContent.content = "qzone share content";
        shareContent.image = "https://avatars3.githubusercontent.com/u/4043830?v=3&s=460";
        shareContent.targetUrl = "https://github.com/idonans/okandroid-share";
        return ShareUtil.shareToQzone(mShareHelper, shareContent);
    }

    private boolean shareWithWeixin() {
        if (!isAppCompatResumed()) {
            return false;
        }

        // 此处只是一个示例，实际生产中需要处理内存泄露(网络请求过程中携带了当前 Activity 对象)
        // 图片大小不能超过 32k
        final String imageUrl = "https://avatars3.githubusercontent.com/u/4043830?v=3&s=300";
        ImageUtil.cacheImageWithFresco(imageUrl, new ImageUtil.ImageFileFetchListener() {
            @Override
            public void onFileFetched(@Nullable File file) {
                if (file != null && file.exists() && file.length() > 0) {
                    final String localImagePath = file.getAbsolutePath();
                    Threads.runOnUi(new Runnable() {
                        @Override
                        public void run() {
                            shareWithWeixin(localImagePath);
                        }
                    });
                } else {
                    Log.d(TAG + " shareWithWeixin fail to load network image to local");
                }
            }
        });

        return true;
    }

    private boolean shareWithWeixin(String localImagePath) {
        if (!isAppCompatResumed()) {
            return false;
        }

        ShareUtil.WeixinShareContent shareContent = new ShareUtil.WeixinShareContent();
        shareContent.title = "weixin share title";
        shareContent.content = "weixin share content";
        shareContent.targetUrl = "https://github.com/idonans/okandroid-share";
        shareContent.image = readAll(localImagePath);
        return ShareUtil.shareToWeixin(mShareHelper, shareContent);
    }

    private boolean shareWithWeixinTimeline() {
        if (!isAppCompatResumed()) {
            return false;
        }

        // 此处只是一个示例，实际生产中需要处理内存泄露(网络请求过程中携带了当前 Activity 对象)
        // 图片大小不能超过 32k
        final String imageUrl = "https://avatars3.githubusercontent.com/u/4043830?v=3&s=300";
        ImageUtil.cacheImageWithFresco(imageUrl, new ImageUtil.ImageFileFetchListener() {
            @Override
            public void onFileFetched(@Nullable File file) {
                if (file != null && file.exists() && file.length() > 0) {
                    final String localImagePath = file.getAbsolutePath();
                    Threads.runOnUi(new Runnable() {
                        @Override
                        public void run() {
                            shareWithWeixinTimeline(localImagePath);
                        }
                    });
                } else {
                    Log.d(TAG + " shareWithWeixin fail to load network image to local");
                }
            }
        });

        return true;
    }

    private boolean shareWithWeixinTimeline(String localImagePath) {
        if (!isAppCompatResumed()) {
            return false;
        }

        ShareUtil.WeixinShareContent shareContent = new ShareUtil.WeixinShareContent();
        shareContent.title = "weixin share title";
        shareContent.content = "weixin share content";
        shareContent.targetUrl = "https://github.com/idonans/okandroid-share";
        shareContent.image = readAll(localImagePath);
        return ShareUtil.shareToWeixinTimeline(mShareHelper, shareContent);
    }

    private static byte[] readAll(String localPath) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(localPath);
            return IOUtil.readAll(fis, AvailableUtil.always(), null);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(fis);
        }
        return null;
    }

    private boolean shareWithWeibo() {
        if (!isAppCompatResumed()) {
            return false;
        }

        // 此处只是一个示例，实际生产中需要处理内存泄露(网络请求过程中携带了当前 Activity 对象)
        final String imageUrl = "https://avatars3.githubusercontent.com/u/4043830?v=3&s=460";
        ImageUtil.cacheImageWithFresco(imageUrl, new ImageUtil.ImageFileFetchListener() {
            @Override
            public void onFileFetched(@Nullable File file) {
                if (file != null && file.exists() && file.length() > 0) {
                    final String localImagePath = file.getAbsolutePath();
                    Threads.runOnUi(new Runnable() {
                        @Override
                        public void run() {
                            shareWithWeibo(localImagePath);
                        }
                    });
                } else {
                    Log.d(TAG + " shareWithWeibo fail to load network image to local");
                }
            }
        });

        return true;
    }

    private boolean shareWithWeibo(String localImagePath) {
        if (!isAppCompatResumed()) {
            return false;
        }

        ShareUtil.WeiboShareContent shareContent = new ShareUtil.WeiboShareContent();
        shareContent.content = "weibo share content https://github.com/idonans/okandroid-share";
        shareContent.image = localImagePath;
        return ShareUtil.shareToWeibo(mShareHelper, shareContent);
    }

    private ShareHelper.IShareListener mShareListener = ShareUtil.newShareListener(new ShareUtil.ShareListener() {

        private static final String TAG = "ThirdShareActivity#mShareListener";

        @Override
        public void onQQShareSuccess() {
            Log.d(TAG + " onQQShareSuccess");
        }

        @Override
        public void onQQShareFail() {
            Log.d(TAG + " onQQShareFail");
        }

        @Override
        public void onQQShareCancel() {
            Log.d(TAG + " onQQShareCancel");
        }

        @Override
        public void onWeixinShareSuccess() {
            Log.d(TAG + " onWeixinShareSuccess");
        }

        @Override
        public void onWeixinShareFail() {
            Log.d(TAG + " onWeixinShareFail");
        }

        @Override
        public void onWeixinShareCancel() {
            Log.d(TAG + " onWeixinShareCancel");
        }

        @Override
        public void onWeiboShareSuccess() {
            Log.d(TAG + " onWeiboShareSuccess");
        }

        @Override
        public void onWeiboShareFail() {
            Log.d(TAG + " onWeiboShareFail");
        }

        @Override
        public void onWeiboShareCancel() {
            Log.d(TAG + " onWeiboShareCancel");
        }
    });

}
