package com.qctx.www.lkl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qctx.www.lkl.R;
import com.qctx.www.lkl.bean.ItemBean;
import com.qctx.www.lkl.defineview.AmountView;
import com.qctx.www.lkl.utils.ToastUtils;

import java.util.List;

import static android.R.attr.x;

/**
 * Created by admin on 2017/8/31.
 */

public class PayAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<ItemBean> list;
    private double price;

    public PayAdapter(Context mContext, List<ItemBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.pay_item_list, parent, false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ItemBean itemBean = list.get(position);
            ((ViewHolder) holder).tv_th_name.setText(itemBean.getItemName());
            ((ViewHolder) holder).tv_th_price.setText("￥:" + itemBean.getPrice());
            ((ViewHolder) holder).amountView.setEtAmount(itemBean.getCount() + "");
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_th_name, tv_th_price;

        public LinearLayout ll_hidden;
        public LinearLayout ll_item;
        private AmountView amountView;
        public double allPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            amountView = (AmountView) itemView.findViewById(R.id.amount_view);
            tv_th_name = (TextView) itemView.findViewById(R.id.tv_th_name);
            tv_th_price = (TextView) itemView.findViewById(R.id.tv_th_price);
            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_item);
            ll_hidden = (LinearLayout) itemView.findViewById(R.id.ll_hidden);
            amountView.setGoods_storage(1000);
                final double[] allMoney = new double[list.size()];
                amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
                    @Override
                    public void onAmountChange(View view, int amount) {
                        double price = list.get(getPosition()).getPrice();
                        allMoney[getPosition()] = price * amount;
                    }
                });
                for (double v : allMoney) {
                    allPrice = allPrice+v;
                ToastUtils.showLong(mContext, allPrice + ":" + ":" + price);
                }
        }
    }
}
