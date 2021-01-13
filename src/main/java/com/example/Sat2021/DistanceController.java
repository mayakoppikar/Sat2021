package com.example.Sat2021;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.text.DecimalFormat;

@Controller
public class DistanceController {

    @RequestMapping(value = "/distanceform", method = RequestMethod.GET)
    public String getDistanceForm(){
        return "distform";
    }

    @RequestMapping(value = "/distanceform", method = RequestMethod.POST)
    public String distance(@ModelAttribute(name="distanceForm") com.example.Sat2021.DistanceForm distanceForm, Model models) throws IOException {


DecimalFormat m = new DecimalFormat("###,###.###");

String satName = distanceForm.getSatname();
String inclin = distanceForm.getInclination();
String eccen = distanceForm.getEccentricity();
String meanMot = distanceForm.getMeanmotion();
String meanAnom = distanceForm.getMeananomaly();


        String satNametwo = distanceForm.getSatnametwo();
        String inclintwo = distanceForm.getInclinationtwo();
        String eccentwo = distanceForm.getEccentricitytwo();
        String meanMottwo = distanceForm.getMeanmotiontwo();
        String meanAnomtwo = distanceForm.getMeananomalytwo();


String raan = distanceForm.getRaan();
String raantwo = distanceForm.getRaantwo();
double raans = Double.parseDouble(raan);
double raantwos = Double.parseDouble(raantwo);


        com.example.Sat2021.Satellites satelliteOne = new com.example.Sat2021.Satellites(0,satName, 0,0,0,0,0,Double.parseDouble(inclin), 0, Double.parseDouble(eccen), 0, Double.parseDouble(meanAnom), Double.parseDouble(meanMot), 0);
        com.example.Sat2021.Satellites satelliteTwo = new com.example.Sat2021.Satellites(0,satNametwo, 0,0,0,0,0,Double.parseDouble(inclintwo), 0, Double.parseDouble(eccentwo), 0, Double.parseDouble(meanAnomtwo), Double.parseDouble(meanMottwo), 0);




        double satOneMean = satelliteOne.findSemiMajorAxisA(Double.parseDouble(meanMot));
double satTwoMean = satelliteTwo.findSemiMajorAxisA(Double.parseDouble(meanMottwo));



        double pValSatOne = satelliteOne.findPvalue(satOneMean, Double.parseDouble(eccen));
        double pValSatTwo = satelliteTwo.findPvalue(satTwoMean, Double.parseDouble(eccentwo));
//
//
double hOne = DistanceController.satDetailsHeight(Double.parseDouble(eccen), pValSatOne);

double hTwo =  DistanceController.satDetailsHeight(Double.parseDouble(eccentwo), pValSatTwo);


double bOne = DistanceController.satB(Double.parseDouble(eccen), pValSatOne);
double bTwo = DistanceController.satB(Double.parseDouble(eccentwo), pValSatTwo);



double AdistEarthToApogeeOne = satelliteOne.findAdistEarthToApogee(satOneMean, Double.parseDouble(eccen));
double AdistEarthToApogeeTwo = satelliteTwo.findAdistEarthToApogee(satTwoMean, Double.parseDouble(eccentwo));

double PdistEarthToPerigeeOne = satelliteOne.findPdistEarthToPerigee(satOneMean, Double.parseDouble(eccen));
double PdistEarthToPerigeeTwo = satelliteTwo.findPdistEarthToPerigee(satTwoMean, Double.parseDouble(eccentwo));


String satOneEquation = DistanceController.returnEquation(satOneMean, bOne);
String satTwoEquation = DistanceController.returnEquation(satTwoMean, bTwo);

double voneatapogee = com.example.Sat2021.Satellites.findVatApogee(AdistEarthToApogeeOne, satOneMean);
double vtwoatapogee = com.example.Sat2021.Satellites.findVatApogee(AdistEarthToApogeeTwo, satTwoMean);


double voneatperigee = com.example.Sat2021.Satellites.findVatPerigee(PdistEarthToPerigeeOne,satOneMean);
double vtwoatperigee= com.example.Sat2021.Satellites.findVatPerigee(PdistEarthToPerigeeTwo,satTwoMean);


double onevatrad50plus = com.example.Sat2021.Satellites.findSatVelocityForGivenR((PdistEarthToPerigeeOne + 50.0), satOneMean);
double twovatrad50plus = com.example.Sat2021.Satellites.findSatVelocityForGivenR((PdistEarthToPerigeeTwo + 50.0), satTwoMean);













//satellite one

double meanAnomsone = Double.parseDouble(meanAnom);

//one hour from now:
//find mean anomaly
        double meananomhourahead = (meanAnomsone/57.3) +  3600;
        double meananomhouraheads = meananomhourahead * 57.3;
//find true anomaly
        double taonehourahead = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheads, Double.parseDouble(eccen));
        double angle = taonehourahead + meananomhouraheads;
//find radius
        double radonehourahead = com.example.Sat2021.Satellites.findRadius(satOneMean, Double.parseDouble(eccen), angle);

