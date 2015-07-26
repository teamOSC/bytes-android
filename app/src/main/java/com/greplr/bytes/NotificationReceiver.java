package com.greplr.bytes;

import android.content.Context;
import android.content.Intent;

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

        try {
            pushData = new JSONObject(intent.getStringExtra("com.parse.Data"));
            String message = pushData.getString("alert");
            JSONObject object = new JSONObject(message);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
