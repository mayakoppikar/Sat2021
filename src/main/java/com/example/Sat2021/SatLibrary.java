package com.example.Sat2021;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

@Controller
public class SatLibrary{

    @RequestMapping(value = "/satlibrary", method = RequestMethod.GET)
    public String getSatLibrary(@ModelAttribute(name="filterForm") com.example.Sat2021.FilterForm filterForm, Model model) throws IOException {

        DecimalFormat x = new DecimalFormat("###,###.###");
        boolean alphe = filterForm.isAlphabeticorder();
        String altituderange = filterForm.getAltitudezone();






    String url = "https://celestrak.com/NORAD/elements/gp.php?GROUP=Active&FORMAT=JSON";
    HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode rootNode = objectMapper.readTree(urlConnection.getInputStream());
    Iterator<JsonNode> albums = rootNode.iterator();
    ArrayList<com.example.Sat2021.sat> sats = new ArrayList<com.example.Sat2021.sat>();
    while(albums.hasNext()){
        JsonNode album = albums.next();
        String objname = String.valueOf(album.path("OBJECT_NAME"));
        String objid = String.valueOf(album.path("OBJECT_ID"));
        String noradid = String.valueOf(album.path("NORAD_CAT_ID"));
        String inclination = String.valueOf(album.path("INCLINATION"));
        String eccentricity = String.valueOf(album.path("ECCENTRICITY"));
        String meanmot = String.valueOf(album.path("MEAN_MOTION"));
        String raascendingnode = String.valueOf(album.path("RA_OF_ASC_NODE"));
        String meananomaly = String.valueOf(album.path("MEAN_ANOMALY"));



        double mean = Double.parseDouble(meanmot);
double alt = com.example.Sat2021.Satellites.findAdistEarthToApogee(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(meanmot)), Double.parseDouble(eccentricity));
String alti = String.valueOf(alt);

        sats.add(new com.example.Sat2021.sat(objname, objid, eccentricity, inclination, noradid, meanmot, alti, raascendingnode, meananomaly));

    }
ArrayList<com.example.Sat2021.sat> filteredsats = new ArrayList<com.example.Sat2021.sat>();

       if(Objects.equals(altituderange, "LEO")){

           for(int i=0; i < sats.size(); i++){
               if(Double.parseDouble(sats.get(i).altitude) < 2000){
filteredsats.add(new com.example.Sat2021.sat(sats.get(i).objname, sats.get(i).objid, sats.get(i).eccentricity, sats.get(i).inclin, sats.get(i).noradid, sats.get(i).meanmot, sats.get(i).altitude,sats.get(i).raascendingnode, sats.get(i).meananomaly));

                   model.addAttribute("sats", filteredsats);

               }
           }
       }
       else if(Objects.equals(altituderange, "MEO")){
           for(int i=0; i < sats.size(); i++){
                if((Double.parseDouble(sats.get(i).altitude) >= 2000) && (Double.parseDouble(sats.get(i).altitude) < 35780))
                    filteredsats.add(new com.example.Sat2021.sat(sats.get(i).objname, sats.get(i).objid, sats.get(i).eccentricity, sats.get(i).inclin, sats.get(i).noradid, sats.get(i).meanmot, sats.get(i).altitude, sats.get(i).raascendingnode, sats.get(i).meananomaly));
               model.addAttribute("sats", filteredsats);

           }
       }
       else if(Objects.equals(altituderange, "GEO")){
           for(int i=0; i < sats.size(); i++){
                if(Double.parseDouble(sats.get(i).altitude) >= 35780){
                    filteredsats.add(new com.example.Sat2021.sat(sats.get(i).objname, sats.get(i).objid, sats.get(i).eccentricity, sats.get(i).inclin, sats.get(i).noradid, sats.get(i).meanmot, sats.get(i).altitude, sats.get(i).raascendingnode, sats.get(i).meananomaly));
                    model.addAttribute("sats", filteredsats);

                }
           }
       }
       else{
           if(alphe){
               ArrayList<com.example.Sat2021.sat> xalph = sortAplhabetically();
               model.addAttribute("sats", xalph);

           }
           else{
               model.addAttribute("sats", sats);

           }
       }

