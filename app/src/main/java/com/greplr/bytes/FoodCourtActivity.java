package com.greplr.bytes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
        new GetTask() {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    Log.d("Test", s);
                    mRecyclerView.setAdapter(new FoodCourtAdapter(new JSONObject(s)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute("http://tosc.in:8084/bytes/outlets/fetch/");
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
                holder.name.setText(jsonArray.getJSONObject(position).getString("name"));
                holder.serviceTime.setText(jsonArray.getJSONObject(position).getString("min_service_time"));
                holder.costForTwo.setText(jsonArray.getJSONObject(position).getString("cost_for_two"));
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
            View v;

            public ViewHolder(View itemView) {
                super(itemView);
                v = itemView;
                name = (TextView) itemView.findViewById(R.id.restaurant_name);
                costForTwo = (TextView) itemView.findViewById(R.id.restaurant_cost);
                serviceTime = (TextView) itemView.findViewById(R.id.restaurant_time);
            }
        }
    }
}
