package cn.glassx.wear.demos;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.BoxInsetLayout;

/**
 * @author Chaos
 */
public class BoxInsetActivity extends Activity {

    private BoxInsetLayout boxInsetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_inset);
        boxInsetLayout = (BoxInsetLayout) findViewById(R.id.box_inset_layout);
    }
}
