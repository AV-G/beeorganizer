package com.juvanvidmar.beeorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BeeHouseActivity extends AppCompatActivity {

    Button newBeeHouseButton;
    private RequestQueue requestQueue;
    private String url = "https://thebeeorganizer.azurewebsites.net/api/v1/panj/";
    private int beeHouseId;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bee_house);
        status = (TextView) findViewById(R.id.tvStatusHive);
        status.setText("");
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            beeHouseId =(int) b.get("beeHouseId");
        }
        this.setTitle("Čebeljnjak");

        Button goBackButton = (Button) findViewById(R.id.button4);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHomeActivity();
            }
        });
    }

    public  void getHives(){
        status.setText("Processing");
        JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                         for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject object =response.getJSONObject(i);
                                int id = object.getInt("id");
                                String houseId = object.getString("cebeljnjakID");
                                String name = object.getString("naziv");
                                if(houseId.equals(beeHouseId)) {
                                    addButton(name, id);
                                }
                            } catch (JSONException e){
                                e.printStackTrace();
                                return;
                            }
                             status.setText("Done");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        status.setText("Error");
                    }
                });
        requestQueue.add(jsonArrayRequest2);
    }

    public void addButton(String name, int id) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.rootlayout2);
        newBeeHouseButton = new MaterialButton(this);
        newBeeHouseButton.setText(name);
        newBeeHouseButton.setTag(id);
        newBeeHouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHiveActivity(id);
            }
        });
        layout.addView(newBeeHouseButton);
    }

    public void goToHiveActivity(int id) {
        Intent intent = new Intent(getApplicationContext(), BeeHouseActivity.class);
        intent.putExtra("beeHouseId", id);
        startActivity(intent);
    }


    public void goToHomeActivity() {
        this.finish();
    }
}