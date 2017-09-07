package com.qctx.www.lkl.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.qctx.www.lkl.R;
import com.qctx.www.lkl.utils.Constant;
import com.qctx.www.lkl.utils.SharedPreferencesUtil;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import static com.qctx.www.lkl.R.id.usernameWrapper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText uerName, Password;
    private TextInputLayout userNameWrapper, passwordWrapper;
    private Button btn;
    private Context mContext;
    private String userName;
    private String password;
    private ProgressDialog dialog;
    private CheckBox ck_rem;
    private boolean checked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("登录");
        initView();

    }

    private void initView() {
        mContext = this;
        userNameWrapper = (TextInputLayout) findViewById(usernameWrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        ck_rem = (CheckBox) findViewById(R.id.ck_rem);
        userNameWrapper.setHint("用户名");
        passwordWrapper.setHint("密码");

        uerName = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.password);
        hideError(uerName, userNameWrapper);
        hideError(Password, passwordWrapper);

        ck_rem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checked = b;
            }
        });
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                dialog = new ProgressDialog(mContext);
                dialog.setMessage("登录中。。。");
                dialog.show();
                userName = userNameWrapper.getEditText().getText().toString();
                password = passwordWrapper.getEditText().getText().toString();
                if (!isEmailValid(userName)) {
                    userNameWrapper.setError("用户名不可用");
                } else if (!isPasswordValid(password)) {
                    passwordWrapper.setError("密码不可用");
                } else {
                    userNameWrapper.setErrorEnabled(false);
                    passwordWrapper.setErrorEnabled(false);
                    QueryTask queryTask = new QueryTask();
                    //启动后台任务
                    queryTask.execute();
                    hideKeyboard();
                }
                break;
            default:
                break;
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return true;
    }

    private void hideError(EditText ed, final TextInputLayout ti) {
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ti.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private String LoginCheck() {
        String s = "";
        SoapObject request = new SoapObject(Constant.LOGIN_PARAM_NAME_SPACE, Constant.CHECK_USER_METHOD);
        // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
        request.addProperty("name", userName);
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
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this, ScanerActivity.class);
                startActivity(intent);
                if (checked){
                    SharedPreferencesUtil.setParam(mContext,"user",userName);
                    SharedPreferencesUtil.setParam(mContext,"password",password);
                    SharedPreferencesUtil.setParam(mContext,"isLogin",true);
                }else {
                    SharedPreferencesUtil.setParam(mContext,"user","");
                    SharedPreferencesUtil.setParam(mContext,"password","");
                    SharedPreferencesUtil.setParam(mContext,"isLogin",false);
                }

                finish();
            }else {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"用户名或密码错误",Toast.LENGTH_LONG).show();
            }
        }
    }

}
