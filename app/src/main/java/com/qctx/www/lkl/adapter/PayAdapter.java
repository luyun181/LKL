package com.qctx.www.lkl.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qctx.www.lkl.R;
import com.qctx.www.lkl.defineview.AmountView;

import java.util.List;

/**
 * Created by admin on 2017/8/31.
 */

public class PayAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<String> list;

    public PayAdapter(Context mContext, List<String> list) {
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
            ((ViewHolder) holder).tv_th_name.setText(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_th_name, tv_th_price;

        public LinearLayout  ll_hidden;
        public LinearLayout ll_item;
        private AmountView amountView;
        public ViewHolder(View itemView) {
            super(itemView);
            amountView = (AmountView) itemView.findViewById(R.id.amount_view);
            tv_th_name = (TextView) itemView.findViewById(R.id.tv_th_name);
            tv_th_price = (TextView) itemView.findViewById(R.id.tv_th_price);
            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_item);
            ll_hidden = (LinearLayout) itemView.findViewById(R.id.ll_hidden);
            amountView.setGoods_storage(200);
            amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
                @Override
                public void onAmountChange(View view, int amount) {

                }
            });
        }
    }
}
