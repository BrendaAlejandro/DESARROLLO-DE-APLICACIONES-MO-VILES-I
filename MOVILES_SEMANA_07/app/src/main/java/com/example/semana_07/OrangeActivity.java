package com.example.semana_07;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OrangeActivity extends AppCompatActivity {

    Button btnRegresarOrage;
    TextView txtData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_orange);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnRegresarOrage  = findViewById(R.id.btnRegresarOrange);
        btnRegresarOrage .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrangeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Bundle extra = getIntent().getExtras();
        String txt =  (String)extra.get("DATA_FRASES_CELEBRES");
        txtData= findViewById(R.id.txtDataOrange);
        txtData.setText(txt);
    }



}