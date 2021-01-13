package com.example.Sat2021;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

@Controller
public class DistanceRealController {

    @RequestMapping(value = "/distanceformreal", method = RequestMethod.GET)
    public String getDistanceRealForm(Model model) throws IOException {

        return "distformreal";
    }

    @RequestMapping(value = "/distanceformreal", method = RequestMethod.POST)
    public String distanceReal(@ModelAttribute(name = "satelliteForms") com.example.Sat2021.SatelliteForm satelliteForms, Model model) throws IOException {
        String s1 = satelliteForms.getSatellitechoice();
        String s2 = satelliteForms.getSatellitechoicetwo();


        if((!(s1.equals(""))) && (!(s2.equals("")))){
            String urlone = "https://celestrak.com/NORAD/elements/gp.php?CATNR=" + s1 + "&FORMAT=JSON";
            HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(urlone).openConnection();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(urlConnection.getInputStream());

            String urltwo = "https://celestrak.com/NORAD/elements/gp.php?CATNR=" + s2 + "&FORMAT=JSON";
            HttpsURLConnection urlConnectiontwo = (HttpsURLConnection) new URL(urltwo).openConnection();
            ObjectMapper objectMappertwo = new ObjectMapper();
            JsonNode rootNodetwo = objectMapper.readTree(urlConnectiontwo.getInputStream());

            String name = String.valueOf(rootNode.get(0).path("OBJECT_NAME"));
            String objid = String.valueOf(rootNode.get(0).path("OBJECT_ID"));
            String meanmot = String.valueOf(rootNode.get(0).path("MEAN_MOTION"));
            String eccen = String.valueOf(rootNode.get(0).path("ECCENTRICITY"));
            String inclin = String.valueOf(rootNode.get(0).path("INCLINATION"));
            String meananom = String.valueOf(rootNode.get(0).path("MEAN_ANOMALY"));
            String raan = String.valueOf(rootNode.get(0).path("RA_OF_ASC_NODE"));
            double meanmotion = Double.parseDouble(meanmot);
            double eccentricity = Double.parseDouble(eccen);
            double inclination = Double.parseDouble(inclin);
            double meananomaly = Double.parseDouble(meananom);
            double raans = Double.parseDouble(raan);
            String namet = String.valueOf(rootNodetwo.get(0).path("OBJECT_NAME"));
            String objidt = String.valueOf(rootNodetwo.get(0).path("OBJECT_ID"));
            String meanmott = String.valueOf(rootNodetwo.get(0).path("MEAN_MOTION"));
            String eccent = String.valueOf(rootNodetwo.get(0).path("ECCENTRICITY"));
            String inclint = String.valueOf(rootNodetwo.get(0).path("INCLINATION"));
            String meananomt = String.valueOf(rootNodetwo.get(0).path("MEAN_ANOMALY"));
            String raant = String.valueOf(rootNodetwo.get(0).path("RA_OF_ASC_NODE"));
            double meanmotiont = Double.parseDouble(meanmott);
            double eccentricityt = Double.parseDouble(eccent);
            double inclinationt = Double.parseDouble(inclint);
            double meananomalyt = Double.parseDouble(meananomt);
            double raanst = Double.parseDouble(raant);

            double semimajoraxisa = com.example.Sat2021.Satellites.findSemiMajorAxisA(meanmotion);
            double pvalue = com.example.Sat2021.Satellites.findPvalue(semimajoraxisa, eccentricity);

            double perigee = com.example.Sat2021.Satellites.findPdistEarthToPerigee(semimajoraxisa, eccentricity);
            double apogee = com.example.Sat2021.Satellites.findAdistEarthToApogee(semimajoraxisa, eccentricity);

            double apogeev = com.example.Sat2021.Satellites.findVatApogee(apogee, semimajoraxisa);
            double perigeev = com.example.Sat2021.Satellites.findVatPerigee(perigee, semimajoraxisa);

            double semimajoraxisat = com.example.Sat2021.Satellites.findSemiMajorAxisA(meanmotiont);
            double pvaluet = com.example.Sat2021.Satellites.findPvalue(semimajoraxisat, eccentricityt);



            double perigeet = com.example.Sat2021.Satellites.findPdistEarthToPerigee(semimajoraxisat, eccentricityt);
            double apogeet = com.example.Sat2021.Satellites.findAdistEarthToApogee(semimajoraxisat, eccentricityt);

            double apogeevt = com.example.Sat2021.Satellites.findVatApogee(apogeet, semimajoraxisat);
            double perigeevt = com.example.Sat2021.Satellites.findVatPerigee(perigeet, semimajoraxisat);

//find mean anomaly
            double meananomhourahead = (meananomaly/57.3);
            double meananomhouraheads = meananomhourahead * 57.3;
//find true anomaly
            double taonehourahead = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheads, eccentricity);
            double angle = taonehourahead + meananomhouraheads;
//find radius
            double radonehourahead = com.example.Sat2021.Satellites.findRadius(semimajoraxisa, eccentricity, angle);

            double x1 = com.example.Sat2021.Satellites.getx(radonehourahead, raans , inclination);
            double y1 = com.example.Sat2021.Satellites.gety(radonehourahead, raans, inclination);
            double z1 = com.example.Sat2021.Satellites.getz(radonehourahead, raans, inclination);


            DecimalFormat x = new DecimalFormat("###,###.###");

            //find mean anomaly
            double meananomhouraheadt = (meananomalyt/57.3);
            double meananomhouraheadst = meananomhouraheadt * 57.3;
//find true anomaly
            double taonehouraheadt = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheadst, eccentricityt);
            double anglet = taonehouraheadt + meananomhouraheadst;
//find radius
            double radonehouraheadt = com.example.Sat2021.Satellites.findRadius(semimajoraxisat, eccentricityt, anglet);