    return "satlib.html";
    }

    @GetMapping("/satlib/{satid}")
    @ResponseBody
    public String getSatelliteIdInfo(@PathVariable("satid") String satid) throws IOException {
        String url = "https://celestrak.com/NORAD/elements/gp.php?CATNR=" + satid + "&FORMAT=JSON";

        HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(urlConnection.getInputStream());
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

        double semimajoraxisa = com.example.Sat2021.Satellites.findSemiMajorAxisA(meanmotion);
        double pvalue = com.example.Sat2021.Satellites.findPvalue(semimajoraxisa, eccentricity);

        if (eccentricity> 0 && eccentricity < 1) {
            double hvalue = com.example.Sat2021.Satellites.findHeightH(eccentricity, pvalue);
            double bvalue = com.example.Sat2021.Satellites.findBValSecnarioOne(eccentricity, pvalue);
            String satEquation = com.example.Sat2021.SatelliteRealData.returnEquations(semimajoraxisa, bvalue);
        } else if (eccentricity == 1) {
            double hvalue = (pvalue / 2);
            double bvalue = 0;
            String satEquation = com.example.Sat2021.SatelliteRealData.returnEquations(semimajoraxisa, bvalue);
        } else if (eccentricity > 1) {
            double hvalue = com.example.Sat2021.Satellites.findHeightH(eccentricity, pvalue);
            double bvalue = com.example.Sat2021.Satellites.findBValScenarioTwo(eccentricity, pvalue);
            String satEquation = com.example.Sat2021.SatelliteRealData.returnEquations(semimajoraxisa, bvalue);
        } else {
            double hvalue = 0;
            double bvalue = 0;
            String satEquation = com.example.Sat2021.SatelliteRealData.returnEquations(semimajoraxisa, bvalue);
        }

        double perigee = com.example.Sat2021.Satellites.findPdistEarthToPerigee(semimajoraxisa, eccentricity);
        double apogee = com.example.Sat2021.Satellites.findAdistEarthToApogee(semimajoraxisa, eccentricity);

        double apogeev = com.example.Sat2021.Satellites.findVatApogee(apogee, semimajoraxisa);
        double perigeev = com.example.Sat2021.Satellites.findVatPerigee(perigee, semimajoraxisa);
        double orbper = com.example.Sat2021.Satellites.findOrbitalPeriod(meanmotion);


//one hour from now:
//find mean anomaly
        double mnow = (meananomaly/57.3);
        double mnows = mnow * 57.3;
//find true anomaly
        double tanow = com.example.Sat2021.Satellites.returnTrueAnomaly(mnows, eccentricity);
        double anglenow = tanow + mnows;
//find radius
        double radnow = com.example.Sat2021.Satellites.findRadius(semimajoraxisa, eccentricity, anglenow);

        double x1now = com.example.Sat2021.Satellites.getx(radnow, raans , inclination);
        double y1now = com.example.Sat2021.Satellites.gety(radnow, raans, inclination);
        double z1now = com.example.Sat2021.Satellites.getz(radnow, raans, inclination);

        String coordinatenow = com.example.Sat2021.Satellites.getCoordinate(x1now,y1now, z1now);


        System.out.println(coordinatenow);
        System.out.println(radnow);

        //one hour from now:
//find mean anomaly
        double mper = (meananomaly/57.3) + 6290.4;
        double mpers = mper * 57.3;
//find true anomaly
        double taper = com.example.Sat2021.Satellites.returnTrueAnomaly(mpers, eccentricity);
        double angleper = taper + mpers;
//find radius
        double radper = com.example.Sat2021.Satellites.findRadius(semimajoraxisa, eccentricity, angleper);

        double x1per = com.example.Sat2021.Satellites.getx(radper, raans , inclination);
        double y1per = com.example.Sat2021.Satellites.gety(radper, raans, inclination);
        double z1per = com.example.Sat2021.Satellites.getz(radper, raans, inclination);

        String coordinateper = com.example.Sat2021.Satellites.getCoordinate(x1per,y1per, z1per);



        System.out.println(coordinateper);
        System.out.println(radper);

//one hour from now:
//find mean anomaly
        double meananomhourahead = (meananomaly/57.3) +  3600;
        double meananomhouraheads = meananomhourahead * 57.3;
//find true anomaly
        double taonehourahead = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheads, eccentricity);
        double angle = taonehourahead + meananomhouraheads;
