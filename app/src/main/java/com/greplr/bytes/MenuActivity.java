package com.greplr.bytes;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private String outletId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        outletId = getIntent().getStringExtra("outlet_id");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_menu);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        new GetTask() {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    Log.d("Test", "" + s);
                    mRecyclerView.setAdapter(new MenuAdapter(new JSONObject(s)));
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }.execute("http://tosc.in:8084/bytes/outlets/info/?out_id=" + outletId);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.menu_primary)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.menu_dark));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.menu_dark));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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

        return super.onOptionsItemSelected(item);
    }

    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

        private JSONArray jsonArray;

        public MenuAdapter(JSONObject jsonObject) {
            try {
                jsonArray = jsonObject.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView v = (CardView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_menu, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            try {
                holder.item.setText(jsonArray.getJSONObject(position).getString("item_name"));
                holder.price.setText("\u20B9" + jsonArray.getJSONObject(position).getString("item_rate"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Log.d("Apprest", outletId + "" + position);
                        (App.rest[(Integer.parseInt(outletId)) - 1][position]).doTheThing(
                                "" + jsonArray.getJSONObject(position).getString("item_name")
                        );
                    } catch (Exception e) {
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

            TextView item;
            TextView price;
            ImageView add;
            View v;

            public ViewHolder(View itemView) {
                super(itemView);
                v = itemView;
                item = (TextView) itemView.findViewById(R.id.menu_item);
                price = (TextView) itemView.findViewById(R.id.menu_price);
                add = (ImageView) itemView.findViewById(R.id.menu_add);
            }
        }
    }
}