            double x1t = com.example.Sat2021.Satellites.getx(radonehouraheadt, raanst , inclinationt);
            double y1t = com.example.Sat2021.Satellites.gety(radonehouraheadt, raanst, inclinationt);
            double z1t = com.example.Sat2021.Satellites.getz(radonehouraheadt, raanst, inclinationt);

            double dist = com.example.Sat2021.Satellites.distanceSatellite(x1, x1t, y1, y1t, z1, z1t);
            model.addAttribute("disttwosats", x.format(dist));
            model.addAttribute("distone", "");
            return "distformrealpage";



        }
        else if((!(s1.equals(""))) && (s2.equals(""))){
            String urlone = "https://celestrak.com/NORAD/elements/gp.php?CATNR=" + s1 + "&FORMAT=JSON";
            HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(urlone).openConnection();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(urlConnection.getInputStream());

            ArrayList<com.example.Sat2021.sat> satss = new ArrayList<com.example.Sat2021.sat>();

            String name = String.valueOf(rootNode.get(0).path("OBJECT_NAME"));
            String objid = String.valueOf(rootNode.get(0).path("OBJECT_ID"));
            String meanmot = String.valueOf(rootNode.get(0).path("MEAN_MOTION"));
            String eccen = String.valueOf(rootNode.get(0).path("ECCENTRICITY"));
            String inclin = String.valueOf(rootNode.get(0).path("INCLINATION"));
            String noradid = String.valueOf(rootNode.get(0).path("NORAD_CAT_ID"));
            String raan = String.valueOf(rootNode.get(0).path("RA_OF_ASC_NODE"));
            String meananom = String.valueOf(rootNode.get(0).path("MEAN_ANOMALY"));
            String alt = String.valueOf(com.example.Sat2021.Satellites.getSatelliteAltitude(Double.parseDouble(meanmot)));

            satss.add(new com.example.Sat2021.sat(name, objid, eccen, inclin, noradid, meanmot,alt, raan, meananom));


            String url = "https://celestrak.com/NORAD/elements/gp.php?GROUP=Active&FORMAT=JSON";
            HttpsURLConnection urlConnectiont = (HttpsURLConnection) new URL(url).openConnection();
            ObjectMapper objectMappert = new ObjectMapper();
            JsonNode rootNodet = objectMappert.readTree(urlConnectiont.getInputStream());
            Iterator<JsonNode> albums = rootNodet.iterator();
            while(albums.hasNext()){
                JsonNode album = albums.next();
                String objnamet = String.valueOf(album.path("OBJECT_NAME"));
                String objidt = String.valueOf(album.path("OBJECT_ID"));
                String noradidt = String.valueOf(album.path("NORAD_CAT_ID"));
                String inclinationt = String.valueOf(album.path("INCLINATION"));
                String eccentricityt = String.valueOf(album.path("ECCENTRICITY"));
                String meanmott = String.valueOf(album.path("MEAN_MOTION"));
                String raascendingnodet = String.valueOf(album.path("RA_OF_ASC_NODE"));
                String meananomalyt = String.valueOf(album.path("MEAN_ANOMALY"));



                double meant = Double.parseDouble(meanmott);
                double altt = com.example.Sat2021.Satellites.findAdistEarthToApogee(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(meanmott)), Double.parseDouble(eccentricityt));
                String altit = String.valueOf(altt);

                if(!(noradidt.equals(noradid))) {
                    satss.add(new com.example.Sat2021.sat(objnamet, objidt, eccentricityt, inclinationt, noradidt, meanmott, altit, raascendingnodet, meananomalyt));
                }
            }

            ArrayList<com.example.Sat2021.DistFormSats> l = DistanceRealController.checkNow(satss);

            model.addAttribute("disttwosats", "");
            model.addAttribute("distone", l);
            return "distformrealpage";



        }



        return "distformrealpage";

    }

    public static String returnEquations(double a,  double b){
        String equation = "x^2/" + Math.pow(a, 2) + " + y^2/" + Math.pow(b, 2) + " = 1";
        return equation;
    }



    public static ArrayList<com.example.Sat2021.DistFormSats> checkNow(ArrayList<com.example.Sat2021.sat> leo){
        ArrayList<String> dists = new ArrayList<String>();
        ArrayList<com.example.Sat2021.sat> ltest = leo;
        ArrayList<com.example.Sat2021.DistFormSats> dsats = new ArrayList<com.example.Sat2021.DistFormSats>();

            //one hour from now:
            //find mean anomaly
        DecimalFormat x = new DecimalFormat("###,###.###");

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
                if (dist < 100) {
                    dists.add(String.valueOf(dist));
                    dists.add(ltest.get(0).objname);
                    dists.add(ltest.get(k).objname);
                    dsats.add(new com.example.Sat2021.DistFormSats(String.valueOf(x.format(dist)),ltest.get(0).objname, ltest.get(k).objname));
                }
            }
            ltest.remove(0);
        return dsats;
    }
}