//find radius
        double radonehourahead = com.example.Sat2021.Satellites.findRadius(semimajoraxisa, eccentricity, angle);

        double x1 = com.example.Sat2021.Satellites.getx(radonehourahead, raans , inclination);
        double y1 = com.example.Sat2021.Satellites.gety(radonehourahead, raans, inclination);
        double z1 = com.example.Sat2021.Satellites.getz(radonehourahead, raans, inclination);

        String coordinate1 = com.example.Sat2021.Satellites.getCoordinate(x1,y1, z1);

double hvalue = SatLibrary.getHval(eccentricity, semimajoraxisa, pvalue);

//two hour from now:
//find mean anomaly
        double meananomtwohourahead = (meananomaly/57.3) +  7200;
        double meananomtwohouraheads = meananomtwohourahead * 57.3;
//find true anomaly
        double tatwohourahead = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomtwohouraheads, eccentricity);
        double angletwo = tatwohourahead + meananomtwohouraheads;
//find radius
        double radtwohourahead = com.example.Sat2021.Satellites.findRadius(semimajoraxisa, eccentricity, angletwo);


        double x2 = com.example.Sat2021.Satellites.getx(radtwohourahead, raans , inclination);
        double y2 = com.example.Sat2021.Satellites.gety(radtwohourahead, raans, inclination);
        double z2 = com.example.Sat2021.Satellites.getz(radtwohourahead, raans, inclination);

        String coordinate2 = com.example.Sat2021.Satellites.getCoordinate(x2, y2, z2);

        //five hour from now:
//find mean anomaly
        double meananomfivehourahead = (meananomaly/57.3) +  18000;
        double meananomfivehouraheads = meananomfivehourahead  * 57.3;
//find true anomaly
        double tafivehourahead = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomfivehouraheads, eccentricity);
        double anglethree = tafivehourahead + meananomfivehouraheads ;
//find radius
        double radfivehourahead = com.example.Sat2021.Satellites.findRadius(semimajoraxisa, eccentricity, anglethree);

        double x3 = com.example.Sat2021.Satellites.getx(radfivehourahead, raans , inclination);
        double y3 = com.example.Sat2021.Satellites.gety(radfivehourahead, raans, inclination);
        double z3 = com.example.Sat2021.Satellites.getz(radfivehourahead, raans, inclination);

        String coordinate3 = com.example.Sat2021.Satellites.getCoordinate(x3, y3, z3);

        //five 24 from now:
//find mean anomaly
        double meananom24hourahead = (meananomaly/57.3) +  86400;
        double meananom24houraheads = meananom24hourahead  * 57.3;
//find true anomaly
        double ta24hourahead = com.example.Sat2021.Satellites.returnTrueAnomaly(meananom24houraheads, eccentricity);
        double anglefour = ta24hourahead  + meananom24houraheads;
//find radius
        double rad24hourahead = com.example.Sat2021.Satellites.findRadius(semimajoraxisa, eccentricity, anglefour);

        double x4 = com.example.Sat2021.Satellites.getx(rad24hourahead, raans , inclination);
        double y4 = com.example.Sat2021.Satellites.gety(rad24hourahead, raans, inclination);
        double z4 = com.example.Sat2021.Satellites.getz(rad24hourahead, raans, inclination);

        String coordinate4 = com.example.Sat2021.Satellites.getCoordinate(x4, y4, z4);

        //five 1 hour ago from now:
