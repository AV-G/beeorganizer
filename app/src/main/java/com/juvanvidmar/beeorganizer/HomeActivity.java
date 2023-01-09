package com.juvanvidmar.beeorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {
    Button newBeeHouseButton;
    private RequestQueue requestQueue;
    private TextView osebe;
    private String url = "https://thebeeorganizer.azurewebsites.net/api/v1/cabeljnjak/";
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        Button createBeeHouseButton = (Button) findViewById(R.id.btn1);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String firstName =  sharedPreferences.getString("firstName", "");
        String lastName =  sharedPreferences.getString("lastName", "");
        userId =  sharedPreferences.getString("id", "");

        this.setTitle("Dobrodošel "+firstName + " " +lastName);
        getBeeHouses();
    }

    public void addButton(String name, int id) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.rootlayout);
        newBeeHouseButton = new MaterialButton(this);
        newBeeHouseButton.setText(name);
        newBeeHouseButton.setTag(id);
        newBeeHouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToBeeHouseActivity(id);
            }
        });
        layout.addView(newBeeHouseButton);
    }

    public  void getBeeHouses(){

        JsonArrayRequest request = new JsonArrayRequest(url+userId, jsonArrayListener, errorListener);
        requestQueue.add(request);

    }

    private Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response){
            ArrayList<String> data = new ArrayList<>();

            for (int i = 0; i < response.length(); i++){
                try {
                    JSONObject object =response.getJSONObject(i);
                    int id = object.getInt("id");
                    String name = object.getString("naslov");

                    addButton(name, id);

                } catch (JSONException e){
                    e.printStackTrace();
                    return;
                }
            }
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };


    public void goToBeeHouseActivity(int id) {
        Intent intent = new Intent(this, BeeHouseActivity.class);
        startActivity(intent);
    }

    public void goToNewBeeHouseActivity() {
        Intent intent = new Intent(this, NewBeeHouseActivity.class);
        startActivity(intent);
    }
}