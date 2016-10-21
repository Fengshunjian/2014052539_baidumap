package com.example.dell.fsjtext2;

import java.util.ArrayList;

/**
 * Created by DELL on 2016/10/19.
 */

public class fri_phone_list {
    private String phone;
    public static ArrayList<String> data = new ArrayList<String>();

    public void setPhone( String number){
        this.phone = ""+number;
        data.add(""+phone);
    }
    public ArrayList<String> getPhonedata(){
        return this.data;
    }

}

