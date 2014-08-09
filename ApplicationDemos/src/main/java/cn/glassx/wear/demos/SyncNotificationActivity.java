package cn.glassx.wear.demos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @author ChaosLeong
 */
public class SyncNotificationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_notification);
    }

    public void onClick(View view) {
        int id = view.getId();
        String action = "";
        switch (id) {
            case R.id.phone_only:
                action = PhoneNotificationService.ACTION_CREATE_LOCAL_ONLY;
                break;
            case R.id.wear_only:
                action = PhoneNotificationService.ACTION_CREATE_WEAR_ONLY;
                break;
            case R.id.different_notifications:
                action = PhoneNotificationService.ACTION_CREATE_BOTH;
                break;
        }
        startNotificationService(action);
    }

    private void startNotificationService(String action) {
        startService(new Intent(this, PhoneNotificationService.class)
                .setAction(action));
    }
}
