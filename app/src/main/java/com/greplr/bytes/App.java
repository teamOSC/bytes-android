package com.greplr.bytes;

import android.app.Application;

/**
 * Created by prempal on 26/7/15.
 */
public class App extends Application {

    public static Query[][] rest = new Query[5][10];

    @Override
    public void onCreate() {
        super.onCreate();
        for (int i = 0; i < 5; i++)
            for (int j = 0; j<10; j++)
                rest[i][j] = new Query();
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

