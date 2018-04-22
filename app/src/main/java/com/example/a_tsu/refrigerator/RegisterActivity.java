package com.example.a_tsu.refrigerator;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //画面下部のボタン郡と遷移
        //includeのほうがいいかも
        Button home_button = findViewById(R.id.home);
        Button register_button = findViewById(R.id.register);
        Button delete_button = findViewById(R.id.delete);
        Button nutrient_button = findViewById(R.id.nutrient);
        Button config_button = findViewById(R.id.configuration);
        Button next_button = findViewById(R.id.next);

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

        //品目の自動入力
        final EditText name_edit = findViewById(R.id.name_edit);
        name_edit.addTextChangedListener(new TextWatcher() {
            @Override
            //入力が修正される直前に呼び出される
            //引数は順に 現在入力されている文字列, 新たに追加される文字列のスタート位置, 変更された文字列の総数, 新規に追加された文字列の数
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //テキスト変更前に行う
            }

            //文字を一つ入力したときに呼び出される
            //引数は順に 現在入力されている文字列, 新たに追加される文字列のスタート位置, 削除される既存文字列の数, 新規に追加された文字列の数
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //テキスト変更中に行う
                EditText category_edit = findViewById(R.id.category_edit);
                String item_name = charSequence.toString();
                MainStore store = new MainStore(getApplicationContext());
                //食材名の検索
                int row = store.name_seek(item_name);
                //食材名が一つ
                if ( row == 1 ) {
                    category_edit.setText(store.category_seek(item_name));
                }
                //それ以外(食材名の候補が複数個ある)
                else {
                    category_edit.setText("");
                }
                store.close();
            }

            //最後に呼び出される
            //引数は 最終的にできた修正可能な、変更された文字列
            @Override
            public void afterTextChanged(Editable editable) {
                //テキスト変更後に行う
            }
        });

        //カレンダー以外のデータを入力した
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //入力値の保存
                EditText name_edit = findViewById(R.id.name_edit);
                EditText number_edit = findViewById(R.id.number_edit);
                EditText category_edit = findViewById(R.id.category_edit);
                EditText price_edit = findViewById(R.id.price_edit);

                final String item_name = name_edit.getText().toString();
                final String number = number_edit.getText().toString();
                final String category = category_edit.getText().toString();
                final String price = price_edit.getText().toString();

                //入力ミスがある("値が入力されていない" または "量が0")
                if (item_name.equals("") || number.equals("") || number.equals("0") || category.equals("") || price.equals("")){
                    //////////////////////////////////
                    //入力ミスがあることを知らせたい//
                    //とりあえず次の画面に遷移しない//
                    //ようにしている　　　　　　　　//
                    //////////////////////////////////
                    if (item_name.equals("")) {
                        name_edit.setHint("入力してください");
                        name_edit.setHintTextColor(Color.RED);
                    }
                    if (number.equals("")) {
                        number_edit.setHint("入力してください");
                        number_edit.setHintTextColor(Color.RED);
                    }
                    if (number.equals("0")) {
                        number_edit.setHint("値が0です");
                        number_edit.setHintTextColor(Color.RED);
                    }
                    if (category.equals("")) {
                        category_edit.setHint("入力してください");
                        category_edit.setHintTextColor(Color.RED);
                    }
                    if (price.equals("")) {
                        price_edit.setHint("入力してください");
                        price_edit.setHintTextColor(Color.RED);
                    }
                    return;
                }

                setContentView(R.layout.activity_reg_cal);

                //カレンダーの画面にあるコンポーネント
                final DatePicker datePicker = findViewById(R.id.datePicker);
                Button finish_button = findViewById(R.id.finish);
                Button back_button = findViewById(R.id.back);

                ////////////////////////////////
                //カレンダーの範囲を限定したい//
                ////////////////////////////////

                //日付を選択した(値が確定)
                finish_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //入力値の反映
                        //SQLite Ver.
                        String deadline = datePicker.getYear()+"/"+(datePicker.getMonth()+1)+"/"+datePicker.getDayOfMonth();
                        MainStore store = new MainStore(getApplicationContext());
                        //ここで一括してデータを登録する
                        //同じ食材でも関係なく登録しようとする(主キーの関係で登録できてない(エラーは出ない))
                        ////////////////////////////////////////
                        //日付が違うと登録できるようにするべき//
                        ////////////////////////////////////////
                        store.add(item_name,number,category,price,deadline);
                        store.close();

                        //Intentでもう一度登録機能を呼び出す
                        //Viewを戻すだけだとボタンが反応しない　メソッドで呼び出せたらいいかも
                        Intent intent = new Intent(getApplication(), RegisterActivity.class);
                        startActivity(intent);
                    }
                });

                //カレンダー以外のデータを訂正する
                //////////////////////////////////////////////
                //すでにさっきの入力はされてる状態に戻したい//
                //////////////////////////////////////////////
                back_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplication(), RegisterActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
