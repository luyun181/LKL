/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qctx.www.lkl.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.Result;
import com.qctx.www.lkl.R;
import com.qctx.www.lkl.bean.ItemBean;
import com.qctx.www.lkl.bean.ShangPinInfo;
import com.qctx.www.lkl.utils.BadgeView;
import com.qctx.www.lkl.utils.Constant;
import com.qctx.www.lkl.utils.ToastUtils;
import com.qctx.www.lkl.zxing.ScanListener;
import com.qctx.www.lkl.zxing.ScanManager;
import com.qctx.www.lkl.zxing.decode.DecodeThread;
import com.qctx.www.lkl.zxing.decode.Utils;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 二维码扫描使用
 */
public final class CommonScanActivity extends Activity implements ScanListener, View.OnClickListener {
    static final String TAG = CommonScanActivity.class.getSimpleName();
    SurfaceView scanPreview = null;
    View scanContainer;
    View scanCropView;
    ImageView scanLine;
    ScanManager scanManager;
    TextView iv_light;
    TextView qrcode_g_gallery;
    TextView qrcode_ic_back;
    final int PHOTOREQUESTCODE = 1111;
    ProgressDialog dialog;

    ItemBean itemBean = null;
    Button rescan;
    BadgeView badge;
    ImageView scan_image;
    ImageView authorize_return;
    private int scanMode;//扫描模型（条形，二维码，全部）

