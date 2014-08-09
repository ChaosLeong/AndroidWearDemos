package cn.glassx.wear.demos;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.GridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.ImageReference;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Chaos
 */
public class GridViewPagerActivity extends Activity {

    private GridViewPager gridViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_pager);
        gridViewPager = (GridViewPager) findViewById(R.id.grid_view_pager);
        DemoGridPagerAdapter adapter = new DemoGridPagerAdapter();
        gridViewPager.setAdapter(adapter);
    }

    /**
     * A simple container for static data in each page
     */
    private static class Page {
        int titleRes;
        int textRes;

        public Page(int titleRes, int textRes) {
            this.titleRes = titleRes;
            this.textRes = textRes;
        }
    }

    private final Page[][] PAGES = {
            {
                    new Page(R.string.welcome_title, R.string.welcome_text),
            },
            {
                    new Page(R.string.about_title, R.string.about_text),
            },
            {
                    new Page(R.string.cards_title, R.string.cards_text),
                    new Page(R.string.expansion_title, R.string.expansion_text),
            },
            {
                    new Page(R.string.backgrounds_title, R.string.backgrounds_text),
                    new Page(R.string.columns_title, R.string.columns_text)
            },
            {
                    new Page(R.string.dismiss_title, R.string.dismiss_text),
            },

    };

    static final int[] BG_IMAGES = new int[]{
            R.drawable.debug_background_1,
            R.drawable.debug_background_2,
            R.drawable.debug_background_3,
            R.drawable.debug_background_4,
            R.drawable.debug_background_5
    };

    private class DemoGridPagerAdapter extends GridPagerAdapter {

        @Override
        public ImageReference getBackground(int row, int column) {
            return ImageReference.forDrawable(BG_IMAGES[row % BG_IMAGES.length]);
        }

        @Override
        public int getRowCount() {
            return PAGES.length;
        }

        @Override
        public int getColumnCount(int i) {
            return PAGES[i].length;
        }

        @Override
        protected Object instantiateItem(ViewGroup viewGroup, int i, int i2) {
            View rootView = getLayoutInflater().inflate(R.layout.item_grid_view_pager, null);
            TextView title = (TextView) rootView.findViewById(R.id.title);
            TextView content = (TextView) rootView.findViewById(R.id.content);
            title.setText(PAGES[i][i2].titleRes);
            content.setText(PAGES[i][i2].textRes);
            viewGroup.addView(rootView);
            return rootView;
        }

        @Override
        protected void destroyItem(ViewGroup viewGroup, int i, int i2, Object o) {

        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }
    }
}
