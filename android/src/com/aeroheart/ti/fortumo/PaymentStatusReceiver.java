package com.aeroheart.ti.fortumo;

import mp.MpUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class PaymentStatusReceiver extends BroadcastReceiver {
    protected static final String LCAT_TAG = "Aeroheart/TiFortumo/PaymentStatusReceiver";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String message = "Payment status changed! [%s]";
        
        switch (extras.getInt("billing_status")) {
            case MpUtils.MESSAGE_STATUS_PENDING:
                message = String.format(message, "Pending");
                break;
            case MpUtils.MESSAGE_STATUS_NOT_SENT:
                message = String.format(message, "Not Sent");
                break;
            case MpUtils.MESSAGE_STATUS_FAILED:
                message = String.format(message, "Failed");
                break;
            case MpUtils.MESSAGE_STATUS_BILLED:
                message = String.format(message, "Billed");
                break;
            default:
                message = String.format(message, "UNKNOWN");
        }
        
        LogUtils.debug(LCAT_TAG, message);
    }
}
