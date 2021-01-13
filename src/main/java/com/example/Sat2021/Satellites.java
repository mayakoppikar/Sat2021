package com.example.Sat2021;


import java.text.DecimalFormat;

public class Satellites {

    private int satelliteId;
    private String satelliteName;
    private double epoch;
    private double derOneBallisticCoefficient;
    private double dragTerm;
    private double ephemerisNumber;
    private double elementNumber;
    private double inclination;
    private double rightAscension;
    private double eccentricity;
    private double argumentOfPerigee;
    private double meanAnomaly;
    private  double meanMotion;
    private double revolutionNumberAtEpoch;
    private double semiMajorAxisA;
    private double pValue;
    private double heightHValue;
    private double bValue;
    private double distEearthToApogee;
    private double distEearthToPerigee;


    public Satellites (int satelliteId, String satelliteName,
                       double epoch, double derOneBallisticCoefficient,
                       double dragTerm, double ephemerisNumber, double elementNumber, double inclination,
                       double rightAscension, double eccentricity, double argumentOfPerigee, double meanAnomaly,
                       double meanMotion, double revolutionNumberAtEpoch) {

        this.satelliteId = satelliteId;
        this.satelliteName = satelliteName;
        this.epoch = epoch;
        this.derOneBallisticCoefficient = derOneBallisticCoefficient;
        this.dragTerm = dragTerm;
        this.ephemerisNumber = ephemerisNumber;
        this.elementNumber = elementNumber;
        this.inclination = inclination;
        this.rightAscension = rightAscension;
        this.eccentricity = eccentricity;
        this.argumentOfPerigee = argumentOfPerigee;
        this.meanAnomaly = meanAnomaly;
        this.meanMotion = meanMotion;
        this.revolutionNumberAtEpoch = revolutionNumberAtEpoch;

        this.semiMajorAxisA = findSemiMajorAxisA(this.meanMotion);
        this.pValue = findPvalue(this.semiMajorAxisA, this.eccentricity);

        //find other values based on eccentricity

        if (this.eccentricity > 0 && this.eccentricity < 1) {
            this.heightHValue = findHeightH(this.eccentricity, this.pValue);
            this.bValue = findBValSecnarioOne(this.eccentricity, this.pValue);
        } else if (this.eccentricity == 1) {
            this.heightHValue = (this.pValue / 2);
        } else if (this.eccentricity > 1) {
            this.heightHValue = findHeightH(this.eccentricity, this.pValue);
            this.bValue = findBValScenarioTwo(this.eccentricity, this.pValue);
        } else {
            System.out.println("INVALID!!!!!!!!!!!!!!!!!!!!!!!");
        }

        this.distEearthToApogee = findAdistEarthToApogee(this.semiMajorAxisA, this.eccentricity);
        this.distEearthToPerigee = findPdistEarthToPerigee(this.semiMajorAxisA, this.eccentricity);
    }

    //CALCULATIONS METHODS

    public  static double findSemiMajorAxisA(double meanMotion){
        double m = 3.986004418 * Math.pow(10, 14);
        double halfOne = Math.cbrt(m);
        double twoPiMeanMotion = Math.PI * 2 * meanMotion;
        double next = twoPiMeanMotion/86400;
        double square = Math.pow(next, 2);
        double cubrt = Math.cbrt(square);
        double fin = (halfOne/cubrt)/1000;


        return fin;
    }

    public static double findPvalue(double a, double e){
        return ((-1 * a * Math.pow(e, 2)) + a);
    }

   public static double findHeightH(double ec, double pVal){
        return (pVal * Math.pow(ec, 2))/ (1-(Math.pow(ec, 2)));
    }

    public static double findBValSecnarioOne(double ec, double pVal){
        return (ec * pVal)/(Math.pow((1- (Math.pow(ec, 2))), 1/2));
    }

    public static double findBValScenarioTwo(double ec, double pVal){
        return (ec * pVal)/(Math.pow(((Math.pow(ec, 2)) -1 ), 1/2));
    }

    public static double findAdistEarthToApogee(double a, double ec){
        return ((a * ( 1+ ec))- 6371);
    }

    public static double findPdistEarthToPerigee(double a, double ec){
        return ((a * ( 1 - ec)) - 6371);
    }




    public static double findSatVelocityForGivenR(double r, double a){
        double G = .0000000000667;
        double M = 5.972 * Math.pow(10, 24);
        return Math.sqrt(G * M * ((2 / r) - (1 / a)));
    }

    public static double findVatApogee(double ra, double a){
        double G = .0000000000667;
        double M = 5.972 * Math.pow(10, 24);
        double first = (2/ra) - (1/a);
        double second = first * M * G;
        double fin = Math.sqrt(second);
        return fin;
    }

    public static double findVatPerigee(double rp, double a){
        double G = .0000000000667;
        double M = 5.972 * Math.pow(10, 24);
        double first = (2/rp) - (1/a);
        double second = first * M * G;
        double fin = Math.sqrt(second);
        return fin;
    }





