package com.example.a_tsu.refrigerator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

////////////////////////////////////////////////
//この仕様ならわざわざ削除用の画面作らなくても//
//ホーム画面で事足りると思う　　　　　　　　　//
////////////////////////////////////////////////
public class DeleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

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
        final ArrayList<Map<String, String>> data = store.loadAll();
        store.close();
        //入力されていないデータがあっても詰まって表示されるから見づらい
        //////////////////
        //縦をそろえたい//
        //////////////////
        SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[]{"item_name", "data"}, new int[]{android.R.id.text1, android.R.id.text2});
        //AdapterをListViewに貼り付け
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        //食材を選択したとき
        //////////////////////////////////////
        //ポップアップ画面の方がよかったかも//
        //ダイアログで実現できる？　　　　　//
        //////////////////////////////////////
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //画面を遷移させる
                setContentView(R.layout.activity_del_no);
                //個数選択画面にあるコンポーネント
                Button plus = findViewById(R.id.plus);
                Button minus = findViewById(R.id.minus);
                Button back = findViewById(R.id.back);
                Button commit = findViewById(R.id.commit);
                final TextView item_name = findViewById(R.id.item_name);

                //選択データの取り出し・切り出し
                //All_data = {item_name=XX,data=期限:XX 品目:XX 量:XX}
                String All_data = data.get(position).toString();
                //配列添え字0に {item_name=XX が、配列添え字1に data=期限:XX 品目:XX 量:XX} がそれぞれ格納される
                String[] sub_data = All_data.split(",", 0);
                //食材名切り出し
                final String name = sub_data[0].substring(11);
                //食材名の表示
                item_name.setText(name);

                String search = "量:";
                //searchがsub_data[1]の中でどこにあるかを検索し、idxに格納
                int idx = sub_data[1].indexOf(search);
                //数量切り出し
                final String cur_number = sub_data[1].substring(idx + search.length(), sub_data[1].length() - 1);

                //決定ボタンを押した
                commit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView item_number = findViewById(R.id.item_number);
                        //String型でとってこれなかったからCharSequence型
                        CharSequence cs = item_number.getText();
                        //number = (現在の個数) - (削除個数)
                        int number = Integer.parseInt(cur_number) - Integer.parseInt(cs.toString());
                        //いくつか削除するとき
                        if (number != 0) {
                            MainStore store = new MainStore(getApplicationContext());
                            store.number_update(name, number);
                            store.close();
                        }
                        //すべて削除するとき
                        else {
                            MainStore store = new MainStore(getApplicationContext());
                            store.delete(name);
                            store.close();
                        }
                        //元の画面に戻す
                        Intent intent = new Intent(getApplication(), DeleteActivity.class);
                        startActivity(intent);
                    }
                });

                //戻るボタンを押した
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //元の画面に戻す
                        Intent intent = new Intent(getApplication(), DeleteActivity.class);
                        startActivity(intent);
                    }
                });

                //plusボタンを押した
                plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView item_number = findViewById(R.id.item_number);
                        //String型でとってこれなかったからCharSequence型
                        CharSequence cs = item_number.getText();
                        int number = Integer.parseInt(cs.toString()) + 1;
                        if (number <= Integer.parseInt(cur_number)) {
                            item_number.setText(String.valueOf(number));
                        }
                    }
                });

                //minusボタンを押した
                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView item_number = findViewById(R.id.item_number);
                        //String型でとってこれなかったからCharSequence型
                        CharSequence cs = item_number.getText();
                        int number = Integer.parseInt(cs.toString()) - 1;
                        if (number >= 0) {
                            item_number.setText(String.valueOf(number));
                        }
                    }
                });
            }
        });
    }
}
