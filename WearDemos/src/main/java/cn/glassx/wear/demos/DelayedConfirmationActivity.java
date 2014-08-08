package cn.glassx.wear.demos;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.DelayedConfirmationView;
import android.view.View;

/**
 * @author Chaos
 */
public class DelayedConfirmationActivity extends Activity {

    private DelayedConfirmationView delayedConfirmationView;
    private static final int NUM_SECONDS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delayed_confirmation_layout);
        delayedConfirmationView = (DelayedConfirmationView) findViewById(R.id.delayed_confirmation);

        delayedConfirmationView.setTotalTimeMs(NUM_SECONDS * 1000);
    }

    public void onStartTimer(View view) {
        delayedConfirmationView.start();
    }
}
