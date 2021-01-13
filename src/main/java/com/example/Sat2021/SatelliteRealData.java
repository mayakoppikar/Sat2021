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
public class SatelliteRealData {

    @RequestMapping(value = "/satellites", method = RequestMethod.GET)
    public String getSatellitePage(Model model) throws IOException {

        String url = "https://data.ivanstanojevic.me/api/tle";

          HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(urlConnection.getInputStream());
        System.out.println(rootNode);
        Iterator<JsonNode> albums = rootNode.path("").iterator();


        ArrayList<String> sats = new ArrayList<String>();


        while (albums.hasNext()){



            JsonNode album = albums.next();
//            System.out.println("Satellite ID: " + album.path("satelliteId") + " Name: " + album.path("name") + " Line 1: " + album.path("line1") + " Line 2: " + album.path("line2"));

            sats.add(String.valueOf(album.path("satelliteId")));
            sats.add(String.valueOf(album.path("name")));
            sats.add(String.valueOf(album.path("line1")));
            sats.add(String.valueOf(album.path("line2")));
        }

        int arraysize = sats.size();

        for (int i=0;i<arraysize;i++){
            model.addAttribute("hello" + (String.valueOf(i)), sats.get(i));
        }

        for(int i=0;i<arraysize;i+=4){
            model.addAttribute("id"+(String.valueOf(i)), sats.get(i));
        }




        return "satellite";
    }

