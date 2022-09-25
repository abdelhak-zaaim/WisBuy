package com.strontech.imgautam.handycaft;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HeadlessSmsSendService extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

}
