package com.juvanvidmar.beeorganizer;

import androidx.appcompat.app.AppCompatActivity;

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
    }

    public void addButton() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.rootlayout);
        newbtn = new MaterialButton(this);
        newbtn.setText("newButton");
        layout.addView(newbtn);
    }
}