    @RequestMapping(value = "/satellites", method = RequestMethod.POST)
    public String satellite(@ModelAttribute(name="satelliteForm") com.example.Sat2021.SatelliteForm satelliteForm, Model model) throws IOException {


        String satchoice = satelliteForm.getSatellitechoice();
//        System.out.println(satchoice);

        String url = "https://data.ivanstanojevic.me/api/tle";
        HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(urlConnection.getInputStream());

        Iterator<JsonNode> albums = rootNode.iterator();

        while(albums.hasNext()){
            System.out.println("MEWO");
        }

        ArrayList<String> sats = new ArrayList<String>();


        while (albums.hasNext()){

            JsonNode album = albums.next();
//            System.out.println("Satellite ID: " + album.path("satelliteId") + " Name: " + album.path("name") + " Line 1: " + album.path("line1") + " Line 2: " + album.path("line2"));

            sats.add(String.valueOf(album.path("satelliteId")));
            sats.add(String.valueOf(album.path("name")));
            sats.add(String.valueOf(album.path("line1")));
            sats.add(String.valueOf(album.path("line2")));
        }
//        System.out.println(sats);

        DecimalFormat x = new DecimalFormat("###,###.###");


        int arraysize = sats.size();

        for (int i=0;i<arraysize;i++){
            model.addAttribute("hello" + (String.valueOf(i)), sats.get(i));
        }

        for(int i=0;i<arraysize;i+=4){
            model.addAttribute("id"+(String.valueOf(i)), sats.get(i));
        }
//System.out.println(satchoice);

        model.addAttribute("satellitechoice", satchoice);


        int satchoicee = Integer.parseInt(satchoice);

        String s = (sats.get(satchoicee)) ;
        String s1 = (sats.get(satchoicee + 1)).substring(1,(sats.get(satchoicee+1)).length() - 1);
        String s2 = (sats.get(satchoicee + 2)).substring(1,(sats.get(satchoicee+2).length() - 1));
        String s3 = (sats.get(satchoicee + 3)).substring(1,(sats.get(satchoicee+3).length() - 1));

        String inclination = s3.substring(8,16);
        double inclin = Double.parseDouble(inclination);
        String eccentricity = s3.substring(28,34);
        double eccen = Double.parseDouble(eccentricity);
        double ecc = eccen * (Math.pow(10,-7));
        String meananomaly = s3.substring(43,51);
        double meananom = Double.parseDouble(meananomaly);
        String meanmotion = s3.substring((s3.length() - 17),(s3.length())-7);
        double meanmot = Double.parseDouble(meanmotion);
        String raan = s3.substring(27,33);
       double raans = Double.parseDouble(raan);


double semimajoraxisa = com.example.Sat2021.Satellites.findSemiMajorAxisA(meanmot);
double pvalue = com.example.Sat2021.Satellites.findPvalue(semimajoraxisa, ecc);

        if (ecc> 0 && ecc < 1) {
            double hvalue = com.example.Sat2021.Satellites.findHeightH(ecc, pvalue);
            double bvalue = com.example.Sat2021.Satellites.findBValSecnarioOne(ecc, pvalue);
            String satEquation = SatelliteRealData.returnEquations(semimajoraxisa, bvalue);
            model.addAttribute("b", x.format(bvalue));
            model.addAttribute("h", x.format(hvalue));
            model.addAttribute("equation", satEquation);
        } else if (ecc == 1) {
            double hvalue = (pvalue / 2);
            double bvalue = 0;
            String satEquation = SatelliteRealData.returnEquations(semimajoraxisa, bvalue);
            model.addAttribute("b", x.format(bvalue));
            model.addAttribute("h", x.format(hvalue));
            model.addAttribute("equation", satEquation);
        } else if (ecc > 1) {
            double hvalue = com.example.Sat2021.Satellites.findHeightH(ecc, pvalue);
            double bvalue = com.example.Sat2021.Satellites.findBValScenarioTwo(ecc, pvalue);
            String satEquation = SatelliteRealData.returnEquations(semimajoraxisa, bvalue);
            model.addAttribute("b", x.format(bvalue));
            model.addAttribute("h", x.format(hvalue));
            model.addAttribute("equation", satEquation);
        } else {
           double hvalue = 0;
           double bvalue = 0;
            String satEquation = SatelliteRealData.returnEquations(semimajoraxisa, bvalue);
           model.addAttribute("b", x.format(bvalue));
           model.addAttribute("h", hvalue);
            model.addAttribute("equation", satEquation);
        }

        double perigee = com.example.Sat2021.Satellites.findPdistEarthToPerigee(semimajoraxisa, ecc);
        double apogee = com.example.Sat2021.Satellites.findAdistEarthToApogee(semimajoraxisa, ecc);


double apogeev = com.example.Sat2021.Satellites.findVatApogee(apogee, semimajoraxisa);
double perigeev = com.example.Sat2021.Satellites.findVatPerigee(perigee, semimajoraxisa);






//one hour from now:
//find mean anomaly
double meananomhourahead = (meananom/57.3) +  3600;
double meananomhouraheads = meananomhourahead * 57.3;
//find true anomaly
 double taonehourahead = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheads, ecc);
 double angle = taonehourahead + meananomhouraheads;
//find radius
        double radonehourahead = com.example.Sat2021.Satellites.findRadius(semimajoraxisa, ecc, angle);

double x1 = com.example.Sat2021.Satellites.getx(radonehourahead, raans , inclin);
double y1 = com.example.Sat2021.Satellites.gety(radonehourahead, raans, inclin);
double z1 = com.example.Sat2021.Satellites.getz(radonehourahead, raans, inclin);

String coordinate1 = com.example.Sat2021.Satellites.getCoordinate(x1,y1, z1);
        model.addAttribute("onehouraheadcoordinates", coordinate1);
        model.addAttribute("radatonehourahead", x.format(radonehourahead));





//two hour from now:
//find mean anomaly
        double meananomtwohourahead = (meananom/57.3) +  7200;
        double meananomtwohouraheads = meananomtwohourahead * 57.3;
//find true anomaly
        double tatwohourahead = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomtwohouraheads, ecc);
        double angletwo = tatwohourahead + meananomtwohouraheads;
//find radius
        double radtwohourahead = com.example.Sat2021.Satellites.findRadius(semimajoraxisa, ecc, angletwo);


        double x2 = com.example.Sat2021.Satellites.getx(radtwohourahead, raans , inclin);
        double y2 = com.example.Sat2021.Satellites.gety(radtwohourahead, raans, inclin);
        double z2 = com.example.Sat2021.Satellites.getz(radtwohourahead, raans, inclin);

        String coordinate2 = com.example.Sat2021.Satellites.getCoordinate(x2, y2, z2);
        model.addAttribute("twohouraheadcoordinates", coordinate2);

        model.addAttribute("radtwohourahead", x.format(radtwohourahead));



        //five hour from now:
//find mean anomaly
        double meananomfivehourahead = (meananom/57.3) +  18000;
        double meananomfivehouraheads = meananomfivehourahead  * 57.3;
