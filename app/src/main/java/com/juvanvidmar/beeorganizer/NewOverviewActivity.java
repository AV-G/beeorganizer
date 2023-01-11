package com.juvanvidmar.beeorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_overview);

        this.setTitle("Ustvari novo evidenco");

        Button goBackButton = (Button) findViewById(R.id.butBackNewOverview);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHomeActivity();
            }
        });
    }

    public void goToHomeActivity() {
        this.finish();
    }
}