package com.juvanvidmar.beeorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    private EditText mail;
    private EditText password;
    private TextView status;

    private RequestQueue requestQueue;
    private String url = "https://thebeeorganizer.azurewebsites.net/api/login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mail = (EditText) findViewById(R.id.TEemail);
        password = (EditText) findViewById(R.id.TEpassword);
        status = (TextView) findViewById(R.id.TVstatus);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        status.setText("Status: on stand by!");
        this.setTitle("BeeOrganizer - Login");
        password.setText("");
        mail.setText("");
    }
    public void startLogin(View view) {
        // Create the post data
        status.setText("Status: processing!");
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String postData = "{\"username\": \"" + mail.getText() + "\", \"password\": \"" + password.getText() + "\"}";

        // Create the request
        JsonObjectRequest request = null;
        try {
            request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    new JSONObject(postData),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            try {
                                editor.putString("apiKey", response.getString("apiKey"));
                                editor.putString("id", response.getString("id"));
                                editor.putString("username", response.getString("username"));
                                editor.putString("firstName", response.getString("firstName"));
                                editor.putString("lastName", response.getString("lastName"));
                                editor.apply();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // Redirect to the new activity
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            status.setText("Status: login unsuccessful!");
                        }
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Add the request to the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}