//find true anomaly
        double tafivehourahead = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomfivehouraheads, ecc);
        double anglethree = tafivehourahead + meananomfivehouraheads ;
//find radius
        double radfivehourahead = com.example.Sat2021.Satellites.findRadius(semimajoraxisa, ecc, anglethree);


        double x3 = com.example.Sat2021.Satellites.getx(radfivehourahead, raans , inclin);
        double y3 = com.example.Sat2021.Satellites.gety(radfivehourahead, raans, inclin);
        double z3 = com.example.Sat2021.Satellites.getz(radfivehourahead, raans, inclin);

        String coordinate3 = com.example.Sat2021.Satellites.getCoordinate(x3, y3, z3);
        model.addAttribute("fivehouraheadcoordinates", coordinate3);

        model.addAttribute("radfivehourahead", x.format(radfivehourahead));





        //five 24 from now:
//find mean anomaly
        double meananom24hourahead = (meananom/57.3) +  86400;
        double meananom24houraheads = meananom24hourahead  * 57.3;
//find true anomaly
        double ta24hourahead = com.example.Sat2021.Satellites.returnTrueAnomaly(meananom24houraheads, ecc);
        double anglefour = ta24hourahead  + meananom24houraheads;
//find radius
        double rad24hourahead = com.example.Sat2021.Satellites.findRadius(semimajoraxisa, ecc, anglefour);

        double x4 = com.example.Sat2021.Satellites.getx(rad24hourahead, raans , inclin);
        double y4 = com.example.Sat2021.Satellites.gety(rad24hourahead, raans, inclin);
        double z4 = com.example.Sat2021.Satellites.getz(rad24hourahead, raans, inclin);

        String coordinate4 = com.example.Sat2021.Satellites.getCoordinate(x4, y4, z4);
        model.addAttribute("twentyfourhouraheadcoordinates", coordinate4);


        model.addAttribute("rad24hourahead", x.format(rad24hourahead));






        //five 1 hour ago from now:
//find mean anomaly
        double meananom1hourago = (meananom/57.3) - 3600;
        double meananom1houragos = meananom1hourago  * 57.3;
//find true anomaly
        double ta1behind = com.example.Sat2021.Satellites.returnTrueAnomaly(meananom1houragos, ecc);
        double anglefive = ta1behind  + meananom1houragos;
//find radius
        double rad1hourbehind = com.example.Sat2021.Satellites.findRadius(semimajoraxisa, ecc, anglefive);



        double x5 = com.example.Sat2021.Satellites.getx(rad1hourbehind, raans , inclin);
        double y5 = com.example.Sat2021.Satellites.gety(rad1hourbehind, raans, inclin);
        double z5 = com.example.Sat2021.Satellites.getz(rad1hourbehind, raans, inclin);

        String coordinate5 = com.example.Sat2021.Satellites.getCoordinate(x5, y5, z5);
        model.addAttribute("onehourbehindcoordinates", coordinate5);


        model.addAttribute("rad1hourbehind", x.format(rad1hourbehind));


     double apogeesplit = apogee/10;
     for(int i = 0; i < 10; i ++){
      model.addAttribute(("apog") + i, com.example.Sat2021.Satellites.findSatVelocityForGivenR((i * apogeesplit), semimajoraxisa));
     }



    model.addAttribute("satone", s);
    model.addAttribute("sattwo", s1);
    model.addAttribute("satthree", s2);
    model.addAttribute("satfour", s3);
    model.addAttribute("inclination", x.format(inclin));
    model.addAttribute("eccentricity", x.format(ecc));
   model.addAttribute("meananomaly", x.format(meananom));
   model.addAttribute("meanmotion", x.format(meanmot));

   model.addAttribute("a", x.format(semimajoraxisa));
   model.addAttribute("p", x.format(pvalue));

   model.addAttribute("disttoapogee", x.format(apogee));
   model.addAttribute("disttoperigee", x.format(perigee));

        model.addAttribute("vatapogee", x.format(apogeev));
        model.addAttribute("vatperigee", x.format(perigeev));

        return "satellitepage";
    }

    public static String returnEquations(double a,  double b){
        String equation = "x^2/" + Math.pow(a, 2) + " + y^2/" + Math.pow(b, 2) + " = 1";
        return equation;
    }

}

