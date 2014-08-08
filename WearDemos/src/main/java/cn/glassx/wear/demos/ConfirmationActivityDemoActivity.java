package cn.glassx.wear.demos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.view.View;

/**
 * @author Chaos
 */
public class ConfirmationActivityDemoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_activity_demo_layout);
    }

    public void showDismissActivity(View view) {
        startActivity(new Intent(this,DismissOverlayViewActivity.class));
        startConfirmationActivity(ConfirmationActivity.FAILURE_ANIMATION,
                                  getString(R.string.delete_unsuccessful));
    }

    public void startSuccessConfirmationActivity(View view){
        startConfirmationActivity(ConfirmationActivity.SUCCESS_ANIMATION,"成功");
    }

    public void startFailureConfirmationActivity(View view){
        startConfirmationActivity(ConfirmationActivity.FAILURE_ANIMATION,"失败");
    }

    public void startPhoneConfirmationActivity(View view){
        startConfirmationActivity(ConfirmationActivity.OPEN_ON_PHONE_ANIMATION,"手机打开");
    }

    private void startConfirmationActivity(int animationType, String message) {
        Intent confirmationActivity = new Intent(this, ConfirmationActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION)
                .putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, animationType)
                .putExtra(ConfirmationActivity.EXTRA_MESSAGE, message);
        startActivity(confirmationActivity);
    }

    @Override
    protected void onDestroy() {
        System.out.println("onDestroy");
        super.onDestroy();
    }
}
