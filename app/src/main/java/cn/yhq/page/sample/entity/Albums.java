package cn.yhq.page.sample.entity;
import java.io.Serializable;
import java.util.List;
public class Albums implements Serializable  {
    private List<Artists> artists ;

    private boolean available;

    private String company;

    private String cover;

    private int id;

    private String name;

    private int num_tracks;

    private String release_date;

    private String type;

    public void setArtists(List<Artists> artists){
        this.artists = artists;
    }
    public List<Artists> getArtists(){
        return this.artists;
    }
    public void setAvailable(boolean available){
        this.available = available;
    }
    public boolean getAvailable(){
        return this.available;
    }
    public void setCompany(String company){
        this.company = company;
    }
    public String getCompany(){
        return this.company;
    }
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
    public void setNum_tracks(int num_tracks){
        this.num_tracks = num_tracks;
    }
    public int getNum_tracks(){
        return this.num_tracks;
    }
    public void setRelease_date(String release_date){
        this.release_date = release_date;
    }
    public String getRelease_date(){
        return this.release_date;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }

}