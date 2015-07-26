package com.greplr.bytes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FoodCourtActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, BeaconDetectionService.class));
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_foodcourt);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        try {
            mRecyclerView.setAdapter(new FoodCourtAdapter(new JSONObject(Utils.loadJSONFromAsset(
                    getApplicationContext(), "restaurants.json"))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_cart) {
            startActivity(new Intent(this, CartActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class FoodCourtAdapter extends RecyclerView.Adapter<FoodCourtAdapter.ViewHolder> {

        private JSONArray jsonArray;

        public FoodCourtAdapter(JSONObject jsonObject) {
            try {
                jsonArray = jsonObject.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public FoodCourtAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView v = (CardView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_foodcourt, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            try {
                String restName = jsonArray.getJSONObject(position).getString("name");
                holder.name.setText(restName);
                holder.serviceTime.setText(jsonArray.getJSONObject(position).getString("min_service_time"));
                holder.costForTwo.setText(jsonArray.getJSONObject(position).getString("cost_for_two"));

                if (restName.contains("Dunkin")) {
                    holder.logo.setImageDrawable(getResources().getDrawable(R.drawable.logo_dunkin));
                }
                if (restName.contains("Starbucks")) {
                    holder.logo.setImageDrawable(getResources().getDrawable(R.drawable.logo_starbucks));
                }
                if (restName.contains("Subway")) {
                    holder.logo.setImageDrawable(getResources().getDrawable(R.drawable.logo_subway));
                }
                if (restName.contains("Dominos")) {
                    holder.logo.setImageDrawable(getResources().getDrawable(R.drawable.logo_dominos));
                }
                if (restName.contains("McDonalds")) {
                    holder.logo.setImageDrawable(getResources().getDrawable(R.drawable.logo_mcdonalds));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                        intent.putExtra("outlet_id", jsonArray.getJSONObject(position).getString("outlet_id"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            TextView costForTwo;
            TextView serviceTime;
            ImageView logo;
            View v;

            public ViewHolder(View itemView) {
                super(itemView);
                v = itemView;
                name = (TextView) itemView.findViewById(R.id.restaurant_name);
                costForTwo = (TextView) itemView.findViewById(R.id.restaurant_cost);
                serviceTime = (TextView) itemView.findViewById(R.id.restaurant_time);
                logo = (ImageView) itemView.findViewById(R.id.restaurant_logo);
            }
        }
    }
}
