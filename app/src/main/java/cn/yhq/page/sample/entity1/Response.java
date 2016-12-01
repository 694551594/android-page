package cn.yhq.page.sample.entity1;
import java.io.Serializable;
import java.util.List;
public class Response implements Serializable {
    private String appName;

    private String bizType;

    private String customerId;

    private List<Data> data ;

    private String deviceId;

    private String responseCode;

    private String responseTime;

    public void setAppName(String appName){
        this.appName = appName;
    }
    public String getAppName(){
        return this.appName;
    }
    public void setBizType(String bizType){
        this.bizType = bizType;
    }
    public String getBizType(){
        return this.bizType;
    }
    public void setCustomerId(String customerId){
        this.customerId = customerId;
    }
    public String getCustomerId(){
        return this.customerId;
    }
    public void setData(List<Data> data){
        this.data = data;
    }
    public List<Data> getData(){
        return this.data;
    }
    public void setDeviceId(String deviceId){
        this.deviceId = deviceId;
    }
    public String getDeviceId(){
        return this.deviceId;
    }
    public void setResponseCode(String responseCode){
        this.responseCode = responseCode;
    }
    public String getResponseCode(){
        return this.responseCode;
    }
    public void setResponseTime(String responseTime){
        this.responseTime = responseTime;
    }
    public String getResponseTime(){
        return this.responseTime;
    }

}