/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.glassx.wear.demos;

import static com.google.android.gms.wearable.PutDataRequest.WEAR_URI_SCHEME;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.text.DateFormat;
import java.util.Date;

import cn.glassx.wear.common.Constants;

/**
 * A {@link com.google.android.gms.wearable.WearableListenerService} that is invoked when certain
 * notifications are dismissed from either the phone or watch.
 */
public class PhoneNotificationService extends WearableListenerService
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<DataApi.DeleteDataItemsResult> {

    private static final String TAG = "NotificationService";
    private GoogleApiClient mGoogleApiClient;

    public static final String ACTION_CREATE_LOCAL_ONLY = "cn.glassx.wear.demos.notification.local";
    public static final String ACTION_CREATE_WEAR_ONLY  = "cn.glassx.wear.demos.notification.wear";
    public static final String ACTION_CREATE_BOTH       = "cn.glassx.wear.demos.notification.both";

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_DELETED) {
                if (Constants.BOTH_PATH.equals(dataEvent.getDataItem().getUri().getPath())) {
                    NotificationManagerCompat.from(this).cancel(Constants.BOTH_ID);
                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mGoogleApiClient.isConnected()&&!mGoogleApiClient.isConnecting()){
            mGoogleApiClient.connect();
        }
        if (null != intent) {
            String action = intent.getAction();
            if (Constants.ACTION_DISMISS.equals(action)) {
                int notificationId = intent.getIntExtra(Constants.KEY_NOTIFICATION_ID, -1);
                if (notificationId == Constants.BOTH_ID) {
                    dismissWearableNotification(notificationId);
                }
            } else if (ACTION_CREATE_LOCAL_ONLY.equals(action)) {
                buildLocalOnlyNotification(getString(R.string.phone_only), now(), Constants.PHONE_ONLY_ID, false);
            } else if (ACTION_CREATE_WEAR_ONLY.equals(action)) {
                buildWearableOnlyNotification(getString(R.string.wear_only), now(), Constants.WATCH_ONLY_PATH);
            } else if (ACTION_CREATE_BOTH.equals(action)) {
                buildMirroredNotifications(getString(R.string.phone_both), getString(R.string.watch_both), now());
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void dismissWearableNotification(final int id) {
        mGoogleApiClient.connect();
    }

    @Override // ConnectionCallbacks
    public void onConnected(Bundle bundle) {
        final Uri dataItemUri = new Uri.Builder().scheme(WEAR_URI_SCHEME).path(Constants.BOTH_PATH).build();
        logD("Deleting Uri: " + dataItemUri.toString());
        Wearable.DataApi.deleteDataItems(mGoogleApiClient, dataItemUri).setResultCallback(this);
    }

    @Override // ConnectionCallbacks
    public void onConnectionSuspended(int i) {
    }

    @Override // OnConnectionFailedListener
    public void onConnectionFailed(ConnectionResult connectionResult) {
        logE("Failed to connect to the Google API client");
    }

    @Override // ResultCallback<DataApi.DeleteDataItemsResult>
    public void onResult(DataApi.DeleteDataItemsResult deleteDataItemsResult) {
        if (!deleteDataItemsResult.getStatus().isSuccess()) {
            logE("dismissWearableNotification(): failed to delete DataItem");
        }
//        mGoogleApiClient.disconnect();
    }

    private void buildLocalOnlyNotification(String title, String content, int notificationId, boolean withDismissal) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(title)
                .setContentText(content)
                .setLocalOnly(true)
                .setSmallIcon(R.drawable.ic_launcher);

        if (withDismissal) {
            Intent dismissIntent = new Intent(Constants.ACTION_DISMISS);
            dismissIntent.putExtra(Constants.KEY_NOTIFICATION_ID, Constants.BOTH_ID);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setDeleteIntent(pendingIntent);
        }
        NotificationManagerCompat.from(this).notify(notificationId, builder.build());
    }

    private void buildWearableOnlyNotification(String title, String content, String path) {
        if (mGoogleApiClient.isConnected()) {
            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(path);
            putDataMapRequest.getDataMap().putString(Constants.KEY_CONTENT, content);
            putDataMapRequest.getDataMap().putString(Constants.KEY_TITLE, title);
            PutDataRequest request = putDataMapRequest.asPutDataRequest();
            Wearable.DataApi.putDataItem(mGoogleApiClient, request)
                    .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                        @Override
                        public void onResult(DataApi.DataItemResult dataItemResult) {
                            if (!dataItemResult.getStatus().isSuccess()) {
                                logE("buildWatchOnlyNotification(): Failed to set the data, " + "status: " + dataItemResult.getStatus().getStatusCode());
                            }
                        }
                    });
        } else {
            logE("buildWearableOnlyNotification(): no Google API Client connection");
        }
    }

    private void buildMirroredNotifications(String phoneTitle, String watchTitle, String content) {
        if (mGoogleApiClient.isConnected()) {
            // Wearable notification
            buildWearableOnlyNotification(watchTitle, content, Constants.BOTH_PATH);

            // Local notification, with a pending intent for dismissal
            buildLocalOnlyNotification(phoneTitle, content, Constants.BOTH_ID, true);
        }
    }

    private String now() {
        return android.text.format.DateFormat.format("hh:mm:ss",new Date()).toString();
    }

    private void logD(String msg) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, msg);
        }
    }

    private void logE(String msg) {
        Log.e(TAG, msg);
    }
}
