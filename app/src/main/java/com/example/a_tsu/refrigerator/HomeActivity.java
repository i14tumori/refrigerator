package com.example.a_tsu.refrigerator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                Intent intent = new Intent(getApplication(), NutrientActivity.class);
                startActivity(intent);
            }
        });

        config_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ConfigActivity.class);
                startActivity(intent);
            }
        });

        //SQLite Ver.
        //登録された食材を表示
        //なにも登録されていないとき画面がさみしい

        //Adapterにデータを登録
        MainStore store = new MainStore(getApplicationContext());
        //loadAll()でデータを取り出す
        ArrayList<Map<String, String>> data = store.loadAll();
        store.close();
        //入力されていないデータがあっても詰まって表示されるから見づらい
        //////////////////
        //縦をそろえたい//
        //////////////////
        SimpleAdapter adapter = new SimpleAdapter(this,data,android.R.layout.simple_list_item_2,new String[] {"item_name", "data"},new int[] {android.R.id.text1,android.R.id.text2});
        //AdapterをListViewに貼り付け
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
