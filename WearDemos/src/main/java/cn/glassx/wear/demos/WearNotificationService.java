package cn.glassx.wear.demos;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import static com.google.android.gms.wearable.PutDataRequest.WEAR_URI_SCHEME;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import cn.glassx.wear.common.Constants;

/**
 * @author ChaosLeong
 */
public class WearNotificationService extends WearableListenerService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<DataApi.DeleteDataItemsResult> {

    private static final String TAG = "NotificationService";
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (Constants.ACTION_DISMISS.equals(action)) {
                int notificationId = intent.getIntExtra(Constants.KEY_NOTIFICATION_ID, -1);
                if (notificationId == Constants.BOTH_ID) {
                    dismissPhoneNotification(notificationId);
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void dismissPhoneNotification(int id) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        logD("onDataChanged: " + dataEvents);
        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                String content = dataMap.getString(Constants.KEY_CONTENT);
                String title = dataMap.getString(Constants.KEY_TITLE);
                if (Constants.WATCH_ONLY_PATH.equals(dataEvent.getDataItem().getUri().getPath())) {
                    buildWearableOnlyNotification(title, content, false);
                } else if (Constants.BOTH_PATH.equals(dataEvent.getDataItem().getUri().getPath())) {
                    buildWearableOnlyNotification(title, content, true);
                }
            } else if (dataEvent.getType() == DataEvent.TYPE_DELETED) {
                logD("DataItem deleted: " + dataEvent.getDataItem().getUri().getPath());
                if (Constants.BOTH_PATH.equals(dataEvent.getDataItem().getUri().getPath())) {
                    ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(Constants.BOTH_ID);
                }
            }
        }
    }

    private void buildWearableOnlyNotification(String title, String content, boolean withDismissal) {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(content);

        if (withDismissal) {
            Intent dismissIntent = new Intent(Constants.ACTION_DISMISS);
            dismissIntent.putExtra(Constants.KEY_NOTIFICATION_ID, Constants.BOTH_ID);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setDeleteIntent(pendingIntent);
        }

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(Constants.WATCH_ONLY_ID, builder.build());
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        logD("onMessageReceived");
    }

    @Override // ConnectionCallbacks
    public void onConnected(Bundle bundle) {
        logD("onConnected");
        final Uri dataItemUri = new Uri.Builder().scheme(WEAR_URI_SCHEME).path(Constants.BOTH_PATH).build();
        logD("Deleting Uri: " + dataItemUri.toString());
        Wearable.DataApi.deleteDataItems(mGoogleApiClient, dataItemUri).setResultCallback(this);
    }

    @Override // ConnectionCallbacks
    public void onConnectionSuspended(int i) {
        logD("onConnectionSuspended");
    }

    @Override // OnConnectionFailedListener
    public void onConnectionFailed(ConnectionResult connectionResult) {
        logD("onConnectionFailed");
    }

    @Override // ResultCallback<DataApi.DeleteDataItemsResult>
    public void onResult(DataApi.DeleteDataItemsResult deleteDataItemsResult) {
        logD("onResult");
        if (!deleteDataItemsResult.getStatus().isSuccess()) {
            Log.e(TAG, "dismissWearableNotification(): failed to delete DataItem");
        }
        mGoogleApiClient.disconnect();
    }

    private void logD(String msg) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, msg);
        }
    }
}
