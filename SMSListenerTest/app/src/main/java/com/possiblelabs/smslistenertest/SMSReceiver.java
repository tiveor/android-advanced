package com.possiblelabs.smslistenertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by possiblelabs on 10/5/15.
 */
public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;
            if (bundle != null) {
                //---retrieve the SMS message received---
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];

                    if (msgs.length > 0) {
                        msgs[0] = SmsMessage.createFromPdu((byte[]) pdus[0]);
                        String msgFrom = msgs[0].getOriginatingAddress();
                        String msgBody = msgs[0].getMessageBody();

                        Intent i = new Intent();
                        i.setClassName("com.possiblelabs.smslistenertest", "com.possiblelabs.smslistenertest.MainActivity");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("FROM", msgFrom);
                        i.putExtra("BODY", msgBody);
                        context.startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