        double x1 = com.example.Sat2021.Satellites.getx(radonehourahead, raans , Double.parseDouble(inclin));
        double y1 = com.example.Sat2021.Satellites.gety(radonehourahead, raans, Double.parseDouble(inclin));
        double z1 = com.example.Sat2021.Satellites.getz(radonehourahead, raans, Double.parseDouble(inclin));

        String coordinate1 = com.example.Sat2021.Satellites.getCoordinate(x1,y1, z1);
        models.addAttribute("onehouraheadcoordinates", coordinate1);
        models.addAttribute("radatonehourahead", m.format(radonehourahead));





//two hour from now:
//find mean anomaly
        double meananomtwohourahead = (meanAnomsone/57.3) +  7200;
        double meananomtwohouraheads = meananomtwohourahead * 57.3;
//find true anomaly
        double tatwohourahead = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomtwohouraheads, Double.parseDouble(eccen));
        double angletwo = tatwohourahead + meananomtwohouraheads;
//find radius
        double radtwohourahead = com.example.Sat2021.Satellites.findRadius(satOneMean, Double.parseDouble(eccen), angletwo);


        double x2 = com.example.Sat2021.Satellites.getx(radtwohourahead, raans , Double.parseDouble(inclin));
        double y2 = com.example.Sat2021.Satellites.gety(radtwohourahead, raans, Double.parseDouble(inclin));
        double z2 = com.example.Sat2021.Satellites.getz(radtwohourahead, raans, Double.parseDouble(inclin));

        String coordinate2 = com.example.Sat2021.Satellites.getCoordinate(x2, y2, z2);
        models.addAttribute("twohouraheadcoordinates", coordinate2);

        models.addAttribute("radtwohourahead", m.format(radtwohourahead));



        //five hour from now:
//find mean anomaly
        double meananomfivehourahead = (meanAnomsone/57.3) +  18000;
        double meananomfivehouraheads = meananomfivehourahead  * 57.3;
//find true anomaly
        double tafivehourahead = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomfivehouraheads, Double.parseDouble(eccen));
        double anglethree = tafivehourahead + meananomfivehouraheads ;
//find radius
        double radfivehourahead = com.example.Sat2021.Satellites.findRadius(satOneMean, Double.parseDouble(eccen), anglethree);


        double x3 = com.example.Sat2021.Satellites.getx(radfivehourahead, raans , Double.parseDouble(inclin));
        double y3 = com.example.Sat2021.Satellites.gety(radfivehourahead, raans, Double.parseDouble(inclin));
        double z3 = com.example.Sat2021.Satellites.getz(radfivehourahead, raans, Double.parseDouble(inclin));

        String coordinate3 = com.example.Sat2021.Satellites.getCoordinate(x3, y3, z3);
        models.addAttribute("fivehouraheadcoordinates", coordinate3);

        models.addAttribute("radfivehourahead", m.format(radfivehourahead));





        //five 24 from now:
//find mean anomaly
        double meananom24hourahead = (meanAnomsone/57.3) +  86400;
        double meananom24houraheads = meananom24hourahead  * 57.3;
//find true anomaly
        double ta24hourahead = com.example.Sat2021.Satellites.returnTrueAnomaly(meananom24houraheads, Double.parseDouble(eccen));
        double anglefour = ta24hourahead  + meananom24houraheads;
//find radius
        double rad24hourahead = com.example.Sat2021.Satellites.findRadius(satOneMean, Double.parseDouble(eccen), anglefour);

        double x4 = com.example.Sat2021.Satellites.getx(rad24hourahead, raans , Double.parseDouble(inclin));
        double y4 = com.example.Sat2021.Satellites.gety(rad24hourahead, raans, Double.parseDouble(inclin));
        double z4 = com.example.Sat2021.Satellites.getz(rad24hourahead, raans, Double.parseDouble(inclin));

        String coordinate4 = com.example.Sat2021.Satellites.getCoordinate(x4, y4, z4);
        models.addAttribute("twentyfourhouraheadcoordinates", coordinate4);


        models.addAttribute("rad24hourahead", m.format(rad24hourahead));






        //five 1 hour ago from now:
