package com.example.Sat2021;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

@Controller
public class SatelliteTracking {

    @RequestMapping(value = "/tracking", method = RequestMethod.GET)
    public String getSatTracking(Model model) throws IOException {

        String url = "https://celestrak.com/NORAD/elements/gp.php?GROUP=Active&FORMAT=JSON";
        HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(urlConnection.getInputStream());
        Iterator<JsonNode> albums = rootNode.iterator();
        ArrayList<sat> sats = new ArrayList<sat>();
        while(albums.hasNext()){
            JsonNode album = albums.next();
            String objname = String.valueOf(album.path("OBJECT_NAME"));
            String objid = String.valueOf(album.path("OBJECT_ID"));
            String noradid = String.valueOf(album.path("NORAD_CAT_ID"));
            String inclination = String.valueOf(album.path("INCLINATION"));
            String eccentricity = String.valueOf(album.path("ECCENTRICITY"));
            String meanmot = String.valueOf(album.path("MEAN_MOTION"));
            String meananomaly = String.valueOf(album.path("MEAN_ANOMALY"));
            String raascendingnode = String.valueOf(album.path("RA_OF_ASC_NODE"));

            double mean = Double.parseDouble(meanmot);
            double alt = com.example.Sat2021.Satellites.findAdistEarthToApogee(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(meanmot)), Double.parseDouble(eccentricity));
            String alti = String.valueOf(alt);

            sats.add(new sat(objname, objid, eccentricity, inclination, noradid, meanmot, alti, raascendingnode, meananomaly));

        }

        ArrayList<sat> leos = new ArrayList<sat>();
        ArrayList<sat> meos = new ArrayList<sat>();
        ArrayList<sat> geos = new ArrayList<sat>();


        for(int i=0; i < sats.size(); i++){
            if(Double.parseDouble(sats.get(i).altitude) < 2000){
                leos.add(new sat(sats.get(i).objname, sats.get(i).objid, sats.get(i).eccentricity, sats.get(i).inclin, sats.get(i).noradid, sats.get(i).meanmot, sats.get(i).altitude, sats.get(i).raascendingnode, sats.get(i).meananomaly));

            }
            else if((Double.parseDouble(sats.get(i).altitude) >= 2000) && (Double.parseDouble(sats.get(i).altitude) < 35780)) {
                meos.add(new sat(sats.get(i).objname, sats.get(i).objid, sats.get(i).eccentricity, sats.get(i).inclin, sats.get(i).noradid, sats.get(i).meanmot, sats.get(i).altitude, sats.get(i).raascendingnode, sats.get(i).meananomaly));
            }
            else if(Double.parseDouble(sats.get(i).altitude) >= 35780){
                geos.add(new sat(sats.get(i).objname, sats.get(i).objid, sats.get(i).eccentricity, sats.get(i).inclin, sats.get(i).noradid, sats.get(i).meanmot, sats.get(i).altitude, sats.get(i).raascendingnode, sats.get(i).meananomaly));
            }
        }


        ArrayList<String> LeoOneHourAhead = checkOneHourAhead(leos);
        ArrayList<String> MeoOneHourAhead = checkOneHourAhead(meos);
        ArrayList<String> GeoOneHourAhead = checkOneHourAhead(geos);
        ArrayList<String> LeoTwoHourAhead = checkTwoHourAhead(leos);
        ArrayList<String> MeoTwoHourAhead = checkTwoHourAhead(meos);
        ArrayList<String> GeoTwoHourAhead = checkTwoHourAhead(geos);


        ArrayList<DistFormSats> LeoNow = checkNow(leos);
        ArrayList<DistFormSats> MeoNow = checkNow(meos);
        ArrayList<DistFormSats> GeoNow = checkNow(geos);



        model.addAttribute("LEO", LeoNow);
        model.addAttribute("MEO", MeoNow);
        model.addAttribute("GEO", GeoNow);


