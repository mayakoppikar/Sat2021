package com.example.Sat2021;

public class FilterForm {

    boolean alphabeticorder;
    String altitudezone;

    public FilterForm(){
        super();
    }

    public boolean isAlphabeticorder() {
        return alphabeticorder;
    }

    public void setAlphabeticorder(boolean alphabeticorder) {
        this.alphabeticorder = alphabeticorder;
    }

    public String getAltitudezone() {
        return altitudezone;
    }

    public void setAltitudezone(String altitudezone) {
        this.altitudezone = altitudezone;
    }
}
