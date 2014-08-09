package cn.glassx.wear.demos;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.CardFrame;
import android.support.wearable.view.CardScrollView;

/**
 * @author Chaos
 */
public class CardScrollViewActivity extends Activity {

    private CardScrollView cardScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_scroll_view);
        cardScrollView = (CardScrollView) findViewById(R.id.card_scroll_view);
        //设定拓展方向
        cardScrollView.setExpansionDirection(CardFrame.EXPAND_UP);
//        cardScrollView.setExpansionDirection(CardFrame.EXPAND_DOWN);
        cardScrollView.setExpansionEnabled(true);
        cardScrollView.setExpansionFactor(CardFrame.NO_EXPANSION);
    }
}
