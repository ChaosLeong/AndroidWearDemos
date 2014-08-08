package cn.glassx.wear.demos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @author Chaos
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
    }

    public void startBoxInsetActivity(View v){
        startActivity(BoxInsetActivity.class);
    }

    public void startCardScrollViewActivity(View v){
        startActivity(CardScrollViewActivity.class);
    }

    public void startDelayedConfirmationActivity(View v){
        startActivity(DelayedConfirmationActivity.class);
    }

    public void startDemoActivity(View v){
        startActivity(DemoActivity.class);
    }

    public void startDismissOverlayActivity(View v){
        startActivity(DismissOverlayActivity.class);
    }

    public void startGridViewPagerActivity(View v){
        startActivity(GridViewPagerActivity.class);
    }

    public void startWearableListViewActivity(View v){
        startActivity(WearableListViewActivity.class);
    }

    private void startActivity(Class<?> c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