    TextView title, tv_count;
    TextView scan_hint;
    TextView tv_scan_result;
    String rawResultText;
    int i = 0;
    ArrayList<ItemBean> proList = new ArrayList<>();
    Map<String, Integer> map = new HashMap<>();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_scan_code);
        scanMode = getIntent().getIntExtra(Constant.REQUEST_SCAN_MODE, Constant.REQUEST_SCAN_MODE_ALL_MODE);
        initView();
    }

    void initView() {
        rescan = (Button) findViewById(R.id.service_register_rescan);
        scan_image = (ImageView) findViewById(R.id.scan_image);
        authorize_return = (ImageView) findViewById(R.id.authorize_return);
        title = (TextView) findViewById(R.id.common_title_TV_center);
        scan_hint = (TextView) findViewById(R.id.scan_hint);
        tv_scan_result = (TextView) findViewById(R.id.tv_scan_result);
        switch (scanMode) {
            case DecodeThread.BARCODE_MODE:
                title.setText(R.string.scan_barcode_title);
                scan_hint.setText(R.string.scan_barcode_hint);
                break;
            case DecodeThread.QRCODE_MODE:
                title.setText(R.string.scan_qrcode_title);
                scan_hint.setText(R.string.scan_qrcode_hint);
                break;
            case DecodeThread.ALL_MODE:
                title.setText(R.string.scan_allcode_title);
                scan_hint.setText(R.string.scan_allcode_hint);
                break;
        }
        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = findViewById(R.id.capture_container);
        scanCropView = findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        qrcode_g_gallery = (TextView) findViewById(R.id.qrcode_g_gallery);
        qrcode_g_gallery.setOnClickListener(this);
        qrcode_ic_back = (TextView) findViewById(R.id.qrcode_ic_back);
        qrcode_ic_back.setOnClickListener(this);
        iv_light = (TextView) findViewById(R.id.iv_light);
        iv_light.setOnClickListener(this);
        rescan.setOnClickListener(this);
        authorize_return.setOnClickListener(this);
        //构造出扫描管理器
        scanManager = new ScanManager(this, scanPreview, scanContainer, scanCropView, scanLine, scanMode, this);
        badge = new BadgeView(this, qrcode_g_gallery);
    }

    @Override
    public void onResume() {
        super.onResume();
        scanManager.onResume();
        rescan.setVisibility(View.INVISIBLE);
        scan_image.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        scanManager.onPause();
    }

    /**
     *
     */
    public void scanResult(Result rawResult, Bundle bundle) {
        //扫描成功后，扫描器不会再连续扫描，如需连续扫描，调用reScan()方法。
        //scanManager.reScan();
//		Toast.makeText(that, "result="+rawResult.getText(), Toast.LENGTH_LONG).show();
        dialog = new ProgressDialog(this);
        dialog.setMessage("");
        dialog.setCancelable(false);
        if (!scanManager.isScanning()) { //如果当前不是在扫描状态
            //设置再次扫描按钮出现
            rescan.setVisibility(View.VISIBLE);
            scan_image.setVisibility(View.VISIBLE);
            Bitmap barcode = null;
            byte[] compressedBitmap = bundle.getByteArray(DecodeThread.BARCODE_BITMAP);
            if (compressedBitmap != null) {
                barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
            }
            scan_image.setImageBitmap(barcode);
        }
        rescan.setVisibility(View.VISIBLE);
        scan_image.setVisibility(View.VISIBLE);
        tv_scan_result.setVisibility(View.VISIBLE);
        rawResultText = rawResult.getText();
        tv_scan_result.setText("结果：" + rawResultText);
        if (rawResult != null) {
            dialog.show();
            QueryTask task = new QueryTask();
            task.execute();
        }

    }

    void startScan() {
        if (rescan.getVisibility() == View.VISIBLE) {
            rescan.setVisibility(View.INVISIBLE);
            scan_image.setVisibility(View.GONE);
            scanManager.reScan();
        }
    }

    @Override
    public void scanError(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        //相机扫描出错时
        if (e.getMessage() != null && e.getMessage().startsWith("相机")) {
            scanPreview.setVisibility(View.INVISIBLE);
        }
    }

    public void showPictures(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String photo_path;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTOREQUESTCODE:
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = this.getContentResolver().query(data.getData(), proj, null, null, null);
                    if (cursor.moveToFirst()) {
                        int colum_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        photo_path = cursor.getString(colum_index);
                        if (photo_path == null) {
                            photo_path = Utils.getPath(getApplicationContext(), data.getData());
                        }
                        scanManager.scanningImage(photo_path);
                    }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qrcode_g_gallery:
                if (i != 0) {
                    Intent intent = new Intent(CommonScanActivity.this, PayActivity.class);
                    intent.putExtra("proList", proList);
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtils.showShort(this, "请扫码添加商品");
                }

                break;
            case R.id.iv_light:
                scanManager.switchLight();
                break;
            case R.id.qrcode_ic_back:
                finish();
                break;
            case R.id.service_register_rescan://再次开启扫描
                startScan();
                break;
            case R.id.authorize_return:
                finish();
                break;
            default:
                break;
        }
    }

    private String getShangpinInfo() {
        String s = "";
        SoapObject request = new SoapObject(Constant.SHANGPIN_INFO_PARAM_NAME_SPACE, Constant.GET_SHANGPIN_INFO);
        // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
        request.addProperty("jsonstr", "{\"商品条形码\"" + ":" + "\"" + rawResultText + "\"}");

        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
        envelope.bodyOut = request;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true

        HttpTransportSE httpTransportSE = new HttpTransportSE(Constant.INFO_SERVICE_URL);
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

    class QueryTask extends AsyncTask<String, Integer, String> {
        String s;

        @Override
        protected String doInBackground(String... params) {
            try {
                s = getShangpinInfo();

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
            dialog.dismiss();
            if (mresult != null) {
                if (rawResultText != null) {
                    i++;
                    badge.setText(i + "");
                    badge.show();
                }
                Gson gson = new Gson();
                ShangPinInfo shangPinInfo = gson.fromJson(mresult, ShangPinInfo.class);
                double price = shangPinInfo.get售价();
                String productName = shangPinInfo.get商品名称();
                String proCode = shangPinInfo.get商品编号();
                String unit = shangPinInfo.get包装单位();
                int count = map.containsKey(rawResultText) ? map.get(rawResultText) : 0;
                if (!map.containsKey(rawResultText)) {
                    itemBean = new ItemBean();
                    itemBean.setCount(count+1);
                    itemBean.setItemName(productName);
                    itemBean.setPrice(price);
                    itemBean.setProCode(proCode);
                    itemBean.setChecked(true);
                    itemBean.setUnit(unit);
                }else {
                    itemBean.setCount(count+1);
                }
                map.put(rawResultText, count + 1);

                if (!proList.contains(itemBean)) {
                    proList.add(itemBean);
                }
            } else {
                ToastUtils.showShort(CommonScanActivity.this, "无此商品");
            }
        }
    }

}