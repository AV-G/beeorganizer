package com.juvanvidmar.beeorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class NewBeeHouseActivity extends AppCompatActivity {

    private TextView status;
    private EditText naziv;
    private EditText steviloPanjev;

    private RequestQueue requestQueue;
    private String url = "https://thebeeorganizer.azurewebsites.net/api/v1/cabeljnjak/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bee_house);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        status = (TextView) findViewById(R.id.TVstatusPostCebeljnjak);
        status.setText("");
        naziv = (EditText) findViewById(R.id.ETnazivCebeljnjaka);
        steviloPanjev = (EditText) findViewById(R.id.ETstPanjev);

        Button but1 = (Button) findViewById(R.id.butPrekliciCebeljnjak);
        but1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                goToHomeActivity();
            }
        });

        Button but2 = (Button) findViewById(R.id.butSubmit);
        but2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                createNewCebeljnjak();
            }
        });
    }


    public void createNewCebeljnjak(){
        this.status.setText("Posting to " + url);
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String userId =  sharedPreferences.getString("id", "");
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("naziv", String.valueOf(naziv.getText()));
            jsonBody.put("userId", userId);
            jsonBody.put("num", Integer.parseInt(String.valueOf(steviloPanjev.getText())));

            Uri.Builder builder = Uri.parse(url).buildUpon();
            builder.appendQueryParameter("naziv", String.valueOf(naziv.getText()));
            builder.appendQueryParameter("userId", userId);
            builder.appendQueryParameter("num", String.valueOf(steviloPanjev.getText()));
            String url = builder.build().toString();

            final String mRequestBody = jsonBody.toString();

            status.setText("Processing!");



            StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    Log.i("LOG_VOLLEY", response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("accept", "text/plain");
                    return headers;
                }
            };
            requestQueue.add(stringRequest);


//            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    Log.i("LOG_VOLLEY", "response");
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e("LOG_VOLLEY", error.toString());
//                }
//            }
//            ) {
//                @Override
//                public String getBodyContentType() {
//                    return "application/json; charset=utf-8";
//                }
//                @Override
//                public byte[] getBody() throws AuthFailureError {
//                    try {
//                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
//                    } catch (UnsupportedEncodingException uee) {
//                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
//                        return null;
//                    }
//                }
//                @Override
//                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                    String responseString = "";
//                    if (response != null) {
//                        responseString = String.valueOf(response.statusCode);
//
//                    }
//                    if (responseString.equals("201") || responseString.equals("200")) {
//                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                        startActivity(intent);
//                    } else
//                    {
//                        status.setText("Sorry! Something gone wrong!");
//                    }
//
//                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//                }
//
//            };
//
//            requestQueue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void goToHomeActivity() {
        this.finish();
    }
}