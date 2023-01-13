package com.juvanvidmar.beeorganizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;


import javax.microedition.khronos.egl.EGLDisplay;

public class NewOverviewActivity extends AppCompatActivity {

    private EditText opis;
    private EditText moc;
    private TextView status;

    private Switch cistost;
    private Switch mirnost;
    private Switch  rojivost;
    private Switch zalega;
    private Switch zalogaHrane;
    private Switch menjavaMatice;

    private RequestQueue requestQueue;
    private String url = "https://thebeeorganizer.azurewebsites.net/api/v1/Evidenvca";
    private int hiveId;
    private String hiveName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_overview);

        init();
        this.setTitle("Ustvari novo evidenco");
        requestQueue = Volley.newRequestQueue(getApplicationContext());


    }



    public void addOverview(){
        status.setText("Processing!");
        try {
            JSONObject jsonBody = new JSONObject();

            android.text.format.DateFormat df = new DateFormat();
            String datum = (String) df.format("yyyy-MM-ddThh:mm:ss", new Date());

            jsonBody.put("kratekOpis", opis.getText());
            jsonBody.put("cistostCebele", cistost.isChecked());
            jsonBody.put("moc", moc.getText());
            jsonBody.put("mirnost", mirnost.isChecked());
            jsonBody.put("rojivost", rojivost.isChecked());
            jsonBody.put("zalega", zalega.isChecked());
            jsonBody.put("zalogaHrane", zalogaHrane.isChecked());
            jsonBody.put("menjavaMatice", menjavaMatice.isChecked());
            jsonBody.put("datum", datum);
            jsonBody.put("panjId", hiveId);



            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                    status.setText("");

//                    Intent intent = new Intent(getApplicationContext(), HiveActivity.class);
//                    intent.putExtra("hiveId", hiveId);
//                    intent.putExtra("hiveName", hiveName);
//                    startActivity(intent);
                    goToHomeActivity();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    status.setText("Sorry - something went wrong!");
                    Log.e("LOG_VOLLEY", error.toString());
                }
            }
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        //status.setText(responseString);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }

            };

            requestQueue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void goToHomeActivity() {
        this.finish();
    }

    private void init() {
        opis = (EditText) findViewById(R.id.etKratekOpis);
        opis.setText("");
        moc = (EditText) findViewById(R.id.etMoc);
        moc.setText("");
        status = (TextView) findViewById(R.id.tvOverview);
        status.setText("");

        cistost = (Switch) findViewById(R.id.swCistost);
        cistost.setChecked(false);
        mirnost = (Switch) findViewById(R.id.swMirnost);
        mirnost.setChecked(false);
        rojivost = (Switch) findViewById(R.id.swRojivost);
        rojivost.setChecked(false);
        zalega = (Switch) findViewById(R.id.swZalega);
        zalega.setChecked(false);
        zalogaHrane = (Switch) findViewById(R.id.swZaloga);
        zalogaHrane.setChecked(false);
        menjavaMatice = (Switch) findViewById(R.id.swMatica);
        menjavaMatice.setChecked(false);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            hiveId = (int) b.get("hiveId");
            hiveName = (String) b.get("hiveName");
        }

        Button goBackButton = (Button) findViewById(R.id.butBackNewOverview);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHomeActivity();
            }
        });
        Button butSubmit2 = (Button) findViewById(R.id.butSubmit2);
        butSubmit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOverview();
            }
        });
    }
}