package com.example.dell.fsjtext2;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.dell.fsjtext2.Friend_show.data_fri;


public class friend_detial_show extends AppCompatActivity {
    private ImageView fri_radar;
    private ImageView fri_enemy;

    private ImageView fri_delete;
    private fri_phone_list list = new fri_phone_list();
    private ArrayList<String> fri_data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detial_show);
        final String fri_show_number;
        Intent fri_intent=getIntent();
        int i = fri_intent.getIntExtra("panduanshu",0);
        if(0 == i) {
            fri_show_number = fri_intent.getStringExtra("fri_number");
            Double fri_show_jingdu = fri_intent.getDoubleExtra("jingdu", 0);
            Double fri_show_weidu = fri_intent.getDoubleExtra("weidu", 0);
            String x = String.valueOf(fri_show_jingdu);
            String y = String.valueOf(fri_show_weidu);
            TextView friend_show_jingweidu = (TextView) findViewById(R.id.friend_detial_jingweidu);
            friend_show_jingweidu.setText(x+"/"+y);
            TextView fri_detial_number = (TextView) findViewById(R.id.friend_detial_number);
            fri_detial_number.setText(fri_show_number);

        }
        else {
            String fri_show_name = fri_intent.getStringExtra("fri_name_detial");
            fri_show_number = fri_intent.getStringExtra("fri_number_detial");
            TextView fri_detial_name = (TextView) findViewById(R.id.friend_detial_name);
            fri_detial_name.setText(fri_show_name);
            TextView fri_detial_number = (TextView) findViewById(R.id.friend_detial_number);
            fri_detial_number.setText(fri_show_number);
        }
        fri_radar = (ImageView) findViewById(R.id.fri_radar);
        fri_radar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_fri =new Intent(friend_detial_show.this,MainActivity.class);
                startActivity(intent_fri);
            }
        });
        fri_enemy =(ImageView) findViewById(R.id.fri_enemy);
        fri_enemy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_fri_enemy =new Intent(friend_detial_show.this,Enemy_show.class);
                intent_fri_enemy.putExtra("panduanzi","f");
                startActivity(intent_fri_enemy);
            }
        });

        fri_delete = (ImageView) findViewById(R.id.fri_detial_delete);
        fri_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fri_data = list.getPhonedata();
                for(int i = 0 ;i<fri_data.size();i++){
                    if (fri_show_number.equals(fri_data.get(i))){
                        fri_data.remove(i);
                        HashMap<String,Object> map_fri=new HashMap<String,Object>();
                        map_fri = data_fri.get(i);
                        data_fri.remove(map_fri);
                        i--;
                        Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_LONG).show();
                        finish();
                       /*Intent intent_f = new Intent(friend_detial_show.this,Friend_show.class);
                        intent_f.putExtra("panduanshu","y");
                        intent_f.putExtra("weizhi",i);
                        startActivity(intent_f);*/
                    };
                }



                        //Intent intent = new Intent(friend_detial_show.this,Friend_show.class);
                        //intent.putExtra("shanchudedifang",i);
                        //startActivity(intent);

            }
        });
    }


}
