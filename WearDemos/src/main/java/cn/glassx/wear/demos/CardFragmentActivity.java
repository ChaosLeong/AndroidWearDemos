package cn.glassx.wear.demos;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Chaos
 */
public class CardFragmentActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_fragment);
        //原生的CardFragment
//        cardFragment = CardFragment.create("Test", "TestContent", R.drawable.ic_launcher);
//        getFragmentManager().beginTransaction().replace(R.id.activity_box_inset, cardFragment).commit();

        getFragmentManager().beginTransaction().replace(R.id.holder, new DemoCardFragment()).commit();
    }

    /**
     * 自定义CardFragment
     */
    public static class DemoCardFragment extends CardFragment {
        @Override
        public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_card, container, false);
            TextView title = (TextView) rootView.findViewById(R.id.title);
            TextView content = (TextView) rootView.findViewById(R.id.content);
            ImageView icon = (ImageView) rootView.findViewById(R.id.icon);
            title.setText("title");
            content.setText("content");
            icon.setImageResource(R.drawable.ic_launcher);
            return rootView;
        }
    }
}
