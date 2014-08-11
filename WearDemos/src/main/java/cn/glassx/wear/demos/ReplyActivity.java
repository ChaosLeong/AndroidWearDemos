package cn.glassx.wear.demos;

import android.app.Activity;
import android.app.RemoteInput;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author Chaos
 */
public class ReplyActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        ((TextView)findViewById(R.id.text)).setText(getString(R.string.reply_msg_receiver)+getMessageText(getIntent()));
    }

    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(NotificationActivity.VOICE_REPLY);
        }
        return null;
    }
}
