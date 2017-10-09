package com.jeman.myapp;

import java.io.Serializable;

/**
 * Created by 신영준 on 2016-09-05.
 */
public class userInfo implements Serializable {
    private static final long serialVersionUID = 44L;

    private String email;
    private String name;
    private String kinderid;
    private String date;
    private String childname;
    private boolean teacher = false; //2016-09-21
    private boolean ready_register; //2016-09-21
    userInfo(String email, String name, String kinderid, String date, boolean ready_register, String childname){
        this.childname = childname;
        this.email = email;
        this.name = name;
        this.kinderid = kinderid;
        this.date = date;
        this.ready_register = ready_register; //2016-09-21
    }

    public String getDate() {
        return date;
    }

    public String getEmail() {
        return email;
    }

    public String getKinderid() {
        return kinderid;
    }

    public String getName() {
        return name;
    }

    public String getChildname() {return childname;}

    public void setTeacher(){
        this.teacher = true;
    }

    //2016-09-21
    public boolean isTeacher(){
        return teacher;
    }
    public boolean isReady_register(){
        return ready_register;
    }

    public void setChildname(String childname){this.childname = childname;}
    public void setName(String name){this.name = name;}
}
