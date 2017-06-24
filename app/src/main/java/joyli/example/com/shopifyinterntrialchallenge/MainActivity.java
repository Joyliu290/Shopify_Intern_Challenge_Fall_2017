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
    Double sumOfRevenue=0.00;
    int soldAero=0;
    String aero = "Aerodynamic Cotton Keyboard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB = new SQLdatabaseActivity(this);

       // progressBar.setVisibility(View.VISIBLE);

        Ion.with(this)
                .load("https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        //data has arrived
                        try {
                            //TextView totalPrice = (TextView) findViewById(R.id.price);
                            TextView totalRevenue = (TextView)findViewById(R.id.totalRevenue);
                            TextView AeroQuantity = (TextView)findViewById(R.id.quantityAero);
                            JSONObject json = new JSONObject(result);
                            JSONArray orders = json.getJSONArray("orders");

                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject each = orders.getJSONObject(i);
                                String price = each.getString("total_price");
                                //Log.v("totalPrice " + i, price);
                                JSONArray individualItem = each.getJSONArray("line_items");

                                for (int j = 0; j < individualItem.length(); j++) {
                                    JSONObject eachItem = individualItem.getJSONObject(j);

                                    //Log.v(eachItem.getString("title"), "hi");

                                    if (eachItem.getString("title").equals(aero)) {
                                        String aeroPrice = eachItem.getString("quantity");
                                        int individualAeroQuantity = Integer.valueOf(aeroPrice);
                                        soldAero = soldAero+individualAeroQuantity;
                                        Log.v("sold" + aeroPrice, "hi");

                                    }
                                    String price2 = eachItem.getString("price");
                                    String nameOfItem = eachItem.getString("name");
                                    String quantity = eachItem.getString("quantity");
                                    Double revenue = Math.round(Double.valueOf(price2) * Double.valueOf(quantity)*100.0)/100.0;
                                    sumOfRevenue = sumOfRevenue + revenue;
                                    Log.v("hi "+ revenue, "hello");
                                    //Float newRevenue2 = (float)revenue;
                                    String newRevenue = revenue.toString();
                                }
                            }
                            sumOfRevenue=Math.round(sumOfRevenue*100.0)/100.0;
                            totalRevenue.setText("Total Order Revenue: " + sumOfRevenue.toString());
                            AeroQuantity.setText("Number of sold Aerodynamic Cotton Keyboards: "+ String.valueOf(soldAero));


                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            Log.wtf("help", "help");
                        }

                    }
                });

    }

    public void AddData(String newEntry1, String newEntry2) {
        boolean insertData = myDB.addData(newEntry1, newEntry2);

        if (insertData == true) {

            Toast.makeText(this, "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }
}

