package com.example.dell.fsjtext2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Enemy_show extends AppCompatActivity {
    private SimpleAdapter ene_simpleAdapter;
    public static ArrayList<HashMap<String,Object>>data_ene=new ArrayList<HashMap<String, Object>>();
    private TextView textView;
    private ene_phone_list enePhoneList = new ene_phone_list();
    private ArrayList<String> as = new ArrayList<String>();
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enemy_show);

        listView= (ListView) findViewById(R.id.ene_listview);
        //添加朋友
        //点击弹出输入对话框
        data_ene = (ArrayList<HashMap<String, Object>>) getObject("ene_phonelist");
        ImageView ene_add = (ImageView) findViewById(R.id.ene_add);
        ene_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.fri_add_dialog,
                        (ViewGroup) findViewById(R.id.add_dialog) );
                //LayoutInflater inflater_ene = getLayoutInflater();
                //final View ene_lay = inflater_ene.inflate(R.layout.activity_friend_show,null);
                new AlertDialog.Builder(Enemy_show.this).setTitle("添加敌人").setView(layout)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText mingzi = (EditText)layout.findViewById(R.id.editname);
                                EditText haoma = (EditText)layout.findViewById(R.id.editnumber);
                                //TextView ene_content = (TextView)ene_lay.findViewById(R.id.ene_content);
                                if(mingzi != null && haoma != null) {
                                    String name = mingzi.getText().toString();
                                    String number = haoma.getText().toString();
                                    //ene_content.setText(name+":"+number);
                                    setdata(name, number);
                                    saveObject("ene_phonelist",getData());
                                    setshipeiqi();
                                }
                            }
                        })
                        .setNegativeButton("取消", null).show();

            }
        });

        //删除名单

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String,Object> map_object = data_ene.get(position);
                Intent intent = new Intent(Enemy_show.this,enemy_detial_show.class);
                String ene_number_detial = (String) map_object.get("ene_number");
                String ene_name_detial = (String) map_object.get("ene_content");
                intent.putExtra("panduanshu",1);
                intent.putExtra("ene_name_detial",ene_name_detial );
                intent.putExtra("ene_number_detial",ene_number_detial);
                startActivity(intent);
            }
        });

        final ImageView ene_back = (ImageView) findViewById(R.id.ene_back);
        //后退
        ene_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveObject("ene_phonelist",getData());
                finish();
            }
        });

        Intent intent_e = getIntent();
        if(!intent_e.equals(null)) {
            //String panduanshu;
                if (intent_e.getStringExtra("panduanzi").equals("y")) {
                    int pan = intent_e.getIntExtra("weizhi",-1);
                    data_ene.remove(pan);
                    saveObject("ene_phonelist",getData());
                    setshipeiqi();
                }

        }

        setdata("舜坚","15692005753");
        setshipeiqi();
        saveObject("ene_phonelist",getData());

    }
    //设置data数据
    public ArrayList<HashMap<String,Object>> getData(){
        return data_ene;
    }
    public void setdata(String name,String number){
        HashMap<String,Object>map=new HashMap<String,Object>();
        map.put("ene_content",name);
        map.put("ene_delete",R.drawable.delete);
        map.put("ene_number",number);
        data_ene.add(map);
        enePhoneList.setPhone(number);

    }

    public void setshipeiqi(){
        //设置SimpleAdapter
        ene_simpleAdapter = new SimpleAdapter(this,getData(),R.layout.ene_item,new String[]{"ene_content","ene_delete"},new int[]{R.id.ene_content,R.id.ene_delete});
        //ListView绑定适配器

        listView.setAdapter(ene_simpleAdapter);

    }

    //保存对象
    private void saveObject(String name,ArrayList data){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = this.openFileOutput(name, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
            //这里是保存文件产生异常
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    //fos流关闭异常
                    e.printStackTrace();
                }
            }
            if (oos != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    //oos流关闭异常
                    e.printStackTrace();
                }
            }
        }
    }
    //取得对象
    private Object getObject(String name){
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        ArrayList list = new ArrayList();
        try {
            fis = this.openFileInput(name);
            ois = new ObjectInputStream(fis);
            list = (ArrayList) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            //这里是读取文件产生异常
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    //fis流关闭异常
                    e.printStackTrace();
                }
            }
            if (ois != null){
                try {
                    ois.close();
                } catch (IOException e) {
                    //ois流关闭异常
                    e.printStackTrace();
                }
            }
        }
        //读取产生异常，返回null
        return list;
    }

}
