package com.example.a_tsu.refrigerator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NutrientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrient);

        //画面下部のボタン郡と遷移
        //includeのほうがいいかも
        Button home_button = findViewById(R.id.home);
        Button register_button = findViewById(R.id.register);
        Button delete_button = findViewById(R.id.delete);
        Button nutrient_button = findViewById(R.id.nutrient);
        Button config_button = findViewById(R.id.configuration);

        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), HomeActivity.class);
                startActivity(intent);
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), DeleteActivity.class);
                startActivity(intent);
            }
        });

        nutrient_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        config_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ConfigActivity.class);
                startActivity(intent);
            }
        });
    }
}
