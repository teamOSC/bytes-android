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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_result);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        String json = getIntent().getStringExtra("json");
        try {
            jsonArray = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRecyclerView.setAdapter(new ResultAdapter());
        Toast.makeText(this, "Your order has been placed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_bitcoin) {
            startActivity(new Intent(getApplicationContext(), BitcoinActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
        @Override
        public ResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView v = (CardView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_result, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ResultAdapter.ViewHolder holder, int position) {
            try {
                holder.restaurant.setText(jsonArray.getJSONObject(position).getString("name"));
                holder.amount.setText(jsonArray.getJSONObject(position).getString("cost"));
                holder.time.setText(jsonArray.getJSONObject(position).getString("time") + ":00");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView restaurant;
            TextView amount;
            TextView time;

            public ViewHolder(View itemView) {
                super(itemView);
                restaurant = (TextView) itemView.findViewById(R.id.result_name);
                amount = (TextView) itemView.findViewById(R.id.result_amount);
                time = (TextView) itemView.findViewById(R.id.result_time);
            }
        }
    }
}
