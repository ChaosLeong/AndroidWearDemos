package cn.glassx.wear.demos;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static com.google.android.gms.wearable.PutDataRequest.WEAR_URI_SCHEME;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

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
                    buildWearableOnlyNotification(title, content, Constants.WATCH_ONLY_ID, false);
                } else if (Constants.BOTH_PATH.equals(dataEvent.getDataItem().getUri().getPath())) {
                    buildWearableOnlyNotification(title, content, Constants.WATCH_ONLY_ID, true);
                } else if (Constants.IMAGE_WATCH_ONLY_PATH.equals(dataEvent.getDataItem().getUri().getPath())) {
                    Asset asset = dataMap.getAsset(Constants.KEY_IMAGE);
                    Bitmap background = null;
                    if (asset != null) {
                        background = loadBitmapFromAsset(asset);
                    }
                    buildNotificationWithBackground(title, content, Constants.MSG_WATCH_ONLY_ID, background);
                }
            } else if (dataEvent.getType() == DataEvent.TYPE_DELETED) {
                logD("DataItem deleted: " + dataEvent.getDataItem().getUri().getPath());
                if (Constants.BOTH_PATH.equals(dataEvent.getDataItem().getUri().getPath())) {
                    ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(Constants.BOTH_ID);
                }
            }
        }
    }

    private void buildWearableOnlyNotification(String title, String content, int notificationId, boolean withDismissal) {
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

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(notificationId, builder.build());
    }

    private void buildNotificationWithBackground(String title, String content, int notificationId, Bitmap background) {
        Notification.WearableExtender wearableExtender = new Notification.WearableExtender().setBackground(background);

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .extend(wearableExtender);

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(notificationId, builder.build());
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        logD("onMessageReceived");
        String path = messageEvent.getPath();
        if (Constants.MSG_WATCH_ONLY_PATH.equals(path)) {
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(Constants.MSG_WATCH_ONLY_ID);
            DataMap dataMap = DataMap.fromByteArray(messageEvent.getData());
            String title = dataMap.getString(Constants.KEY_TITLE);
            String content = dataMap.getString(Constants.KEY_CONTENT);
            buildWearableOnlyNotification(title, content, Constants.WATCH_ONLY_ID, false);
        }
    }

    private Bitmap loadBitmapFromAsset(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }
        ConnectionResult result = mGoogleApiClient.blockingConnect(30, TimeUnit.MILLISECONDS);
        if (!result.isSuccess()) {
            return null;
        }
        //todo 有时会崩溃，原因未查明
        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(mGoogleApiClient, asset).await().getInputStream();
        mGoogleApiClient.disconnect();
        if (assetInputStream == null) {
            Log.w(TAG, "Requested an unknown Asset.");
            return null;
        }
        return BitmapFactory.decodeStream(assetInputStream);
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
