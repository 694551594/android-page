package cn.yhq.page.sample.entity;
import java.util.List;
public class Tracks {
    private Album album;

    private List<Artists> artists ;

    private String availability;

    private String dlyric;

    private int id;

    private List<Medias> medias ;

    private int mv;

    private String slyric;

    private String title;

    private String isdown;

    private String isplay;

    public void setAlbum(Album album){
        this.album = album;
    }
    public Album getAlbum(){
        return this.album;
    }
    public void setArtists(List<Artists> artists){
        this.artists = artists;
    }
    public List<Artists> getArtists(){
        return this.artists;
    }
    public void setAvailability(String availability){
        this.availability = availability;
    }
    public String getAvailability(){
        return this.availability;
    }
    public void setDlyric(String dlyric){
        this.dlyric = dlyric;
    }
    public String getDlyric(){
        return this.dlyric;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setMedias(List<Medias> medias){
        this.medias = medias;
    }
    public List<Medias> getMedias(){
        return this.medias;
    }
    public void setMv(int mv){
        this.mv = mv;
    }
    public int getMv(){
        return this.mv;
    }
    public void setSlyric(String slyric){
        this.slyric = slyric;
    }
    public String getSlyric(){
        return this.slyric;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setIsdown(String isdown){
        this.isdown = isdown;
    }
    public String getIsdown(){
        return this.isdown;
    }
    public void setIsplay(String isplay){
        this.isplay = isplay;
    }
    public String getIsplay(){
        return this.isplay;
    }

}