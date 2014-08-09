package cn.glassx.wear.demos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @author ChaosLeong
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startSyncNotificationActivity(View v){
        startActivity(SyncNotificationActivity.class);
    }

    private void startActivity(Class<?> c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
