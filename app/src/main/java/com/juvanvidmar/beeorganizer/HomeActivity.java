package com.juvanvidmar.beeorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {
Button newbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButton();
            }
        });
        this.setTitle("Dobrodošel placeholder@placeholder.com");
    }

    public void addButton() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.rootlayout);
        newbtn = new MaterialButton(this);
        newbtn.setText("Čebeljnjak X");
        newbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToBeeHouseActivity();
            }
        });
        layout.addView(newbtn);
    }

    public void goToBeeHouseActivity() {
        Intent intent = new Intent(this, BeeHouseActivity.class);
        startActivity(intent);
    }
}