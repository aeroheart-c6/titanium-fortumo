package com.aeroheart.ti.fortumo;

import mp.MpUtils;
import mp.PaymentRequest;

import org.appcelerator.titanium.TiActivitySupportHelpers;
import org.appcelerator.titanium.util.TiActivityResultHandler;
import org.appcelerator.titanium.util.TiActivitySupport;
import org.appcelerator.titanium.util.TiActivitySupportHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.Bundle;


public class PaymentActivity extends mp.PaymentActivity implements TiActivitySupport {
    public static final String EXTRA_PREFIX       = "com.aeroheart.ti.fortumo.intent.extra";
    public static final String EXTRA_SERVICE_ID   = String.format("%s.SERVICE_ID", PaymentActivity.EXTRA_PREFIX);
    public static final String EXTRA_APP_SECRET   = String.format("%s.APP_SECRET", PaymentActivity.EXTRA_PREFIX);
    public static final String EXTRA_PRODUCT_ID   = String.format("%s.PRODUCT_ID", PaymentActivity.EXTRA_PREFIX);
    public static final String EXTRA_PRODUCT_NAME = String.format("%s.PRODUCT_NAME", PaymentActivity.EXTRA_PREFIX);
    public static final String EXTRA_PRODUCT_TYPE = String.format("%s.PRODUCT_TYPE", PaymentActivity.EXTRA_PREFIX);
    
    public static final int CODE_REQUEST_PAYMENT = 1001;
    public static final int CODE_RESULT_BILLED = 2001;
    
    protected TiActivitySupportHelper supportHelper;
    protected PaymentActivity.StatusReceiver statusReceiver;
    protected int supportHelperId;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.statusReceiver = new PaymentActivity.StatusReceiver();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        PaymentRequest.PaymentRequestBuilder builder;
        Bundle extras;
        
        this.registerReceiver(this.statusReceiver,
                              new IntentFilter("mp.info.PAYMENT_STATUS_CHANGED"));
        
        extras  = this.getIntent().getExtras();
        builder = new PaymentRequest.PaymentRequestBuilder();
        builder.setService(extras.getString(PaymentActivity.EXTRA_SERVICE_ID),
                           extras.getString(PaymentActivity.EXTRA_APP_SECRET));
        builder.setProductName(extras.getString(PaymentActivity.EXTRA_PRODUCT_ID))
               .setDisplayString(extras.getString(PaymentActivity.EXTRA_PRODUCT_NAME))
               .setType(MpUtils.PRODUCT_TYPE_CONSUMABLE);
        
        this.makePayment(builder.build());
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        this.unregisterReceiver(this.statusReceiver);
    }
    
    protected TiActivitySupportHelper getSupportHelper() {
        if (this.supportHelper == null) {
            this.supportHelper   = new TiActivitySupportHelper(this);
            this.supportHelperId = TiActivitySupportHelpers.addSupportHelper(this.supportHelper);
        }
        
        return this.supportHelper;
    }
    
    @Override
    public int getUniqueResultCode() {
        return this.getSupportHelper().getUniqueResultCode();
    }

    @Override
    public void launchActivityForResult(Intent intent, int code, TiActivityResultHandler handler) {
        this.getSupportHelper().launchActivityForResult(intent, code, handler);
        
    }

    @Override
    public void launchIntentSenderForResult(
        IntentSender intentSender,
        int resultCode,
        Intent intent,
        int flagsMask,
        int flagsVals,
        int extraFlags,
        Bundle options,
        TiActivityResultHandler handler
    ) {
        this.getSupportHelper().launchIntentSenderForResult(
            intentSender,
            resultCode,
            intent,
            flagsMask,
            flagsVals,
            extraFlags,
            options,
            handler
        );
    }
    
    /*
     ***********************************************************************************************
     * Inner Classes
     ***********************************************************************************************
     */
    protected class StatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            PaymentActivity self = PaymentActivity.this;
            Bundle extras = intent.getExtras();
            int status = extras.getInt("billing_status");
            
            if (status == MpUtils.MESSAGE_STATUS_BILLED) {
                self.setResult(PaymentActivity.CODE_RESULT_BILLED, new Intent());
                self.finish();
            }
        }
    }
}
