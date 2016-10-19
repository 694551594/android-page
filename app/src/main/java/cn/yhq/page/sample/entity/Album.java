package cn.yhq.page.sample.entity;

import java.io.Serializable;

public class Album implements Serializable {
    private String cover;

    private int id;

    private String name;

    public void setCover(String cover){
        this.cover = cover;
    }
    public String getCover(){
        return this.cover;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

}