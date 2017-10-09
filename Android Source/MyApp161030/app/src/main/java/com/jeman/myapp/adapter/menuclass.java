package com.jeman.myapp.adapter;

/**
 * Created by 신영준 on 2016-09-28.
 */
public class menuclass {
    String menu;
    String from;
    public String getFrom() {
        return from;
    }
    public String getMenu() {
        return menu;
    }
    public void setFrom(String from){ this.from = from; }
    public void setMenu(String menu){ this.menu = menu; }
    public menuclass(String menu, String from){
        this.menu = menu;
        this.from = from;
    }
}
