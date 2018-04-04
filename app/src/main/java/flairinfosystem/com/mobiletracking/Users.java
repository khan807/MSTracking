package flairinfosystem.com.mobiletracking;

/**
 * Created by mazher807 on 17-03-2018.
 */

public class Users {

    private String user;
    private double lat;
    private double lon;
//    private double number;
    private String busno;
     private String contactno;

    public Users(){

    }

    public Users(String user, double lat, double lon,String busno,String contactno){
        this.user=user;
        this.lat=lat;
        this.lon=lon;
        this.contactno=contactno;
        this.busno=busno;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getNumber() {
        return contactno;
    }

    public void setNumber(String contactno) {
        this.contactno = contactno;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }


    public String getBusno() {
        return busno;
    }

    public void setBusno(String busno) {
        this.busno = busno;
    }
}

