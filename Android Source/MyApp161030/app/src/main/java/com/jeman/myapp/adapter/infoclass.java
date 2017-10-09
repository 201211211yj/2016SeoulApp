package com.jeman.myapp.adapter;

/**
 * Created by 신영준 on 2016-09-24.
 */
public class infoclass{
    private String mail;
    private String date;

    String getMail(){
        return this.mail;
    }
    String getDate(){
        return this.date;
    }
    public infoclass(String mail, String date){
        this.mail = mail;
        this.date = date;
    }
}
