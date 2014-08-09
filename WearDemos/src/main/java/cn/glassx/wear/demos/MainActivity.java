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
        setContentView(R.layout.activity_main);
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

    public void startConfirmationActivityDemoActivity(View v){
        startActivity(ConfirmationActivityDemoActivity.class);
    }

    public void startDismissOverlayActivity(View v){
        startActivity(DismissOverlayViewActivity.class);
    }

    public void startGridViewPagerActivity(View v){
        startActivity(GridViewPagerActivity.class);
    }

    public void startWearableListViewActivity(View v){
        startActivity(WearableListViewActivity.class);
    }

    public void startCircleImageViewActivity(View v){
        startActivity(CircledImageViewActivity.class);
    }

    public void startCrossfadeDrawableActivity(View v){
        startActivity(CrossfadeDrawableActivity.class);
    }

    public void startCardFragmentActivity(View v){
        startActivity(CardFragmentActivity.class);
    }

    private void startActivity(Class<?> c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
