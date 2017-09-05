package com.qctx.www.lkl.activity;

import android.app.Activity;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qctx.www.lkl.R;
import com.qctx.www.lkl.adapter.PayAdapter;
import com.qctx.www.lkl.bean.ItemBean;
import com.qctx.www.lkl.bean.TransactionEntity;
import com.qctx.www.lkl.utils.DateTimeUtil;
import com.qctx.www.lkl.utils.OnRecyclerItemClickListener;
import com.qctx.www.lkl.utils.SwipeRecyclerView;
import com.qctx.www.lkl.utils.ToastUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class PayActivity extends AppCompatActivity {
    private static String TAG = PayActivity.class.getName();
    private SwipeRecyclerView recyclerView;
    private Button btn_js;
    private Button btn_code;
    private static TextView tv_count;
    private Context mContext = this;
    private ItemTouchHelper mItemTouchHelper;
    private static ArrayList<ItemBean> porList;
    private static PayAdapter payAd;
    private static final String APPID = "com.qctx.www.lkl";
    private String money;
    private String msg_tp;
    // 打单页面是否自动关闭
    private String returntype = "1";
    private static int a = 0;
    private int b = 1;
    private Gson gson;
    private static String format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        initView();
    }

    private void initView() {
        gson = new Gson();
        recyclerView = (SwipeRecyclerView) findViewById(R.id.pay_recycler);
        btn_js = (Button) findViewById(R.id.btn_all);
        tv_count = (TextView) findViewById(R.id.tv_count);
        btn_code = (Button) findViewById(R.id.bt_scan);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        porList = (ArrayList<ItemBean>) getIntent().getSerializableExtra("proList");

        payAd = new PayAdapter(mContext, porList);

        recyclerView.setAdapter(payAd);
        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {

            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                //判断被拖拽的是否是前两个，如果不是则执行拖拽
                if (vh.getLayoutPosition() != 0 && vh.getLayoutPosition() != 1) {
                    mItemTouchHelper.startDrag(vh);

                    //获取系统震动服务
                    Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);//震动70毫秒
                    vib.vibrate(70);

                }
            }
        });
        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {


            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                } else {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    final int swipeFlags = 0;
//                    final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //得到当拖拽的viewHolder的Position
                int fromPosition = viewHolder.getAdapterPosition();
                //拿到当前拖拽到的item的viewHolder
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(porList, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(porList, i, i - 1);
                    }
                }
                payAd.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
