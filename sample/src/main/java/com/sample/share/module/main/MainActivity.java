package com.sample.share.module.main;

import android.os.Bundle;
import android.view.View;

import com.okandroid.boot.util.ViewUtil;
import com.sample.share.R;
import com.sample.share.app.BaseActivity;
import com.sample.share.module.third.share.ThirdShareActivity;
import com.sample.share.module.third.signin.ThirdSignInActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_main);

        View signInView = ViewUtil.findViewByID(this, R.id.sign_in);
        signInView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directToThirdSignIn();
            }
        });

        View shareView = ViewUtil.findViewByID(this, R.id.share);
        shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directToThirdShare();
            }
        });
    }

    private boolean directToThirdSignIn() {
        if (isAppCompatResumed()) {
            startActivity(ThirdSignInActivity.startIntent(this));
            return true;
        }
        return false;
    }

    private boolean directToThirdShare() {
        if (isAppCompatResumed()) {
            startActivity(ThirdShareActivity.startIntent(this));
            return true;
        }
        return false;
    }

}
