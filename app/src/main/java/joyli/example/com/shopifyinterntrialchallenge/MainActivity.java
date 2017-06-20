package joyli.example.com.shopifyinterntrialchallenge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

                            for (int i=0;i<orders.length();i++)
                            {
                                JSONObject each = orders.getJSONObject(i);
                                String price = each.getString("total_price");
                                Log.v("totalPrice " + i, price);
                                JSONArray individualItem =each.getJSONArray("line_items");

                                for (int j=0;j<individualItem.length();j++)
                                {
                                    JSONObject eachItem = individualItem.getJSONObject(j);
                                    String price2 = eachItem.getString("price");
                                    String nameOfItem = eachItem.getString("name");
                                    String quantity = eachItem.getString("quantity");

                                    Log.v("individual name " + j, nameOfItem);
                                }
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            Log.wtf("help", "help");
                        }

                    }
                });


    }
}
