package cn.glassx.wear.demos;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.CardFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Chaos
 */
public class BoxInsetActivity extends Activity {

    private BoxInsetLayout boxInsetLayout;
    private CardFragment   cardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_inset_layout);
        boxInsetLayout = (BoxInsetLayout) findViewById(R.id.box_inset_layout);

        //原生的CardFragment
//        cardFragment = CardFragment.create("Test", "TestContent", R.drawable.ic_launcher);
//        getFragmentManager().beginTransaction().replace(R.id.box_inset_layout, cardFragment).commit();

        getFragmentManager().beginTransaction().replace(R.id.box_inset_layout, new DemoCardFragment()).commit();
    }

    /**
     * 自定义CardFragment
     */
    private class DemoCardFragment extends CardFragment {
        @Override
        public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView=inflater.inflate(R.layout.card_fragment_layout,container,false);
            TextView title=(TextView)rootView.findViewById(R.id.title);
            TextView content=(TextView)rootView.findViewById(R.id.content);
            ImageView icon=(ImageView)rootView.findViewById(R.id.icon);
            title.setText("title");
            content.setText("content");
            icon.setImageResource(R.drawable.ic_launcher);
            return rootView;
        }
    }
}