//find mean anomaly
        double meananom1hourago = (meanAnomsone/57.3) - 3600;
        double meananom1houragos = meananom1hourago  * 57.3;
//find true anomaly
        double ta1behind = com.example.Sat2021.Satellites.returnTrueAnomaly(meananom1houragos, Double.parseDouble(eccen));
        double anglefive = ta1behind  + meananom1houragos;
//find radius
        double rad1hourbehind = com.example.Sat2021.Satellites.findRadius(satOneMean, Double.parseDouble(eccen), anglefive);



        double x5 = com.example.Sat2021.Satellites.getx(rad1hourbehind, raans , Double.parseDouble(inclin));
        double y5 = com.example.Sat2021.Satellites.gety(rad1hourbehind, raans, Double.parseDouble(inclin));
        double z5 = com.example.Sat2021.Satellites.getz(rad1hourbehind, raans, Double.parseDouble(inclin));

        String coordinate5 = com.example.Sat2021.Satellites.getCoordinate(x5, y5, z5);
        models.addAttribute("onehourbehindcoordinates", coordinate5);


        models.addAttribute("rad1hourbehind", m.format(rad1hourbehind));



//satellite two predictions
double meanAnomstwo = Double.parseDouble(meanAnomtwo);

//one hour from now:
//find mean anomaly
        double meananomhouraheadtwo = (meanAnomstwo/57.3) +  3600;
        double meananomhouraheadstwo = meananomhouraheadtwo * 57.3;
//find true anomaly
        double taonehouraheadtwo = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomhouraheadstwo, Double.parseDouble(eccentwo));
        double angleonetwo = taonehouraheadtwo + meananomhouraheadstwo;
//find radius
        double radonehouraheadtwo = com.example.Sat2021.Satellites.findRadius(satTwoMean, Double.parseDouble(eccentwo), angleonetwo);

        double x1two = com.example.Sat2021.Satellites.getx(radonehouraheadtwo, raantwos , Double.parseDouble(inclintwo));
        double y1two = com.example.Sat2021.Satellites.gety(radonehouraheadtwo, raantwos, Double.parseDouble(inclintwo));
        double z1two = com.example.Sat2021.Satellites.getz(radonehouraheadtwo, raantwos, Double.parseDouble(inclintwo));

        String coordinate1two = com.example.Sat2021.Satellites.getCoordinate(x1two,y1two, z1two);
        models.addAttribute("onehouraheadcoordinatestwo", coordinate1two);
        models.addAttribute("radatonehouraheadtwo", m.format(radonehouraheadtwo));





//two hour from now:
//find mean anomaly
        double meananomtwohouraheadtwo = (meanAnomstwo/57.3) +  7200;
        double meananomtwohouraheadstwo = meananomtwohouraheadtwo * 57.3;
//find true anomaly
        double tatwohouraheadtwo = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomtwohouraheadstwo , Double.parseDouble(eccentwo));
        double angletwotwo = tatwohouraheadtwo + meananomtwohouraheadstwo;
//find radius
        double radtwohouraheadtwo = com.example.Sat2021.Satellites.findRadius(satTwoMean, Double.parseDouble(eccentwo), angletwotwo);


        double x2two = com.example.Sat2021.Satellites.getx(radtwohouraheadtwo, raantwos , Double.parseDouble(inclintwo));
        double y2two = com.example.Sat2021.Satellites.gety(radtwohouraheadtwo, raantwos, Double.parseDouble(inclintwo));
        double z2two = com.example.Sat2021.Satellites.getz(radtwohouraheadtwo, raantwos, Double.parseDouble(inclintwo));

        String coordinate2two = com.example.Sat2021.Satellites.getCoordinate(x2two, y2two, z2two);
        models.addAttribute("twohouraheadcoordinatestwo", coordinate2two);

        models.addAttribute("radtwohouraheadtwo", m.format(radtwohouraheadtwo));



        //five hour from now:
//find mean anomaly
        double meananomfivehouraheadtwo = (meanAnomstwo/57.3) +  18000;
        double meananomfivehouraheadstwo = meananomfivehouraheadtwo  * 57.3;
