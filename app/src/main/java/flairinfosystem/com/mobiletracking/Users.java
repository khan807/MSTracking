package flairinfosystem.com.mobiletracking;

/**
 * Created by mazher807 on 17-03-2018.
 */

public class Users {

    private String user;
    private double lat;
    private double lon;
    private String busno;
    // private String contactno;

    public Users(){

    }

    public Users(String user, double lat, double lon,String busno){
        this.user=user;
        this.lat=lat;
        this.lon=lon;
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