        return "tracking.html";
    }

    public static ArrayList<DistFormSats> checkNow(ArrayList<sat> leo){
        DecimalFormat x = new DecimalFormat("###,###.##");

        ArrayList<sat> ltest = leo;
        ArrayList<DistFormSats> finaldistlist = new ArrayList<DistFormSats>();

        for(int i=0; i<ltest.size(); i++) {
            //one hour from now:
            //find mean anomaly
            double meananomhourahead = ((Double.parseDouble(leo.get(0).meananomaly)) / 57.3);
            double meananomhouraheads = meananomhourahead * 57.3;
            //find true anomaly
            double taonehourahead = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheads, Double.parseDouble(leo.get(0).eccentricity));
            double angle = taonehourahead + meananomhouraheads;
            //find radius
            double radonehourahead = com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(leo.get(0).meanmot)), Double.parseDouble(leo.get(0).eccentricity), angle);
            double x1 = com.example.Sat2021.Satellites.getx(radonehourahead, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));
            double y1 = com.example.Sat2021.Satellites.gety(radonehourahead, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));
            double z1 = com.example.Sat2021.Satellites.getz(radonehourahead, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));


            double meananomhouraheadtest = ((Double.parseDouble(leo.get(0).meananomaly)) / 57.3) + 3600;
            double meananomhouraheadstest = meananomhouraheadtest * 57.3;
            //find true anomaly
            double taonehouraheadtest = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheadstest, Double.parseDouble(leo.get(0).eccentricity));
            double angletest = taonehouraheadtest + meananomhouraheadstest;
            //find radius
            double radonehouraheadtest = com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(leo.get(0).meanmot)), Double.parseDouble(leo.get(0).eccentricity), angletest);
            double x1test = com.example.Sat2021.Satellites.getx(radonehouraheadtest, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));
            double y1test = com.example.Sat2021.Satellites.gety(radonehouraheadtest, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));
            double z1test = com.example.Sat2021.Satellites.getz(radonehouraheadtest, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));



            double meananomhouraheadtesttest = ((Double.parseDouble(leo.get(0).meananomaly)) / 57.3) + 7200;
            double meananomhouraheadstesttest = meananomhouraheadtesttest * 57.3;
            //find true anomaly
            double taonehouraheadtesttest = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheadstesttest, Double.parseDouble(leo.get(0).eccentricity));
            double angletesttest = taonehouraheadtesttest + meananomhouraheadstesttest;
            //find radius
            double radonehouraheadtesttest = com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(leo.get(0).meanmot)), Double.parseDouble(leo.get(0).eccentricity), angletesttest);
            double x1testtest = com.example.Sat2021.Satellites.getx(radonehouraheadtesttest, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));
            double y1testtest = com.example.Sat2021.Satellites.gety(radonehouraheadtesttest, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));
            double z1testtest = com.example.Sat2021.Satellites.getz(radonehouraheadtesttest, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));


            double meananomhouraheadtesttesttest = ((Double.parseDouble(leo.get(0).meananomaly)) / 57.3) + 10800;
            double meananomhouraheadstesttesttest = meananomhouraheadtesttesttest * 57.3;
            //find true anomaly
            double taonehouraheadtesttesttest = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheadstesttesttest, Double.parseDouble(leo.get(0).eccentricity));
            double angletesttesttest = taonehouraheadtesttesttest + meananomhouraheadstesttesttest;
            //find radius
            double radonehouraheadtesttesttest = com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(leo.get(0).meanmot)), Double.parseDouble(leo.get(0).eccentricity), angletesttesttest);
            double x1testtesttest = com.example.Sat2021.Satellites.getx(radonehouraheadtesttesttest, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));
            double y1testtesttest = com.example.Sat2021.Satellites.gety(radonehouraheadtesttesttest, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));
            double z1testtesttest = com.example.Sat2021.Satellites.getz(radonehouraheadtesttesttest, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));


            double meananomhouraheadtesttesttesttest = ((Double.parseDouble(leo.get(0).meananomaly)) / 57.3) + 14400;
            double meananomhouraheadstesttesttesttest = meananomhouraheadtesttesttesttest * 57.3;
            //find true anomaly
            double taonehouraheadtesttesttesttest = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheadstesttesttesttest, Double.parseDouble(leo.get(0).eccentricity));
            double angletesttesttesttest = taonehouraheadtesttesttesttest + meananomhouraheadstesttesttesttest;
            //find radius
            double radonehouraheadtesttesttesttest = com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(leo.get(0).meanmot)), Double.parseDouble(leo.get(0).eccentricity), angletesttesttesttest);
            double x1testtesttesttest = com.example.Sat2021.Satellites.getx(radonehouraheadtesttesttesttest, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));
            double y1testtesttesttest = com.example.Sat2021.Satellites.gety(radonehouraheadtesttesttesttest, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));
            double z1testtesttesttest = com.example.Sat2021.Satellites.getz(radonehouraheadtesttesttesttest, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));


            for (int k = 1; k < (ltest.size()); k++) {
                //one hour from now:
                //find mean anomaly
                double meananomhouraheadt = ((Double.parseDouble(leo.get(k).meananomaly)) / 57.3);
                double meananomhouraheadst = meananomhouraheadt * 57.3;
                //find true anomaly
                double taonehouraheadt = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheads, Double.parseDouble(leo.get(k).eccentricity));
                double anglet = taonehouraheadt + meananomhouraheadst;
                //find radius
                double radonehouraheadt = com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(leo.get(k).meanmot)), Double.parseDouble(leo.get(k).eccentricity), anglet);
                double x1t = com.example.Sat2021.Satellites.getx(radonehouraheadt, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));
                double y1t = com.example.Sat2021.Satellites.gety(radonehouraheadt, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));
                double z1t = com.example.Sat2021.Satellites.getz(radonehouraheadt, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));


                double meananomhouraheadttest = ((Double.parseDouble(leo.get(k).meananomaly)) / 57.3) + 3600;
                double meananomhouraheadsttest = meananomhouraheadttest * 57.3;
                //find true anomaly
                double taonehouraheadttest = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheadsttest, Double.parseDouble(leo.get(k).eccentricity));
                double anglettest = taonehouraheadttest + meananomhouraheadsttest;
                //find radius
                double radonehouraheadttest = com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(leo.get(k).meanmot)), Double.parseDouble(leo.get(k).eccentricity), anglettest);
                double x1ttest = com.example.Sat2021.Satellites.getx(radonehouraheadttest, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));
                double y1ttest = com.example.Sat2021.Satellites.gety(radonehouraheadttest, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));
                double z1ttest = com.example.Sat2021.Satellites.getz(radonehouraheadttest, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));



                double meananomhouraheadttesttest = ((Double.parseDouble(leo.get(k).meananomaly)) / 57.3) + 7200;
                double meananomhouraheadsttesttest = meananomhouraheadttesttest * 57.3;
                //find true anomaly
                double taonehouraheadttesttest = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheadsttesttest, Double.parseDouble(leo.get(k).eccentricity));
                double anglettesttest = taonehouraheadttesttest + meananomhouraheadsttesttest;
                //find radius
                double radonehouraheadttesttest = com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(leo.get(k).meanmot)), Double.parseDouble(leo.get(k).eccentricity), anglettesttest);
                double x1ttesttest = com.example.Sat2021.Satellites.getx(radonehouraheadttesttest, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));
                double y1ttesttest = com.example.Sat2021.Satellites.gety(radonehouraheadttesttest, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));
                double z1ttesttest = com.example.Sat2021.Satellites.getz(radonehouraheadttesttest, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));




                double meananomhouraheadttesttesttest = ((Double.parseDouble(leo.get(k).meananomaly)) / 57.3) + 10800;
                double meananomhouraheadsttesttesttest = meananomhouraheadttesttesttest * 57.3;
                //find true anomaly
                double taonehouraheadttesttesttest = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheadsttesttesttest, Double.parseDouble(leo.get(k).eccentricity));
                double anglettesttesttest = taonehouraheadttesttesttest + meananomhouraheadsttesttesttest;
                //find radius
                double radonehouraheadttesttesttest = com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(leo.get(k).meanmot)), Double.parseDouble(leo.get(k).eccentricity), anglettesttesttest);
                double x1ttesttesttest = com.example.Sat2021.Satellites.getx(radonehouraheadttesttesttest, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));
                double y1ttesttesttest = com.example.Sat2021.Satellites.gety(radonehouraheadttesttesttest, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));
                double z1ttesttesttest = com.example.Sat2021.Satellites.getz(radonehouraheadttesttesttest, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));




                double meananomhouraheadttesttesttesttest = ((Double.parseDouble(leo.get(k).meananomaly)) / 57.3) + 14400;
                double meananomhouraheadsttesttesttesttest = meananomhouraheadttesttesttesttest * 57.3;
                //find true anomaly
                double taonehouraheadttesttesttesttest = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheadsttesttesttesttest, Double.parseDouble(leo.get(k).eccentricity));
                double anglettesttesttesttest = taonehouraheadttesttesttesttest + meananomhouraheadsttesttesttesttest;
                //find radius
                double radonehouraheadttesttesttesttest = com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(leo.get(k).meanmot)), Double.parseDouble(leo.get(k).eccentricity), anglettesttesttesttest);
                double x1ttesttesttesttest = com.example.Sat2021.Satellites.getx(radonehouraheadttesttesttesttest, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));
                double y1ttesttesttesttest = com.example.Sat2021.Satellites.gety(radonehouraheadttesttesttesttest, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));
                double z1ttesttesttesttest = com.example.Sat2021.Satellites.getz(radonehouraheadttesttesttesttest, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));




                double disttest = com.example.Sat2021.Satellites.distanceSatellite(x1test, x1ttest, y1test, y1ttest, z1test, z1ttest);
                double disttesttest = com.example.Sat2021.Satellites.distanceSatellite(x1testtest, x1ttesttest, y1testtest, y1ttesttest, z1testtest, z1ttesttest);
                double disttesttesttest = com.example.Sat2021.Satellites.distanceSatellite(x1testtesttest, x1ttesttesttest, y1testtesttest, y1ttesttesttest, z1testtesttest, z1ttesttesttest);
                double disttesttesttesttest = com.example.Sat2021.Satellites.distanceSatellite(x1testtesttesttest, x1ttesttesttesttest, y1testtesttesttest, y1ttesttesttesttest, z1testtesttesttest, z1ttesttesttesttest);


                double dist = com.example.Sat2021.Satellites.distanceSatellite(x1, x1t, y1, y1t, z1, z1t);
                if ((dist < 75) && (disttest < dist) && (disttesttest < disttest) && (disttesttesttest < disttesttest) && (disttesttesttesttest < disttesttesttest)) {
                    finaldistlist.add(new DistFormSats(String.valueOf(x.format(dist)),ltest.get(0).objname, ltest.get(k).objname));
                }
            }
            ltest.remove(0);
        }
        return finaldistlist;
    }














    public static ArrayList<String> checkOneHourAhead(ArrayList<sat> leo){
ArrayList<String> dists = new ArrayList<String>();
        ArrayList<sat> ltest = leo;

        for(int i=0; i<ltest.size(); i++) {
            //one hour from now:
            //find mean anomaly
            double meananomhourahead = ((Double.parseDouble(leo.get(0).meananomaly)) / 57.3) + 3600;
            double meananomhouraheads = meananomhourahead * 57.3;
            //find true anomaly
            double taonehourahead = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheads, Double.parseDouble(leo.get(0).eccentricity));
            double angle = taonehourahead + meananomhouraheads;
            //find radius
            double radonehourahead = com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(leo.get(0).meanmot)), Double.parseDouble(leo.get(0).eccentricity), angle);
            double x1 = com.example.Sat2021.Satellites.getx(radonehourahead, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));
            double y1 = com.example.Sat2021.Satellites.gety(radonehourahead, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));
            double z1 = com.example.Sat2021.Satellites.getz(radonehourahead, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));

            for (int k = 1; k < (ltest.size()); k++) {
                //one hour from now:
                //find mean anomaly
                double meananomhouraheadt = ((Double.parseDouble(leo.get(k).meananomaly)) / 57.3) + 3600;
                double meananomhouraheadst = meananomhouraheadt * 57.3;
                //find true anomaly
                double taonehouraheadt = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheads, Double.parseDouble(leo.get(k).eccentricity));
                double anglet = taonehouraheadt + meananomhouraheadst;
                //find radius
                double radonehouraheadt = com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(leo.get(k).meanmot)), Double.parseDouble(leo.get(k).eccentricity), anglet);
                double x1t = com.example.Sat2021.Satellites.getx(radonehouraheadt, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));
                double y1t = com.example.Sat2021.Satellites.gety(radonehouraheadt, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));
                double z1t = com.example.Sat2021.Satellites.getz(radonehouraheadt, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));
                double dist = com.example.Sat2021.Satellites.distanceSatellite(x1, x1t, y1, y1t, z1, z1t);
                if (dist < 75) {

                    dists.add(String.valueOf(dist));
                    dists.add(ltest.get(0).objname);
                    dists.add(ltest.get(k).objname);
                }
            }
            ltest.remove(0);
        }
        return dists;
    }





    public static ArrayList<String> checkTwoHourAhead(ArrayList<sat> leo){
        ArrayList<String> dists = new ArrayList<String>();
        ArrayList<sat> ltest = leo;

        for(int i=0; i<ltest.size(); i++) {

            //one hour from now:
            //find mean anomaly
            double meananomhourahead = ((Double.parseDouble(leo.get(0).meananomaly)) / 57.3) + 7200;
            double meananomhouraheads = meananomhourahead * 57.3;
            //find true anomaly
            double taonehourahead = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheads, Double.parseDouble(leo.get(0).eccentricity));
            double angle = taonehourahead + meananomhouraheads;
            //find radius
            double radonehourahead = com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(leo.get(0).meanmot)), Double.parseDouble(leo.get(0).eccentricity), angle);

            double x1 = com.example.Sat2021.Satellites.getx(radonehourahead, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));
            double y1 = com.example.Sat2021.Satellites.gety(radonehourahead, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));
            double z1 = com.example.Sat2021.Satellites.getz(radonehourahead, Double.parseDouble(leo.get(0).raascendingnode), Double.parseDouble(leo.get(0).inclin));


            for (int k = 1; k < (ltest.size()); k++) {

                //one hour from now:
                //find mean anomaly
                double meananomhouraheadt = ((Double.parseDouble(leo.get(k).meananomaly)) / 57.3) + 7200;
                double meananomhouraheadst = meananomhouraheadt * 57.3;
                //find true anomaly
                double taonehouraheadt = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheads, Double.parseDouble(leo.get(k).eccentricity));
                double anglet = taonehouraheadt + meananomhouraheadst;
                //find radius
                double radonehouraheadt = com.example.Sat2021.Satellites.findRadius(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(leo.get(k).meanmot)), Double.parseDouble(leo.get(k).eccentricity), anglet);

                double x1t = com.example.Sat2021.Satellites.getx(radonehouraheadt, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));
                double y1t = com.example.Sat2021.Satellites.gety(radonehouraheadt, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));
                double z1t = com.example.Sat2021.Satellites.getz(radonehouraheadt, Double.parseDouble(leo.get(k).raascendingnode), Double.parseDouble(leo.get(k).inclin));


                double dist = com.example.Sat2021.Satellites.distanceSatellite(x1, x1t, y1, y1t, z1, z1t);
                if (dist < 75) {
                    dists.add(String.valueOf(dist));
                    dists.add(ltest.get(0).objname);
                    dists.add(ltest.get(k).objname);
                }
            }
            ltest.remove(0);
        }
        return dists;
    }
}
