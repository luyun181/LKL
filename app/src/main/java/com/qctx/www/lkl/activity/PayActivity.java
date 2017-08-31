package com.qctx.www.lkl.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Button;
import android.widget.TextView;

import com.qctx.www.lkl.R;
import com.qctx.www.lkl.adapter.PayAdapter;
import com.qctx.www.lkl.utils.SwipeRecyclerView;

import java.util.ArrayList;

public class PayActivity extends AppCompatActivity {
    private SwipeRecyclerView recyclerView;
    private Button btn_js;
    private TextView tv_count;
    private Context mContext = this;
    private ArrayList<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        initView();
    }

    private void initView() {
        recyclerView = (SwipeRecyclerView) findViewById(R.id.pay_recycler);
        btn_js = (Button) findViewById(R.id.btn_all);
        tv_count = (TextView) findViewById(R.id.tv_count);
        list.add("扫码");
        list.add("功能1");
        list.add("功能2");
        PayAdapter payAd = new PayAdapter(mContext,list);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(payAd);
    }
}
