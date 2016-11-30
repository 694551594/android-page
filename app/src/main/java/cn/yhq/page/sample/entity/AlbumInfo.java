package cn.yhq.page.sample.entity;
import java.io.Serializable;
import java.util.List;

public class AlbumInfo implements Serializable {
    private int album_offset;

    private List<Albums> albums ;

    private int artist_offset;

    private List<Artists> artists ;

    private int dm_error;

    private String error_msg;

    private int recommend;

    private int total_albums;

    private int total_artists;

    private int total_tracks;

    private int track_offset;

    private List<Tracks> tracks ;

    public void setAlbum_offset(int album_offset){
        this.album_offset = album_offset;
    }
    public int getAlbum_offset(){
        return this.album_offset;
    }
    public void setAlbums(List<Albums> albums){
        this.albums = albums;
    }
    public List<Albums> getAlbums(){
        return this.albums;
    }
    public void setArtist_offset(int artist_offset){
        this.artist_offset = artist_offset;
    }
    public int getArtist_offset(){
        return this.artist_offset;
    }
    public void setArtists(List<Artists> artists){
        this.artists = artists;
    }
    public List<Artists> getArtists(){
        return this.artists;
    }
    public void setDm_error(int dm_error){
        this.dm_error = dm_error;
    }
    public int getDm_error(){
        return this.dm_error;
    }
    public void setError_msg(String error_msg){
        this.error_msg = error_msg;
    }
    public String getError_msg(){
        return this.error_msg;
    }
    public void setRecommend(int recommend){
        this.recommend = recommend;
    }
    public int getRecommend(){
        return this.recommend;
    }
    public void setTotal_albums(int total_albums){
        this.total_albums = total_albums;
    }
    public int getTotal_albums(){
        return this.total_albums;
    }
    public void setTotal_artists(int total_artists){
        this.total_artists = total_artists;
    }
    public int getTotal_artists(){
        return this.total_artists;
    }
    public void setTotal_tracks(int total_tracks){
        this.total_tracks = total_tracks;
    }
    public int getTotal_tracks(){
        return this.total_tracks;
    }
    public void setTrack_offset(int track_offset){
        this.track_offset = track_offset;
    }
    public int getTrack_offset(){
        return this.track_offset;
    }
    public void setTracks(List<Tracks> tracks){
        this.tracks = tracks;
    }
    public List<Tracks> getTracks(){
        return this.tracks;
    }

}