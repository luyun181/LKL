package com.qctx.www.lkl.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.qctx.www.lkl.CommonScanActivity;
import com.qctx.www.lkl.R;

import java.util.List;

/**
 * Created by admin on 2017/8/22.
 */

public class HomeAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<String> list;

    public HomeAdapter(Context mContext, List list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ((ViewHolder) holder).textView.setText(list.get(position));
        ((ViewHolder) holder).textView.setTextSize(18);
        if (holder instanceof ViewHolder){
            switch (position){
                case 0:
                    Drawable drawable = mContext.getResources().getDrawable(R.drawable.scan);
                    drawable.setBounds(3, 3, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    ((ViewHolder) holder).textView.setCompoundDrawables(null,drawable,null,null);
                    break;
                case 1:
                    Drawable drawable1 = mContext.getResources().getDrawable(R.drawable.question);
                    drawable1.setBounds(3, 3, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                    ((ViewHolder) holder).textView.setCompoundDrawables(null,drawable1,null,null);
                    break;
                case 2:
                    Drawable drawable2 = mContext.getResources().getDrawable(R.drawable.question);
                    drawable2.setBounds(3, 3, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                    ((ViewHolder) holder).textView.setCompoundDrawables(null,drawable2,null,null);
                    break;
                default:
                    break;
            }
      /*      Drawable drawable = mContext.getResources().getDrawable(R.mipmap.ic_launcher);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ((ViewHolder) holder).textView.setCompoundDrawables(null,drawable,null,null);*/

            ((ViewHolder) holder).textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (position) {
                        case 0:
                            mContext.startActivity(new Intent(mContext,CommonScanActivity.class));
                            break;
                        case 1:
                            Toast.makeText(mContext, "开发中", Toast.LENGTH_LONG).show();
                            break;
                        case 2:
                            Toast.makeText(mContext, "开发中", Toast.LENGTH_LONG).show();
                            break;
                    }

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_item);
        }
    }
}