        public static double returnTrueAnomaly(double meananom, double eccentricity){

            double meananoms = meananom/57.3;
        double v = 180 * (2 * eccentricity * (Math.sin(meananoms)));
            double vv = v/(Math.PI);

            return vv;
        }

public static double findRadius(double a, double e, double theta){
       double ee = Math.pow(e, 2);
        double ptone = a * (1- ee);
        double pttwo = 1 + (e * Math.cos((theta/57.3)));
        double r = ptone/pttwo;
        return r;

}




public static double distanceSatellite (double x1, double x2, double y1, double y2, double z1, double z2){
        double ptone = Math.pow((x2-x1), 2);
        double pttwo = Math.pow((y2-y2), 2);
        double ptthree = Math.pow((z2-z2), 2);
        double add = ptone + pttwo + ptthree;
        double fina = Math.sqrt(add);
        return fina;
}








public static double getx(double r, double vpo, double i) {
   double X = r * (Math.cos(vpo/57.3) * Math.cos(vpo/57.3) - Math.sin(vpo/57.3) * Math.sin(vpo/57.3) *
            Math.cos(i/57.3));

return X;
}

public static double gety(double r, double vpo, double i) {
      double Y = r * (Math.sin(vpo/57.3) * Math.cos(vpo/57.3) + Math.cos(vpo/57.3) * Math.sin(vpo/57.3) *
                Math.cos(i/57.3));

        return Y;
}

    public static double getz(double r, double vpo, double i) {
       double Z = r * (Math.sin(vpo/57.3) * Math.sin(i/57.3));

        return Z;
    }



public static String getCoordinate(double x, double y, double z){
    DecimalFormat d = new DecimalFormat("#####.###");
String s = "[" + d.format(x) + ", " + d.format(y) + ", " + d.format(z) + "]";
return s;

}

public static double period(double meanmotion){
        return (1/((meanmotion/24)/3600));
}

public static double getSatelliteAltitude(double mm){
        double period = Satellites.period(mm);
   double o = Math.cbrt((6.675 * Math.pow(10,-11) * 5.927 * Math.pow(10,24) * (Math.pow(period, 2)))/(4 * Math.pow((Math.PI),2)));
   double one = o - 6400;

return  one/1000;
}

public static double findOrbitalPeriod(double meanmot){
    double xone = ((meanmot/24)/60);
    double fin = 1/xone;
    return fin;
}

































    //GETTERS AND SETTERS
    public int getSatelliteId() {
        return satelliteId;
    }

    public void setSatelliteId(int satelliteId) {
        this.satelliteId = satelliteId;
    }

    public String getSatelliteName() {
        return satelliteName;
    }

    public void setSatelliteName(String satelliteName) {
        this.satelliteName = satelliteName;
    }



    public double getEpoch() {
        return epoch;
    }

    public void setEpoch(double epoch) {
        this.epoch = epoch;
    }

    public double getRevolutionNumberAtEpoch() {
        return revolutionNumberAtEpoch;
    }

    public void setRevolutionNumberAtEpoch(double revolutionNumberAtEpoch) {
        this.revolutionNumberAtEpoch = revolutionNumberAtEpoch;
    }

    public double getMeanMotion() {
        return meanMotion;
    }

    public void setMeanMotion(double meanMotion) {
        this.meanMotion = meanMotion;
    }

    public double getMeanAnomaly() {
        return meanAnomaly;
    }

    public void setMeanAnomaly(double meanAnomaly) {
        this.meanAnomaly = meanAnomaly;
    }

    public double getArgumentOfPerigee() {
        return argumentOfPerigee;
    }

    public void setArgumentOfPerigee(double argumentOfPerigee) {
        this.argumentOfPerigee = argumentOfPerigee;
    }

    public double getEccentricity() {
        return eccentricity;
    }

    public void setEccentricity(double eccentricity) {
        this.eccentricity = eccentricity;
    }

    public double getInclination() {
        return inclination;
    }

    public void setInclination(double inclination) {
        this.inclination = inclination;
    }

    public double getRightAscension() {
        return rightAscension;
    }

    public void setRightAscension(double rightAscension) {
        this.rightAscension = rightAscension;
    }

    public double getDragTerm() {
        return dragTerm;
    }

    public double getElementNumber() {
        return elementNumber;
    }

    public void setElementNumber(double elementNumber) {
        this.elementNumber = elementNumber;
    }

    public double getEphemerisNumber() {
        return ephemerisNumber;
    }

    public void setEphemerisNumber(double ephemerisNumber) {
        this.ephemerisNumber = ephemerisNumber;
    }

    public void setDragTerm(double dragTerm) {
        this.dragTerm = dragTerm;
    }

    public double getDerOneBallisticCoefficient() {
        return derOneBallisticCoefficient;
    }

    public void setDerOneBallisticCoefficient(double derOneBallisticCoefficient) {
        this.derOneBallisticCoefficient = derOneBallisticCoefficient;
    }

    public double getSemiMajorAxisA() {
        return semiMajorAxisA;
    }

    public void setSemiMajorAxisA(double semiMajorAxisA) {
        this.semiMajorAxisA = semiMajorAxisA;
    }

    public double getpValue() {
        return pValue;
    }

    public void setpValue(double pValue) {
        this.pValue = pValue;
    }

    public double getHeightHValue() {
        return heightHValue;
    }

    public void setHeightHValue(double heightHValue) {
        this.heightHValue = heightHValue;
    }

    public double getbValue() {
        return bValue;
    }

    public void setbValue(double bValue) {
        this.bValue = bValue;
    }

}
