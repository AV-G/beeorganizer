package com.juvanvidmar.beeorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class HiveActivity extends AppCompatActivity {

    private TextView evidence;
    private TextView status;

    private RequestQueue requestQueue;
    private String url = "https://thebeeorganizer.azurewebsites.net/api/v1/Evidenvca";
    private int hiveId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hive);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        evidence = (TextView) findViewById(R.id.tvEvidenca);
        evidence.setText("");
        status = (TextView) findViewById(R.id.tvStatusHive);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            hiveId = (int) b.get("hiveId");
            String name = (String) b.get("hiveName");
            this.setTitle(name);
        } else
        {
            this.setTitle("Panj");
        }


        Button goBackButton = (Button) findViewById(R.id.butBackHive);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHomeActivity();
            }
        });

        Button goNewButton = (Button) findViewById(R.id.btnAdd);
        goNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNewOverview();
            }
        });
        getEvidences();
    }

    public void getEvidences() {
        status.setText("Processing!");
        JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String currentText = (String) evidence.getText();
                        String newText;
                        boolean pog = false;
                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject object =response.getJSONObject(i);
                                if (hiveId == object.getInt("panjId"))
                                {
                                    newText ="Datum: " + object.getString("datum") +
                                            " Opis: " + object.getString("kratekOpis");

                                    if (pog) {
                                        evidence.setText(currentText + "\n\n" + newText );
                                    } else {
                                        evidence.setText(newText );
                                        pog = true;
                                    }
                                }
                            } catch (JSONException e){
                                e.printStackTrace();
                                return;
                            }

                        }
                        status.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        status.setText("Error!");
                        evidence.setText("Sorry - something go wrong!");
                    }
                });
        requestQueue.add(jsonArrayRequest1);
    }

    public void goToHomeActivity() {
        this.finish();
    }

    public void goToNewOverview() {
        Intent intent = new Intent(getApplicationContext(), NewOverviewActivity.class);
        startActivity(intent);
    }
}