//                myAdapter.notifyItemRemoved(position);
//                datas.remove(position);
            }


            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }


            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }


            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(0);
            }
        });

        mItemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setRightClickListener(new SwipeRecyclerView.OnRightClickListener() {
            @Override
            public void onRightClick(int position, String id) {
                porList.remove(position);
                payAd.notifyItemRemoved(position);
//                payAd.notifyDataSetChanged();
            }
        });
        btn_js.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = (String) tv_count.getText();
                money = temp.substring(2, temp.length());
                Log.d(TAG, money);
                msg_tp = "0200";
                try {
                    Intent intent = setComponent();
                    Bundle bundle = new Bundle();
                    bundle.putString("msg_tp", msg_tp);
                    bundle.putString("pay_tp", "1");
                    bundle.putString("proc_tp", "00");
                    bundle.putString("return_type", returntype);
//                    bundle.putString("adddataword", adddataword);

                    bundle.putString("proc_cd", "660000");
                    bundle.putString("amt", money);
                    bundle.putString("order_no", "2017090500001");
                    bundle.putString("print_info", "");
                    bundle.putString("appid",
                            APPID);
                    bundle.putString("time_stamp",
                            DateTimeUtil.getCurrentDate("yyyyMMddhhmmss"));
                    intent.putExtras(bundle);
                    Log.d(TAG, "========jsonArray======" + bundle.getString("reserve"));
                    startActivityForResult(intent, 1);
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, e.toString());
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });

        btn_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = (String) tv_count.getText();
                money = temp.substring(2, temp.length());
                Log.d(TAG, money);
                msg_tp = "0200";
                try {
                    Intent intent = setComponent();
                    Bundle bundle = new Bundle();
                    bundle.putString("msg_tp", msg_tp);
                    bundle.putString("pay_tp", "1");
                    bundle.putString("proc_tp", "00");
                    bundle.putString("return_type", returntype);
//                    bundle.putString("adddataword", adddataword);

                    bundle.putString("proc_cd", "710000");
                    bundle.putString("amt", money);
                    bundle.putString("order_no", "2017090500001");
                    bundle.putString("print_info", "");
                    bundle.putString("appid",
                            APPID);
                    bundle.putString("time_stamp",
                            DateTimeUtil.getCurrentDate("yyyyMMddhhmmss"));
                    intent.putExtras(bundle);
                    Log.d(TAG, "========jsonArray======" + bundle.getString("reserve"));
                    startActivityForResult(intent, 1);
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, e.toString());
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 应答码
        String msg_tp = data.getExtras().getString("msg_tp");
        // 检索参考号
        String refernumber = data.getExtras().getString("refernumber");
        // 订单号
        String order_no = data.getExtras().getString("order_no");
        // 批次流水号
        String batchbillno = data.getExtras().getString("batchbillno");
        // 失败原因
        String reason = data.getExtras().getString("reason");
        // 时间戳
        String time = data.getExtras().getString("time_stamp");
        // 附加数据
        String addword = data.getExtras().getString("adddataword");
        // 交易详情
        TransactionEntity transactionEntity = gson.fromJson(data.getExtras()
                .getString("txndetail"), TransactionEntity.class);
        Log.i(TAG, "");
        switch (resultCode) {
            // 支付成功
            case Activity.RESULT_OK:
                ToastUtils.showLong(this, "  应答码：" + msg_tp + "\n\r 检索参考号：" + refernumber
                        + "\n\r 订单号：" + order_no + "\n\r 批次流水号：" + batchbillno
                        + "\n\r 时间：" + time + "\n\r 附加数据域：" + addword + "\n\r ");
                if (null != transactionEntity) {
//                    mShow.append(transactionEntity.toString());
                }
                break;
            // 支付取消
            case Activity.RESULT_CANCELED:
                if (reason != null) {
                    ToastUtils.showLong(this, reason);
                }
                break;
            case -2:
                // 交易失败
                if (reason != null) {
                    ToastUtils.showLong(this, " 交易失败：\n\n\r" + reason);
                }
                break;
            default:

                break;
        }
    }

    public static void UpView(boolean checks, String count, int num) {


        double AllPrice = 0;
        int size = 0;//用来计数，判断数据是否被全部选中
        for (ItemBean shop : porList) {
            if (shop.getProCode().equals(count)) {//查找被选中商品id
                shop.setChecked(checks);//改变商品集合shoppingList中的选中状态
                shop.setCount(num);//同时修改商品数量
            }

            if (shop.isChecked()) {//判断商品是否被选中，如被选中计算价格
                size++;
                AllPrice = AllPrice + shop.getPrice() * shop.getCount();//得到被选中商品的总价格
            }
        }
//        payAd.notifyDataSetChanged();
        DecimalFormat df = new DecimalFormat("0.00");
       String format = df.format(AllPrice);
        tv_count.setText("¥:" + format);
     /*   if (size == porList.size()) {
//            all_checks.setChecked(true);//是全部被选中，改变全选check状态为选中
        } else {
            a = 1;//a=1表示all_checks监听中方法不执行
//            if (!all_checks.isChecked()) a = 0;//all_checks未被选中给a赋值0让all_checks的监听中的方法继续执行
//            all_checks.setChecked(false);//不是，继续维持未选中状态
        }*/
    }

    public Intent setComponent() {
        ComponentName component = new ComponentName(
                "com.lkl.cloudpos.payment",
                "com.lkl.cloudpos.payment.activity.MainMenuActivity");
        Intent intent = new Intent();
        intent.setComponent(component);
        return intent;
    }
}
