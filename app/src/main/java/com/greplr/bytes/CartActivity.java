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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Cart> cartList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_cart);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        cartList = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 10; j++) {
                if (App.rest[i][j].getQuantity() > 0) {
                    cartList.add(new Cart(App.rest[i][j].getFoodItem(), String.valueOf(App.rest[i][j].getQuantity())));
                }
            }
        mRecyclerView.setAdapter(new CartAdapter());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_place) {
            progressBar.setVisibility(View.VISIBLE);
            String query = "";
            for (int i = 0; i < 5; i++)
                for (int j = 0; j < 10; j++) {
                    if (App.rest[i][j].getQuantity() > 0) {
                        query += (i + 1) + "," + (j + 1) + "," + App.rest[i][j].getQuantity() + "%7C";
                    }
                }
            new GetTask() {
                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    try {
                        Log.d("FinalOrder", s);
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                        intent.putExtra("json", s);
                        startActivity(intent);
                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
            }.execute("http://tosc.in:8084/bytes/outlets/billing?q=" + query.substring(0, query.length() - 3)
                    + "&user_id=42&username=Prempal");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

        @Override
        public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView v = (CardView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_cart, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(CartAdapter.ViewHolder holder, int position) {
            holder.item.setText(cartList.get(position).name);
            holder.rate.setText(cartList.get(position).quantity);
        }

        @Override
        public int getItemCount() {
            return cartList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView item;
            TextView rate;

            public ViewHolder(View itemView) {
                super(itemView);
                item = (TextView) itemView.findViewById(R.id.cart_item);
                rate = (TextView) itemView.findViewById(R.id.cart_rate);
            }
        }
    }

    private class Cart {
        String name;
        String quantity;

        public Cart(String name, String quantity) {
            this.name = name;
            this.quantity = quantity;
        }
    }
}
