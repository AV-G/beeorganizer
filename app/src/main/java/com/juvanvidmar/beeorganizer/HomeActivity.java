package com.juvanvidmar.beeorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {
    Button newBeeHouseButton;
    private RequestQueue requestQueue;
    private TextView dogodekLable;
    private TextView dogodki;
    private String url = "https://thebeeorganizer.azurewebsites.net/api/v1/cabeljnjak/";
    private String url2 = "https://thebeeorganizer.azurewebsites.net/api/v1/dogodek/";
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        dogodekLable = (TextView) findViewById(R.id.TVDogodekLable);
        dogodki = (TextView) findViewById(R.id.TVdogodki);
        dogodekLable.setText("Dogodek:");
        dogodki.setText("");
        Button but = (Button) findViewById(R.id.btn1);
        but.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewBeeHouseActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String firstName =  sharedPreferences.getString("firstName", "");
        String lastName =  sharedPreferences.getString("lastName", "");
        userId =  sharedPreferences.getString("id", "");

        this.setTitle("Dobrodošel "+firstName + " " +lastName);
        getBeeHouses();
        getDogodki();
    }

    public void addButton(String name, int id) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.rootlayoutBeeHouse);
        newBeeHouseButton = new MaterialButton(this);
        newBeeHouseButton.setText(name);
        newBeeHouseButton.setTag(id);
        newBeeHouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToBeeHouseActivity(id, name);
            }
        });
        layout.addView(newBeeHouseButton);
    }

    public void getDogodki() {
        dogodekLable.setText("Dogodek - loading:");
        JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest(Request.Method.GET, url2, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String currentText = (String) dogodki.getText();
                        String newText;
                        boolean pog = false;
                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject object =response.getJSONObject(i);
                                newText ="Naziv: " + object.getString("naziv") +
                                        " Lokacija: " + object.getString("naziv") +
                                        " Opis: " + object.getString("opis");

                                if (pog) {
                                    dogodki.setText(currentText + "\n\n" + newText );
                                } else {
                                    dogodki.setText(newText );
                                    pog = true;
                                }


                            } catch (JSONException e){
                                e.printStackTrace();
                                return;
                            }

                        }
                        dogodekLable.setText("Dogodek:");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dogodekLable.setText("Dogodek - error:");
                        dogodki.setText("Sorry - something go wrong!");
                    }
                });
        requestQueue.add(jsonArrayRequest1);
    }

    public  void getBeeHouses(){
        JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String currentText = (String) dogodki.getText();
                        String newText;
                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject object =response.getJSONObject(i);
                                int id = object.getInt("id");
                                String uId = object.getString("uporabnikId");
                                String name = object.getString("naslov");
                                if(uId.equals(userId)) {
                                    addButton(name, id);
                                }


                            } catch (JSONException e){
                                e.printStackTrace();
                                return;
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        requestQueue.add(jsonArrayRequest2);
      }

    public void goToBeeHouseActivity(int id, String name) {
        Intent intent = new Intent(getApplicationContext(), BeeHouseActivity.class);
        intent.putExtra("beeHouseId", id);
        intent.putExtra("beeHouseName", name);
        startActivity(intent);
    }

}