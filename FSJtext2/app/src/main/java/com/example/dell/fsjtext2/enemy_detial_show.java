package com.example.dell.fsjtext2;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.dell.fsjtext2.Enemy_show.data_ene;


public class enemy_detial_show extends AppCompatActivity {
    private ImageView ene_radar;
    private ImageView ene_friend;
    private ImageView ene_delete;
    private ene_phone_list list = new ene_phone_list();
    private ArrayList<String> ene_data = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enemy_detial_show);
        final String ene_show_number;
        Intent ene_intent=getIntent();
        int i = ene_intent.getIntExtra("panduanshu",0);
        if(0 == i) {
            ene_show_number = ene_intent.getStringExtra("ene_number");
            Double ene_show_jingdu = ene_intent.getDoubleExtra("jingdu", 0);
            Double ene_show_weidu = ene_intent.getDoubleExtra("weidu", 0);
            String x = String.valueOf(ene_show_jingdu);
            String y = String.valueOf(ene_show_weidu);
            TextView enemy_show_jingweidu = (TextView) findViewById(R.id.enemy_detial_jingweidu);
            enemy_show_jingweidu.setText(x+"/"+y);
            TextView ene_detial_number = (TextView) findViewById(R.id.enemy_detial_number);
            ene_detial_number.setText(ene_show_number);

        }
        else {
            String ene_show_name = ene_intent.getStringExtra("ene_name_detial");
            ene_show_number = ene_intent.getStringExtra("ene_number_detial");
            TextView ene_detial_name = (TextView) findViewById(R.id.enemy_detial_name);
            ene_detial_name.setText(ene_show_name);
            TextView ene_detial_number = (TextView) findViewById(R.id.enemy_detial_number);
            ene_detial_number.setText(ene_show_number);
        }
        ene_radar =(ImageView)findViewById(R.id.ene_radar);
        ene_radar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_ene = new Intent(enemy_detial_show.this,MainActivity.class);
                startActivity(intent_ene);
            }
        });
        ene_friend =(ImageView) findViewById(R.id.ene_friend);
        ene_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_ene_friend = new Intent(enemy_detial_show.this,Friend_show.class);
                intent_ene_friend.putExtra("panduanzi","e");
                startActivity(intent_ene_friend);
            }
        });


        ene_delete = (ImageView) findViewById(R.id.ene_detial_delete);
        ene_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ene_data = list.getPhonedata();
                for(int i = 0 ;i<ene_data.size();i++){
                    if (ene_show_number.equals(ene_data.get(i))){
                        ene_data.remove(i);
                        HashMap<String,Object> map_ene=new HashMap<String,Object>();
                        map_ene = data_ene.get(i);
                        data_ene.remove(map_ene);
                        i--;
                        Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_LONG).show();
                        finish();
                        /*Intent intent_e = new Intent(enemy_detial_show.this,Friend_show.class);
                        intent_e.putExtra("panduan","y");
                        intent_e.putExtra("weizhi",i);
                        startActivity(intent_e);*/
                    };
                }



                        //Intent intent = new Intent(enemy_detial_show.this,Enemy_show.class);
                        //intent.putExtra("shanchudedifang",i);
                       // startActivity(intent);

            }
        });
    }


}
