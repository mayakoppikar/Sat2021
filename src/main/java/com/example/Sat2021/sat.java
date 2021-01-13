package com.example.Sat2021;

public class sat {
    public String objname;
    public String objid;
    public String eccentricity;
    public String meanmot;
    public String inclin;
    public String noradid;
    public String altitude;
    public String raascendingnode;
    public String meananomaly;

    public sat(String objname, String objid, String eccentricity, String inclin, String noradid, String meanmot, String altitude, String raascendingnode, String meananomaly){
        this.objname = objname;
        this.objid = objid;
        this.eccentricity =eccentricity;
        this.meanmot = meanmot;
        this.inclin = inclin;
        this.noradid = noradid;
        this.altitude = altitude;
        this.raascendingnode = raascendingnode;
        this.meananomaly = meananomaly;
    }

    public String getObjname() {
        return objname;
    }

    public void setObjname(String objname) {
        this.objname = objname;
    }

    public String getObjid() {
        return objid;
    }

    public void setObjid(String objid) {
        this.objid = objid;
    }

    public String getEccentricity() {
        return eccentricity;
    }

    public void setEccentricity(String eccentricity) {
        this.eccentricity = eccentricity;
    }

    public String getMeanmot() {
        return meanmot;
    }

    public void setMeanmot(String meanmot) {
        this.meanmot = meanmot;
    }

    public String getInclin() {
        return inclin;
    }

    public void setInclin(String inclin) {
        this.inclin = inclin;
    }

    public String getNoradid() {
        return noradid;
    }

    public void setNoradid(String noradid) {
        this.noradid = noradid;
    }
}
