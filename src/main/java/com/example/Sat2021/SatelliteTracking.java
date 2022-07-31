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
        ArrayList<sattrack> leos = new ArrayList<sattrack>();
        ArrayList<sattrack> meos = new ArrayList<sattrack>();
        ArrayList<sattrack> geos = new ArrayList<sattrack>();

        while(albums.hasNext() && (leos.size() <= 500)){
            JsonNode album = albums.next();
            String objname = String.valueOf(album.path("OBJECT_NAME"));
            String inclination = String.valueOf(album.path("INCLINATION"));
            String eccentricity = String.valueOf(album.path("ECCENTRICITY"));
            String meanmot = String.valueOf(album.path("MEAN_MOTION"));
            String meananomaly = String.valueOf(album.path("MEAN_ANOMALY"));
            String raascendingnode = String.valueOf(album.path("RA_OF_ASC_NODE"));

            double alt = com.example.Sat2021.Satellites.findAdistEarthToApogee(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(meanmot)), Double.parseDouble(eccentricity));
            String alti = String.valueOf(alt);

            if(alt < 2000){
                leos.add(new sattrack(objname, eccentricity, inclination, meanmot, alti, raascendingnode, meananomaly));
            }
            if((alt >= 2000) && (alt < 35780)) {
                meos.add(new sattrack(objname, eccentricity, inclination, meanmot, alti, raascendingnode, meananomaly));
            }
            if(alt >= 35780){
                geos.add(new sattrack(objname, eccentricity, inclination, meanmot, alti, raascendingnode, meananomaly));
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

    public static ArrayList<DistFormSats> checkNow(ArrayList<sattrack> leo){
        DecimalFormat x = new DecimalFormat("###,###.##");
        ArrayList<sattrack> ltest = leo;
        ArrayList<DistFormSats> finaldistlist = new ArrayList<DistFormSats>();
        for(int i=0; i<ltest.size(); i++) {
            for (int k = 1; k < (ltest.size()); k++) {
                double dist = com.example.Sat2021.Satellites.distanceSatellite(leo.get(0).xnow, leo.get(k).x1hour, leo.get(0).ynow, leo.get(k).y1hour, leo.get(0).znow, leo.get(k).z1hour);
                double disttest = com.example.Sat2021.Satellites.distanceSatellite(leo.get(0).x1hour, leo.get(k).x2hour, leo.get(0).y1hour, leo.get(k).y2hour, leo.get(0).z1hour, leo.get(k).z2hour);
                if ((dist < 75) && (disttest < dist)) {
                    finaldistlist.add(new DistFormSats(String.valueOf(x.format(dist)),ltest.get(0).objname, ltest.get(k).objname));
                }
            }
            ltest.remove(0);
        }
        return finaldistlist;
    }

    public static ArrayList<String> checkOneHourAhead(ArrayList<sattrack> leo){
        ArrayList<String> dists = new ArrayList<String>();
        ArrayList<sattrack> ltest = leo;
        for(int i=0; i<ltest.size(); i++) {
            for (int k = 1; k < (ltest.size()); k++) {
                double dist = com.example.Sat2021.Satellites.distanceSatellite(leo.get(0).xnow, leo.get(k).x1hour, leo.get(0).ynow, leo.get(k).y1hour, leo.get(0).znow, leo.get(k).z1hour);
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

    public static ArrayList<String> checkTwoHourAhead(ArrayList<sattrack> leo){
        ArrayList<String> dists = new ArrayList<String>();
        ArrayList<sattrack> ltest = leo;

        for(int i=0; i<ltest.size(); i++) {
            for (int k = 1; k < (ltest.size()); k++) {
                double dist = com.example.Sat2021.Satellites.distanceSatellite(leo.get(0).x1hour, leo.get(k).x2hour, leo.get(0).y1hour, leo.get(k).y2hour, leo.get(0).z1hour, leo.get(k).z2hour);
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