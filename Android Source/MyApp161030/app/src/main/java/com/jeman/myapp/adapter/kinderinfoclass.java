package com.jeman.myapp.adapter;

/**
 * Created by 신영준 on 2016-09-24.
 */
public class kinderinfoclass {
    private String name;
    private String phone;
    private String kindertype;
    private String address;
    private String childlimit;
    private String cctv;
    private String bus;
    private String id;
    String getAddress(){
        return this.address;
    }
    String getName(){
        return this.name;
    }
    String getPhone(){
        return this.phone;
    }
    String getKindertype(){
        return this.kindertype;
    }
    String getChildlimit(){
        return this.childlimit;
    }
    String getCctv(){
        return this.cctv;
    }
    String getBus(){
        return this.bus;
    }
    String getId(){
        return this.id;
    }
    public kinderinfoclass(String id,String name,String phone, String kindertype,String address, String childlimit, String cctv, String bus){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.kindertype = kindertype;
        this.childlimit = childlimit;
        this.cctv = cctv;
        this.bus = bus;
    }
}
