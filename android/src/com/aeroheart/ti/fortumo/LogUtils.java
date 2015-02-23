package com.aeroheart.ti.fortumo;

import org.appcelerator.kroll.common.TiConfig;

import android.util.Log;


public class LogUtils {
    protected static final boolean DEBUG_MODE = TiConfig.LOGD;
    
    public static void debug(String tag, String message) {
        if (!LogUtils.DEBUG_MODE)
            return;
        
        Log.d(tag, message);
    }
    
    public static void info(String tag, String message) {
        Log.i(tag, message);
    }
}