//find mean anomaly
        double meananom1hourago = (meananomaly/57.3) - 3600;
        double meananom1houragos = meananom1hourago  * 57.3;
//find true anomaly
        double ta1behind = com.example.Sat2021.Satellites.returnTrueAnomaly(meananom1houragos, eccentricity);
        double anglefive = ta1behind  + meananom1houragos;
//find radius
        double rad1hourbehind = com.example.Sat2021.Satellites.findRadius(semimajoraxisa, eccentricity, anglefive);

        double x5 = com.example.Sat2021.Satellites.getx(rad1hourbehind, raans , inclination);
        double y5 = com.example.Sat2021.Satellites.gety(rad1hourbehind, raans, inclination);
        double z5 = com.example.Sat2021.Satellites.getz(rad1hourbehind, raans, inclination);

        String coordinate5 = com.example.Sat2021.Satellites.getCoordinate(x5, y5, z5);



        DecimalFormat x = new DecimalFormat("###,###.##");


        return "  <!DOCTYPE html>\n" +
                "<html xmlns:th=\"http://www.thymeleaf.org\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/custom.css\"/>\n" +
                "<link rel=\"shortcut icon\" type=\"image/png\" href=\"https://clipart.info/images/ccovers/1559839401black-star-png-10.png\">\n" +
                "\n" +
                "<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">\n" +
                "<title>Satellites | Satellite</title>\n" +
                "</head>\n" +
                "<body id=\"backy\">\n" +
                "\n" +
                "\n" +
                "<nav class=\"navbar navbar-expand-lg navbar-dark bg-info\">\n" +
                "<a class=\"navbar-brand\" href=\"/\"><img src=\"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRL0GHfo_JPhu5qbZbhq9jHtjtWSrOtH5lHjw&usqp=CAU\" height=\"30px\" width=\"30px\"></a>\n" +
                "<a class=\"navbar-brand\" href=\"/mainpage.html\">Satellites!</a>\n" +
                "\n" +
                "<div>\n" +
                "<button class=\"buttonsnav\">\n" +
                "<a href=\"/login\">Image Of The Day</a>\n" +
                "</button>\n" +
                "</div>\n" +
                "\n" +
                "<div>\n" +
                "<button class=\"buttonsnav\">\n" +
                "<a href=\"/satlibrary\">Satellites </a>\n" +
                "</button>\n" +
                "</div>\n" +
                "\n" +
                "<div>\n" +
                "<button class=\"buttonsnav\">\n" +
                "<a href=\"/tracking\">Tracking</a>\n" +
                "</button>\n" +
                "</div>\n" +
                "\n" +
                "<div>\n" +
                "<button class=\"buttonsnav\">\n" +
                "<a href=\"/background.html\">Background Info</a>\n" +
                "</button>\n" +
                "</div>\n" +
                "\n" +
                "<div>\n" +
                "<button class=\"buttonsnav\">\n" +
                "<a href=\"/calculations.html\">Calculations</a>\n" +
                "</button>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "</nav>\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "<div class=\"container\">\n" +
                "<h4 ID=\"inf\">Information: </h4>\n" +
                "<div class=\"row\">\n" +
                "\n" +
                "\n" +
                "\n" +
                "<div class=\"col-lg-4 col-md-6 col-sm-6 \">\n" +
                "\n" +
                "\n" +
                "\n" +
                "<p class=\"\"><strong>Inclination: " + x.format(inclination) + "  °</p>\n" +
                "<p class=\"\"> <strong>Eccentricity: </strong>" + eccentricity + " </p>\n" +
                "<p class=\"\"><strong>Mean Motion: </strong>" + x.format(meanmotion) + " rev/day</p>\n" +
                "<p class=\"\"><strong>Mean Anomaly: </strong>" + x.format(meananomaly) + "  °</p>\n" +
                "\n" +
                "\n" +
                "</div></div>\n" +
                "\n" +
                "\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"container\">\n" +
                "<div>\n" +
                "<h4>Predicting Satellite Location</h4>\n" +
                "</div>\n" +
                "<table class=\"bo\" style=\"width:100%\">\n" +
                "<tr class=\"bo\">\n" +
                "<th class=\"bo\">Time</th>\n" +
                "<th class=\"bo\">Location</th>\n" +
                "<th class=\"bo\">Ecliptic Coordinates [x,y,z]</th>\n" +
                "\n" +
                "</tr>\n" +
                "<tr class=\"bo\">\n" +
                "</tr>\n" +
                "<tr class=\"bo\">\n" +
                "<td class=\"bo\">1 hr. <span class=\"redcoloragolabel\">ago</span></td>\n" +
                "<td class=\"bo\"><p>" + x.format(rad1hourbehind) + " km</p></td>\n" +
                "<td class=\"bo\"><p>" + coordinate5 + "</p></td>\n" +
                "\n" +
                "</tr>\n" +
                "<td class=\"bo\">Now</td>\n" +
                "<td class=\"bo\"><p>" + x.format(radnow) + " km</p></td>\n" +
                "<td class=\"bo\"><p>" +  coordinatenow + "</p></td>\n" +
                "\n" +
                "\n" +
                "</tr>\n" +
                "</tr>\n" +
                "<td class=\"bo\">1 hr. <span class=\"greencoloragolabel\">from now</span></td>\n" +
                "<td class=\"bo\"><p>" + x.format(radonehourahead) + " km</p></td>\n" +
                "<td class=\"bo\"><p>" +  coordinate1 + "</p></td>\n" +
                "\n" +
                "\n" +
                "</tr>\n" +

                "<tr class=\"bo\">\n" +
                "<td class=\"bo\">2 hr. <span class=\"greencoloragolabel\">from now</span></td>\n" +
                "<td class=\"bo\"><p>" + x.format(radtwohourahead) + " km</p></td>\n" +
                "<td class=\"bo\"><p>" + coordinate2 + "</p></td>\n" +
                "\n" +
                "\n" +
                "</tr>\n" +
                "<tr class=\"bo\">\n" +
                "<td class=\"bo\">5 hr. <span class=\"greencoloragolabel\">from now</span></td>\n" +
                "<td class=\"bo\"><p>" + x.format(radfivehourahead) + " km</p></td>\n" +
                "<td class=\"bo\"><p>" + coordinate3 + "</p></td>\n" +
                "\n" +
                "</tr>\n" +
                "<tr class=\"bo\">\n" +
                "<td class=\"bo\">24 hr. <span class=\"greencoloragolabel\">from now</span></td>\n" +
                "<td class=\"bo\"><p>" + x.format(rad24hourahead) + " km</p></td>\n" +
                "<td class=\"bo\"><p>" + coordinate4 + "</p></td>\n" +
                "\n" +
                "\n" +
                "\n" +
                "</table>\n" +
                "\n" +
                "</div><div class=\"container\">\n" +
                "<h4 id=\"soi\">Satellite Orbit Information: </h4>\n" +
                "<div class=\"row\">\n" +
                "\n" +
                "<div class=\"col-lg-4 col-md-6 col-sm-6 \"><p class=\"\"><strong>Semi-Major Axis(a) Length: </strong>" +  x.format(semimajoraxisa) + " km</p>\n" +
                "<p class=\"\"><strong>Orbital Period: " + x.format(orbper) +  " mins</p>\n" +
                "<p class=\"\"><strong>Distance from Earth to Apogee: </strong>" + x.format(apogee) + " km</p>\n" +
                "<p class=\"\"><strong>Distance from Earth to Perigee: </strong>" + x.format(perigee) + " km</p>\n <p>---------------------------------------------</p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div><div class=\"container\">\n" +
                "<h4 id=\"satmot\">Satellite Motion: </h4>\n" +
                "<div class=\"row\">\n" +
                "\n" +
                "<div class=\"col-lg-4 col-md-6 col-sm-6 \"><p class=\"\"><strong>Velocity at Apogee: </strong>" + x.format(apogeev) + "  m/s</p>\n" +
                "<p class=\"\"><strong>Velocity at Perigee: </strong>" + x.format(perigeev) + " m/s</p>\n</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"container\">\n" +
                "<a href=\"/satlibrary\">Find Another Satellite!</a>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
    }

