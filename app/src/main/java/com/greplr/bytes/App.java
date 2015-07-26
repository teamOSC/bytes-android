package com.greplr.bytes;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by prempal on 26/7/15.
 */
public class App extends Application {

    public static Query[][] rest = new Query[5][10];

    @Override
    public void onCreate() {
        super.onCreate();
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 10; j++)
                rest[i][j] = new Query();

        Parse.initialize(this, "YAMSAX6DxYsMyjiHSj3JePmdHACcAcNSXDXnYXSb",
                "2cxbz7pJnSzFhRygVHV05BvTGEv4VLzeICZ4oPYm");
        ParseACL defaultACL = new ParseACL();
        ParseUser.enableAutomaticUser();
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    e.printStackTrace();
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }

    public class Query {
        private String foodItem;
        private int quantity;

        public String getFoodItem() {
            return foodItem;
        }

        public void setFoodItem(String foodItem) {
            this.foodItem = foodItem;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void doTheThing(String s) {
            this.foodItem = s;
            this.quantity++;
        }
    }

}

