package com.qctx.www.lkl.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qctx.www.lkl.R;

public class SplashActivity extends AppCompatActivity {
    private Context mContext;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_splash);
        mContext = this;
        imageView = (ImageView) findViewById(R.id.iv_show);
        Glide.with(mContext).load("http://120.27.19.71:8080/erp/images/app/qdy.jpg").into(imageView);
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
               Intent intent = setComponent();
//                startActivity(intent);
                startActivity(new Intent(mContext,MainActivity.class));
                finish();
            }
        }.start();
    }
    public Intent setComponent(){
        ComponentName component = new ComponentName(
                "com.lkl.cloudpos.payment",
                "com.lkl.cloudpos.payment.activity.MainMenuActivity");
        Intent intent = new Intent();
        intent.setComponent(component);
        return intent;
    }
}
