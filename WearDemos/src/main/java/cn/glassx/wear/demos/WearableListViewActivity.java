package cn.glassx.wear.demos;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Chaos
 */
public class WearableListViewActivity extends Activity implements WearableListView.ClickListener {

    private WearableListView wearableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearable_list_view);
        wearableListView = (WearableListView) findViewById(R.id.wearable_listview);
        wearableListView.setAdapter(new DemoAdapter());
        wearableListView.setClickListener(this);
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        ((DemoAdapter.ViewHolder)viewHolder).textView.setText("Test");
    }

    @Override
    public void onTopEmptyRegionClick() {
        Toast.makeText(this,"Test",Toast.LENGTH_SHORT).show();
    }

    private class DemoAdapter extends WearableListView.Adapter {
        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(getLayoutInflater().inflate(R.layout.item_wearable_list, null));
        }

        @Override
        public void onBindViewHolder(WearableListView.ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }

        public class ViewHolder extends WearableListView.ViewHolder {

            public ImageView imageView;
            public TextView  textView;

            public ViewHolder(View rootView) {
                super(rootView);
                imageView = (ImageView) rootView.findViewById(R.id.image);
                textView = (TextView) rootView.findViewById(R.id.text);
            }
        }
    }
}
