package com.qctx.www.lkl.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by admin on 2017/3/8.
 */


//Toast统一管理类
public class ToastUtils {

    private volatile static Toast mToast = null;

    private  static  Toast getToastInstance(Context context) {
        if (mToast == null) {
            synchronized (ToastUtils.class) {
                if (mToast == null) {
                    mToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
                }
            }
        }
        return mToast;
    }

    /**
     * 长时间的toast
     * @param text toast的内容
     */
    public static void showLong(Context context,String text) {
        getToastInstance(context).setText(text);
        mToast.show();
    }

    /**
     * 短时间的toast
     * @param text toast的内容
     */
    public static void showShort(Context context,String text) {
        getToastInstance(context).setText(text);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }
}



