package cn.glassx.wear.demos;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

/**
 * @author Chaos
 */
public class WatchViewStubActivity extends Activity {

    private TextView rectTextView;
    private TextView roundTextView;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.main_activity);

        WatchViewStub stub = (WatchViewStub) findViewById(R.id.stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                rectTextView = (TextView) findViewById(R.id.rect_tv);
                roundTextView = (TextView) findViewById(R.id.round_tv);
            }
        });
    }

    public void onTextViewClicked(View view) {
        if (rectTextView != null) {
            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.7f, 1.0f, 0.7f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(300);
            scaleAnimation.setRepeatCount(1);
            scaleAnimation.setRepeatMode(Animation.REVERSE);
            rectTextView.startAnimation(scaleAnimation);
        }
        if (roundTextView != null) {
            roundTextView.animate().rotationBy(360).setDuration(300).start();
        }
    }
}
