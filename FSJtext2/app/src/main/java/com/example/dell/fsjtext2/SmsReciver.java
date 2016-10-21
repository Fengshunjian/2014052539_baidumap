package com.example.dell.fsjtext2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

/**
 * Created by DELL on 2016/10/18.
 */

public class SmsReciver extends BroadcastReceiver{
    private static MessageListener mMessageListener;
    @Override
    public void onReceive(Context context, Intent intent) {

        /*public SmsReciver() {
            super();
        }*/

            Object [] pdus= (Object[]) intent.getExtras().get("pdus");
            for(Object pdu:pdus){
                SmsMessage smsMessage=SmsMessage.createFromPdu((byte [])pdu);
                String sender=smsMessage.getDisplayOriginatingAddress();
                String body=smsMessage.getMessageBody();
                mMessageListener.OnReceived(sender,body);

            }

    }
        // 回调接口
        public interface MessageListener {
            public void OnReceived(String sender,String body);
        }

        public void setOnReceivedMessageListener(MessageListener messageListener) {
            this.mMessageListener=messageListener;
        }

}