//find true anomaly
        double tafivehouraheadtwo = com.example.Sat2021.Satellites.returnTrueAnomaly(meananomfivehouraheadstwo, Double.parseDouble(eccentwo));
        double anglethreetwo = tafivehouraheadtwo + meananomfivehouraheadstwo;
//find radius
        double radfivehouraheadtwo = com.example.Sat2021.Satellites.findRadius(satTwoMean, Double.parseDouble(eccentwo), anglethreetwo);


        double x3two = com.example.Sat2021.Satellites.getx(radfivehouraheadtwo, raantwos , Double.parseDouble(inclintwo));
        double y3two = com.example.Sat2021.Satellites.gety(radfivehouraheadtwo, raantwos, Double.parseDouble(inclintwo));
        double z3two = com.example.Sat2021.Satellites.getz(radfivehouraheadtwo, raantwos, Double.parseDouble(inclintwo));

        String coordinate3two = com.example.Sat2021.Satellites.getCoordinate(x3two, y3two, z3two);
        models.addAttribute("fivehouraheadcoordinatestwo", coordinate3two);

        models.addAttribute("radfivehouraheadtwo", m.format(radfivehouraheadtwo));





        //five 24 from now:
//find mean anomaly
        double meananom24houraheadtwo = (meanAnomstwo/57.3) +  86400;
        double meananom24houraheadstwo = meananom24houraheadtwo  * 57.3;
//find true anomaly
        double ta24houraheadtwo = com.example.Sat2021.Satellites.returnTrueAnomaly(meananom24houraheadstwo, Double.parseDouble(eccentwo));
        double anglefourtwo = ta24houraheadtwo  + meananom24houraheadstwo;
//find radius
        double rad24houraheadtwo = com.example.Sat2021.Satellites.findRadius(satTwoMean, Double.parseDouble(eccentwo), anglefourtwo);

        double x4two = com.example.Sat2021.Satellites.getx(rad24houraheadtwo, raantwos , Double.parseDouble(inclintwo));
        double y4two = com.example.Sat2021.Satellites.gety(rad24houraheadtwo, raantwos, Double.parseDouble(inclintwo));
        double z4two = com.example.Sat2021.Satellites.getz(rad24houraheadtwo, raantwos, Double.parseDouble(inclintwo));

        String coordinate4two = com.example.Sat2021.Satellites.getCoordinate(x4two, y4two, z4two);
        models.addAttribute("twentyfourhouraheadcoordinatestwo", coordinate4two);


        models.addAttribute("rad24houraheadtwo", m.format(rad24houraheadtwo));

        //five 1 hour ago from now:
//find mean anomaly
        double meananom1houragotwo = (meanAnomstwo/57.3) - 3600;
        double meananom1houragostwo = meananom1houragotwo  * 57.3;
//find true anomaly
        double ta1behindtwo = com.example.Sat2021.Satellites.returnTrueAnomaly(meananom1houragostwo, Double.parseDouble(eccentwo));
        double anglefivetwo = ta1behindtwo  + meananom1houragostwo;
//find radius
        double rad1hourbehindtwo = com.example.Sat2021.Satellites.findRadius(satTwoMean, Double.parseDouble(eccentwo), anglefivetwo);



        double x5two = com.example.Sat2021.Satellites.getx(rad1hourbehindtwo, raantwos , Double.parseDouble(inclintwo));
        double y5two = com.example.Sat2021.Satellites.gety(rad1hourbehindtwo, raantwos, Double.parseDouble(inclintwo));
        double z5two = com.example.Sat2021.Satellites.getz(rad1hourbehindtwo, raantwos, Double.parseDouble(inclintwo));

        String coordinate5two = com.example.Sat2021.Satellites.getCoordinate(x5two, y5two, z5two);
        models.addAttribute("onehourbehindcoordinatestwo", coordinate5two);


        models.addAttribute("rad1hourbehindtwo", m.format(rad1hourbehindtwo));


        double distance = com.example.Sat2021.Satellites.distanceSatellite(x1, x1two, y1, y1two, z1, z1two);
        models.addAttribute("distance", m.format(distance));


