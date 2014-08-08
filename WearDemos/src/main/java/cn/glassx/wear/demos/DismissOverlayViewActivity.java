package cn.glassx.wear.demos;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.wearable.view.DismissOverlayView;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * @author Chaos
 */
public class DismissOverlayViewActivity extends Activity {
    private GestureDetectorCompat mGestureDetector;
    private DismissOverlayView    mDismissOverlayView;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.dismiss_overlay_view_layout);
        mDismissOverlayView = (DismissOverlayView) findViewById(R.id.dismiss_overlay);
        mDismissOverlayView.setIntroText(R.string.intro_text);
        mDismissOverlayView.showIntroIfNecessary();
        mGestureDetector = new GestureDetectorCompat(this, new LongPressListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private class LongPressListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent event) {
            mDismissOverlayView.show();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event) || super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        System.out.println("onDestroy");
        super.onDestroy();
    }
}
