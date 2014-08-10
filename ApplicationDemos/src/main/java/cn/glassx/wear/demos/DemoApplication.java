package cn.glassx.wear.demos;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * @author ChaosLeong
 */
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        bindService(new Intent(this, PhoneNotificationService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, BIND_AUTO_CREATE);
    }
}