double apogeetwo = com.example.Sat2021.Satellites.findAdistEarthToApogee(satTwoMean, Double.parseDouble(eccentwo));

       double inclins = Double.parseDouble(inclin);
       double eccens = Double.parseDouble(eccen);
       double meanMots = Double.parseDouble(meanMot);
       double meanAnoms = Double.parseDouble(meanAnom);


        models.addAttribute("inclin", m.format(inclins));
        models.addAttribute("eccen", m.format(eccens));
        models.addAttribute("meanMot", m.format(meanMots));
        models.addAttribute("meanAnom", m.format(meanAnoms));


        models.addAttribute("satnametwo", satNametwo);

        double inclintwos = Double.parseDouble(inclintwo);
        double eccentwos = Double.parseDouble(eccentwo);
        double meanMottwos = Double.parseDouble(meanMottwo);
        double meanAnomtwos = Double.parseDouble(meanAnomtwo);


        double apogee = com.example.Sat2021.Satellites.findAdistEarthToApogee(satOneMean, eccens);
        double apogeesplit = apogee/10;
        for(int i = 0; i < 10; i ++){
            models.addAttribute(("apog") + i, com.example.Sat2021.Satellites.findSatVelocityForGivenR((i * apogeesplit), satOneMean));
        }

        double apogeesplittwo = apogeetwo/10;
        for(int i = 1; i < 11; i ++){
            models.addAttribute(("apogs") + i, com.example.Sat2021.Satellites.findSatVelocityForGivenR((i * apogeesplittwo), satTwoMean));
        }


        models.addAttribute("inclintwo", m.format(inclintwos));
        models.addAttribute("eccentwo", m.format(eccentwos));
        models.addAttribute("meanMottwo", m.format(meanMottwos));
        models.addAttribute("meanAnomtwo", m.format(meanAnomtwos));

        models.addAttribute("a", m.format(satOneMean));
        models.addAttribute("atwo", m.format(satTwoMean));

        models.addAttribute("pone", m.format(pValSatOne));
        models.addAttribute("ptwo", m.format(pValSatTwo));

        models.addAttribute("hone", m.format(hOne));
        models.addAttribute("htwo", m.format(hTwo));

        models.addAttribute("apogeeone", m.format(AdistEarthToApogeeOne));
        models.addAttribute("apogeetwo", m.format(AdistEarthToApogeeTwo));

        models.addAttribute("perone", m.format(PdistEarthToPerigeeOne));
        models.addAttribute("pertwo", m.format(PdistEarthToPerigeeTwo));

        models.addAttribute("bone",m.format(bOne));
        models.addAttribute("btwo", m.format(bTwo));

        models.addAttribute("equaone",satOneEquation) ;
        models.addAttribute("equatwo", satTwoEquation);

        models.addAttribute("onevatperigee",m.format(voneatperigee));
        models.addAttribute("twovp", m.format(vtwoatperigee));
        models.addAttribute("onevatapogee",m.format(voneatapogee));
        models.addAttribute("twova", m.format(vtwoatapogee));


        models.addAttribute("vonefiftyplus",m.format(onevatrad50plus));
        models.addAttribute("vtwofiftyplus", m.format(twovatrad50plus));



        return "distpage";
    }

    public static double satDetailsHeight (double ec, double pval){
        com.example.Sat2021.Satellites satelliteOne = new com.example.Sat2021.Satellites(0,"hi",0,0,0,0,0,0,0,0,0,0,0,0);
        if (ec > 0 && ec< 1) {

           double hOne = satelliteOne.findHeightH(ec, pval);
           return hOne;
        }
        else if (ec == 1) {
            double hOne =  pval/2;
            return hOne;
        }
        else if (ec > 1) {
            double hOne = satelliteOne.findHeightH(ec, pval);
            return hOne;
        }
        else{
            double hOne = -1;
            return hOne;
        }


    }

    public static double satB (double ec, double pval){
        com.example.Sat2021.Satellites satelliteOne = new com.example.Sat2021.Satellites(0,"hi",0,0,0,0,0,0,0,0,0,0,0,0);

        if (ec > 0 && ec < 1) {

            double bOne = satelliteOne.findBValSecnarioOne(ec, pval);
            return bOne;
        }
        else if (ec == 1) {

            double bOne = 0;
            return bOne;

        }
        else if (ec > 1) {
            double bOne = satelliteOne.findBValScenarioTwo(ec, pval);
            return bOne;
        }
        else{
            double bOne = -1;
            return bOne;
        }
    }

    public static String returnEquation(double a,  double b){
        String equation = "x^2/" + Math.pow(a, 2) + " + y^2/" + Math.pow(b, 2) + " = 1";
        return equation;
    }

}
