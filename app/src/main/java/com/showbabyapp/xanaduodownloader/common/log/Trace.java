package com.showbabyapp.xanaduodownloader.common.log;

import android.util.Log;

/**
 * Created by 秀宝-段誉 on 2016/5/14 17:32.
 * <p/>
 * 日志类
 */
public class Trace {
    public static final String TAG = Trace.class.getSimpleName();
    private static final boolean DEBUG = true;

    public static void i(String i) {
        if (DEBUG)
            Log.d(TAG, i);
    }

    public static void d(String d) {
        if (DEBUG)
            Log.d(TAG, d);
    }

    public static void w(String w) {
        if (DEBUG)
            Log.d(TAG, w);
    }

    public static void e(String e) {
        if (DEBUG)
            Log.d(TAG, e);
    }
}
