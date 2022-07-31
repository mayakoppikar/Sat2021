package com.example.Sat2021;

public class sattrack {

    public String objname;
    public String eccentricity;
    public String meanmot;
    public String inclin;
    public String altitude;
    public String raascendingnode;
    public String meananomaly;
    public double xnow;
    public double ynow;
    public double znow;
    public double x1hour;
    public double y1hour;
    public double z1hour;
    public double x2hour;
    public double y2hour;
    public double z2hour;

    public sattrack(String objname, String eccentricity, String inclin, String meanmot, String altitude, String raascendingnode, String meananomaly){
        this.objname = objname;
        this.eccentricity =eccentricity;
        this.meanmot = meanmot;
        this.inclin = inclin;
        this.altitude = altitude;
        this.raascendingnode = raascendingnode;
        this.meananomaly = meananomaly;
        this.xnow = com.example.Sat2021.Satellites.getx(com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(meanmot)), Double.parseDouble(eccentricity), ((com.example.Sat2021.Satellites.returnTrueAnomaly((((Double.parseDouble(meananomaly)) / 57.3) * 57.3), Double.parseDouble(eccentricity))) + ((((Double.parseDouble(meananomaly)) / 57.3) * 57.3)))),Double.parseDouble(raascendingnode),Double.parseDouble(inclin));
        this.ynow = com.example.Sat2021.Satellites.gety(com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(meanmot)), Double.parseDouble(eccentricity), ((com.example.Sat2021.Satellites.returnTrueAnomaly((((Double.parseDouble(meananomaly)) / 57.3) * 57.3), Double.parseDouble(eccentricity))) + ((((Double.parseDouble(meananomaly)) / 57.3) * 57.3)))),Double.parseDouble(raascendingnode),Double.parseDouble(inclin));
        this.znow = com.example.Sat2021.Satellites.getz(com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(meanmot)), Double.parseDouble(eccentricity), ((com.example.Sat2021.Satellites.returnTrueAnomaly((((Double.parseDouble(meananomaly)) / 57.3) * 57.3), Double.parseDouble(eccentricity))) + ((((Double.parseDouble(meananomaly)) / 57.3) * 57.3)))),Double.parseDouble(raascendingnode),Double.parseDouble(inclin));
        this.x1hour = com.example.Sat2021.Satellites.getx(com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(meanmot)), Double.parseDouble(eccentricity), ((com.example.Sat2021.Satellites.returnTrueAnomaly(((((Double.parseDouble(meananomaly)) / 57.3) + 3600) * 57.3), Double.parseDouble(eccentricity))) + (((((Double.parseDouble(meananomaly)) / 57.3) + 3600) * 57.3)))),Double.parseDouble(raascendingnode),Double.parseDouble(inclin));
        this.y1hour = com.example.Sat2021.Satellites.gety(com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(meanmot)), Double.parseDouble(eccentricity), ((com.example.Sat2021.Satellites.returnTrueAnomaly(((((Double.parseDouble(meananomaly)) / 57.3) + 3600) * 57.3), Double.parseDouble(eccentricity))) + (((((Double.parseDouble(meananomaly)) / 57.3) + 3600) * 57.3)))),Double.parseDouble(raascendingnode),Double.parseDouble(inclin));
        this.z1hour = com.example.Sat2021.Satellites.getz(com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(meanmot)), Double.parseDouble(eccentricity), ((com.example.Sat2021.Satellites.returnTrueAnomaly(((((Double.parseDouble(meananomaly)) / 57.3) + 3600) * 57.3), Double.parseDouble(eccentricity))) + (((((Double.parseDouble(meananomaly)) / 57.3) + 3600) * 57.3)))),Double.parseDouble(raascendingnode),Double.parseDouble(inclin));
        this.x2hour = com.example.Sat2021.Satellites.getx(com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(meanmot)), Double.parseDouble(eccentricity), ((com.example.Sat2021.Satellites.returnTrueAnomaly(((((Double.parseDouble(meananomaly)) / 57.3) + 7200) * 57.3), Double.parseDouble(eccentricity))) + (((((Double.parseDouble(meananomaly)) / 57.3) + 7200) * 57.3)))),Double.parseDouble(raascendingnode),Double.parseDouble(inclin));
        this.y2hour = com.example.Sat2021.Satellites.gety(com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(meanmot)), Double.parseDouble(eccentricity), ((com.example.Sat2021.Satellites.returnTrueAnomaly(((((Double.parseDouble(meananomaly)) / 57.3) + 7200) * 57.3), Double.parseDouble(eccentricity))) + (((((Double.parseDouble(meananomaly)) / 57.3) + 7200) * 57.3)))),Double.parseDouble(raascendingnode),Double.parseDouble(inclin));
        this.z2hour = com.example.Sat2021.Satellites.getz(com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(meanmot)), Double.parseDouble(eccentricity), ((com.example.Sat2021.Satellites.returnTrueAnomaly(((((Double.parseDouble(meananomaly)) / 57.3) + 7200) * 57.3), Double.parseDouble(eccentricity))) + (((((Double.parseDouble(meananomaly)) / 57.3) + 7200) * 57.3)))),Double.parseDouble(raascendingnode),Double.parseDouble(inclin));
    }
}
