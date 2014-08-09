package cn.glassx.wear.demos;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.CrossfadeDrawable;
import android.widget.ImageView;

/**
 * @author Chaos
 */
public class CrossfadeDrawableActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossfade_drawable);
        ImageView imageView=(ImageView)findViewById(R.id.image);
        CrossfadeDrawable drawable=new CrossfadeDrawable();
        drawable.setBase(getResources().getDrawable(R.drawable.ic_launcher));
        drawable.setFading(getResources().getDrawable(R.drawable.ic_full_cancel));
        drawable.setProgress(0.5f);
        imageView.setImageDrawable(drawable);
    }
}
