package joyli.example.com.shopifyinterntrialchallenge;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    SQLdatabaseActivity myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB = new SQLdatabaseActivity(this);
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        Ion.with(this)
                .load("https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        //data has arrived
                        try {
                            //TextView totalPrice = (TextView) findViewById(R.id.price);
                            JSONObject json = new JSONObject(result);
                            JSONArray orders = json.getJSONArray("orders");

                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject each = orders.getJSONObject(i);
                                String price = each.getString("total_price");
                                Log.v("totalPrice " + i, price);
                                JSONArray individualItem = each.getJSONArray("line_items");

                                for (int j = 0; j < individualItem.length(); j++) {
                                    JSONObject eachItem = individualItem.getJSONObject(j);
                                    String price2 = eachItem.getString("price");
                                    String nameOfItem = eachItem.getString("name");
                                    String quantity = eachItem.getString("quantity");
                                    Float revenue = Float.valueOf(price2) * Float.valueOf(quantity);

                                    String newRevenue = revenue.toString();
                                    if (newRevenue.length() != 0) {
                                        AddData(newRevenue);

                                    } else {
                                        Toast.makeText(MainActivity.this, "ERROR!", Toast.LENGTH_LONG).show();
                                    }


                                    Log.v("quantity " + j, quantity);
                                }
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            Log.wtf("help", "help");
                        }

                    }
                });

        progressBar.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent deadPixel = new Intent(getApplicationContext(),ViewList.class);
                startActivity(deadPixel);
            }
        }, 5000);

    }

    public void AddData(String newEntry1) {
        boolean insertData = myDB.addData(newEntry1);

        if (insertData == true) {

            Toast.makeText(this, "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }
}

