package com.example.Sat2021;

public class DistFormSats {
    public String dist;
    public String sonename;
    public String stwoname;

    public DistFormSats(String dist, String sonename, String stwoname){
        this.dist = dist;
        this.sonename = sonename;
        this.stwoname = stwoname;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getSonename() {
        return sonename;
    }

    public void setSonename(String sonename) {
        this.sonename = sonename;
    }

    public String getStwoname() {
        return stwoname;
    }

    public void setStwoname(String stwoname) {
        this.stwoname = stwoname;
    }
}
