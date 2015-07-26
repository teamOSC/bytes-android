package com.greplr.bytes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by prempal on 26/7/15.
 */
public class NotificationReceiver extends ParsePushBroadcastReceiver {

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        JSONObject pushData = null;

        Log.d("GCM", "received");

        try {
            pushData = new JSONObject(intent.getStringExtra("com.parse.Data"));
            String message = pushData.getString("alert");
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
