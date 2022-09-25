package com.strontech.imgautam.handycaft;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.strontech.imgautam.handycaft.activities.LoginActivity;
import com.strontech.imgautam.handycaft.activities.MainActivity;
import com.strontech.imgautam.handycaft.activities.SplashActivity;

import java.util.Timer;
import java.util.TimerTask;

public class HideAppService extends Service {
    public HideAppService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

/*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PackageManager p = getPackageManager();
                p.setComponentEnabledSetting(new ComponentName(getApplicationContext(),SplashActivity.class),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            }
        }, 60*1000*10);

*/

        return super.onStartCommand(intent, flags, startId);
    }
}