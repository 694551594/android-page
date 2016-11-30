package cn.yhq.page.sample.entity;

import java.io.Serializable;

public class Artists implements Serializable  {
    private int id;

    private String name;

    private int num_albums;

    private int num_tracks;

    private String portrait;

    private boolean valid;

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
    public void setNum_albums(int num_albums){
        this.num_albums = num_albums;
    }
    public int getNum_albums(){
        return this.num_albums;
    }
    public void setNum_tracks(int num_tracks){
        this.num_tracks = num_tracks;
    }
    public int getNum_tracks(){
        return this.num_tracks;
    }
    public void setPortrait(String portrait){
        this.portrait = portrait;
    }
    public String getPortrait(){
        return this.portrait;
    }
    public void setValid(boolean valid){
        this.valid = valid;
    }
    public boolean getValid(){
        return this.valid;
    }

}