public static double getHval(double eccentricity, double semimajoraxisa, double pvalue){
    if (eccentricity> 0 && eccentricity < 1) {
        double hvalue = com.example.Sat2021.Satellites.findHeightH(eccentricity, pvalue);
        double bvalue = com.example.Sat2021.Satellites.findBValSecnarioOne(eccentricity, pvalue);
        String satEquation = com.example.Sat2021.SatelliteRealData.returnEquations(semimajoraxisa, bvalue);
        return hvalue;
    } else if (eccentricity == 1) {
        double hvalue = (pvalue / 2);
        double bvalue = 0;
        String satEquation = com.example.Sat2021.SatelliteRealData.returnEquations(semimajoraxisa, bvalue);
        return hvalue;

    } else if (eccentricity > 1) {
        double hvalue = com.example.Sat2021.Satellites.findHeightH(eccentricity, pvalue);
        double bvalue = com.example.Sat2021.Satellites.findBValScenarioTwo(eccentricity, pvalue);
        String satEquation = com.example.Sat2021.SatelliteRealData.returnEquations(semimajoraxisa, bvalue);
        return hvalue;

    } else {
        double hvalue = 0;
        double bvalue = 0;
        String satEquation = com.example.Sat2021.SatelliteRealData.returnEquations(semimajoraxisa, bvalue);
        return hvalue;

    }
}

    public static ArrayList<com.example.Sat2021.sat> sortAplhabetically() throws IOException {
        String url = "https://celestrak.com/NORAD/elements/gp.php?GROUP=Active&FORMAT=JSON";
        HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(urlConnection.getInputStream());

        Iterator<JsonNode> albums = rootNode.iterator();
        ArrayList<String> sats = new ArrayList<String>();
        ArrayList<com.example.Sat2021.sat> sat = new ArrayList<com.example.Sat2021.sat>();

        while(albums.hasNext()){
            JsonNode album = albums.next();
            String objname = String.valueOf(album.path("OBJECT_NAME"));
            String objid = String.valueOf(album.path("OBJECT_ID"));
            String eccentricity = String.valueOf(album.path("ECCENTRICITY"));
            String inclination = String.valueOf(album.path("INCLINATION"));
            String noradid = String.valueOf(album.path("NORAD_CAT_ID"));
            String meanmotion = String.valueOf(album.path("MEAN_MOTION"));
            double alt = com.example.Sat2021.Satellites.findAdistEarthToApogee(com.example.Sat2021.Satellites.findSemiMajorAxisA(Double.parseDouble(meanmotion)), Double.parseDouble(eccentricity));
            String raan = String.valueOf(album.path("RA_OF_ASC_NODE"));
            String meananomaly = String.valueOf(album.path("MEAN_ANOMALY"));



            sats.add(objname);
            sat.add(new com.example.Sat2021.sat(objname, objid, eccentricity, inclination, noradid, meanmotion, String.valueOf(alt), raan, meananomaly));

        }

String temp;

        for(int i=0;i<sats.size();i++){

            for(int j=i+1;j<sats.size();j++){

                if(sats.get(i).compareTo(sats.get(j))<0){

                    temp=sats.get(i);
                    sats.set(i, sats.get(j));
                    sats.set(j,temp );

                }
            }
        }

       Collections.reverse(sats);

        ArrayList<com.example.Sat2021.sat> finsatlist = new ArrayList<com.example.Sat2021.sat>();
        for(int x=0; x< sats.size(); x++){
           String snam = sats.get(x);
           for(int i=0; i < sats.size(); i ++){
               if(sat.get(i).objname == snam){
                   finsatlist.add(sat.get(i));
               }
           }
        }

        return finsatlist;

    }



}