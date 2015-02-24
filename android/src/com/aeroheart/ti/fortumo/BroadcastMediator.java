package com.aeroheart.ti.fortumo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;


public class BroadcastMediator {
    protected static List<BroadcastMediator.Listener> listeners = new ArrayList<BroadcastMediator.Listener>();
    
    public static void addBroadcastListener(BroadcastMediator.Listener listener) {
        if (BroadcastMediator.listeners.contains(listener))
            return;
        
        BroadcastMediator.listeners.add(listener);
    }
    
    public static void removeBroadcastListener(BroadcastMediator.Listener listener) {
        int index = BroadcastMediator.listeners.indexOf(listener);
        
        if (index < 0)
            return;
        
        BroadcastMediator.listeners.remove(index);
    }
    
    public static void runBroadcastListeners(Context context, Intent intent) {
        for (BroadcastMediator.Listener listener : BroadcastMediator.listeners)
            listener.onReceive(context, intent);
    }
    
    protected BroadcastMediator() {}
    
    /*
     ***********************************************************************************************
     * Inner Classes / Interfaces
     ***********************************************************************************************
     */
    public static interface Listener {
        void onReceive(Context context, Intent intent);
    }
}
