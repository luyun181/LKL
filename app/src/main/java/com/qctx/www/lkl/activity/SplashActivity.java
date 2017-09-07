package com.qctx.www.lkl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qctx.www.lkl.R;
import com.qctx.www.lkl.utils.Constant;
import com.qctx.www.lkl.utils.SharedPreferencesUtil;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {
    private Context mContext;
    private ImageView imageView;
    private String user;
    private String password;
    private boolean isLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_splash);
        mContext = this;
        imageView = (ImageView) findViewById(R.id.iv_show);

        user = (String) SharedPreferencesUtil.getParam(mContext, "user", "");
        password = (String) SharedPreferencesUtil.getParam(mContext, "password", "");
        isLogin = (boolean) SharedPreferencesUtil.getParam(mContext, "isLogin", false);
        Glide.with(mContext)
                .load("http://120.27.19.71:8080/erp/images/app/qdy.jpg")
                .placeholder(R.drawable.sp)
                .into(imageView);
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
//                startActivity(intent);
                if (!isLogin) {
                    startActivity(new Intent(mContext, MainActivity.class));
                    finish();
                }else {
                    QueryTask queryTask = new QueryTask();
                    //启动后台任务
                    queryTask.execute();
                }
            }
        }.start();
    }
    private String LoginCheck() {
        String s = "";
        SoapObject request = new SoapObject(Constant.LOGIN_PARAM_NAME_SPACE, Constant.CHECK_USER_METHOD);
        // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
        request.addProperty("name", user);
        request.addProperty("pwd", password);

        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
        envelope.bodyOut = request;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true

        HttpTransportSE httpTransportSE = new HttpTransportSE(Constant.LOGIN_SERVICE_URL);
        try {
            httpTransportSE.call(null, envelope);//调用
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
//        Object bodyIn = envelope.bodyIn;
        // 获取返回的结果
//       result= bodyIn.toString();
        s = object.getProperty("return").toString();
        Log.d("debug", s);
        return s;
    }

    public class QueryTask extends AsyncTask<String, Integer, String> {
        String s;

        @Override
        protected String doInBackground(String... params) {
            try {
                s = LoginCheck();

            } catch (Exception e) {
                e.printStackTrace();
            }
            //将结果返回给onPostExecute方法
            return s;
        }

        @Override
        //此方法可以在主线程改变UI
        protected void onPostExecute(String mresult) {
            // 将WebService返回的结果显示在TextView中
            if (mresult!=null&&mresult.length() > 0) {
                Intent intent = new Intent(SplashActivity.this, ScanerActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SharedPreferencesUtil.setParam(mContext,"isLogin",false);
                finish();
                Toast.makeText(getApplicationContext(),"用户名或密码错误",Toast.LENGTH_LONG).show();
            }
        }
    }
}
