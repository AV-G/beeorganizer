package com.juvanvidmar.beeorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BeeHouseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bee_house);
        this.setTitle("Čebeljnjak Placeholder");
        Button goBackButton = (Button) findViewById(R.id.button4);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHomeActivity();
            }
        });
    }

    public void goToHomeActivity() {
        this.finish();
        //Intent intent = new Intent(this, HomeActivity.class);
        //startActivity(intent);
    }
}