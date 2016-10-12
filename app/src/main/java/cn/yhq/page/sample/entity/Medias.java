package cn.yhq.page.sample.entity;
public class Medias {
    private int bitrate;

    private String p2purl;

    public void setBitrate(int bitrate){
        this.bitrate = bitrate;
    }
    public int getBitrate(){
        return this.bitrate;
    }
    public void setP2purl(String p2purl){
        this.p2purl = p2purl;
    }
    public String getP2purl(){
        return this.p2purl;
    }

}