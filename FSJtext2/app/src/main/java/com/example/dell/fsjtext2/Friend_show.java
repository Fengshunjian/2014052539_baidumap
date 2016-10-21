package com.example.dell.fsjtext2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import static com.example.dell.fsjtext2.R.layout.fri_add_dialog;

public class Friend_show extends AppCompatActivity {
    private SimpleAdapter fri_simpleAdapter;
    public static ArrayList<HashMap<String,Object>>data_fri=new ArrayList<HashMap<String, Object>>();
    private TextView textView;
    private fri_phone_list friPhoneList = new fri_phone_list();
    private ArrayList<String> as = new ArrayList<String>();
    private ListView listView;
//    private ImageView fri_delete = (ImageView) findViewById(R.id.fri_delete);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_show);


        listView= (ListView) findViewById(R.id.fri_listview);
        //添加朋友
        //点击弹出输入对话框
        data_fri = (ArrayList<HashMap<String, Object>>) getObject("fri_phonelist");
        ImageView fri_add = (ImageView) findViewById(R.id.fri_add);
        fri_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.fri_add_dialog,
                        (ViewGroup) findViewById(R.id.add_dialog) );

                new AlertDialog.Builder(Friend_show.this).setTitle("添加好友").setView(layout)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText mingzi = (EditText)layout.findViewById(R.id.editname);
                                EditText haoma = (EditText)layout.findViewById(R.id.editnumber);
                                //TextView fri_content = (TextView) findViewById(R.id.fri_content);
                                if(mingzi != null && haoma != null) {
                                    String name = mingzi.getText().toString();
                                    String number = haoma.getText().toString();
                                   // fri_content.setText(name+":"+number);
                                    setdata(name, number);
                                    saveObject("fri_phonelist",getData());
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

                HashMap<String,Object> map_object = data_fri.get(position);
                Intent intent = new Intent(Friend_show.this,friend_detial_show.class);
                String fri_number_detial = (String) map_object.get("fri_number");
                String fri_name_detial = (String) map_object.get("fri_content");
                intent.putExtra("panduanshu",1);
                intent.putExtra("fri_name_detial",fri_name_detial );
                intent.putExtra("fri_number_detial",fri_number_detial);
                startActivity(intent);
            }
        });

        final ImageView fri_back = (ImageView) findViewById(R.id.fri_back);
        //后退
        fri_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveObject("fri_phonelist",getData());
                finish();
            }
        });


        Intent intent_f = getIntent();
        if(!intent_f.equals(null)) {
            //String panduanshu;
                if (intent_f.getStringExtra("panduanzi").equals("y")) {
                    int pan = intent_f.getIntExtra("weizhi",-1);
                    data_fri.remove(pan);
                    saveObject("fri_phonelist", getData());
                    setshipeiqi();
                }

        }
            setdata("舜坚","15692005753");
            setshipeiqi();
            saveObject("fri_phonelist", getData());

    }
    //设置data数据
    public ArrayList<HashMap<String,Object>> getData(){
        return data_fri;
    }
    public void setdata(String name,String number){
        HashMap<String,Object>map=new HashMap<String,Object>();

        map.put("fri_content",name);
        map.put("fri_delete",R.drawable.delete);
        map.put("fri_number",number);
        data_fri.add(map);
        friPhoneList.setPhone(number);

    }

    public void setshipeiqi(){
        //设置SimpleAdapter
        fri_simpleAdapter = new SimpleAdapter(this,getData(),R.layout.fri_item,new String[]{"fri_content","fri_delete"},new int[]{R.id.fri_content,R.id.fri_delete});
        //ListView绑定适配器

        listView.setAdapter(fri_simpleAdapter);

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
