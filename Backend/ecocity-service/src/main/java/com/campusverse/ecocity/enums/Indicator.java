package com.campusverse.ecocity.enums;

/** Pollutants and noise measured by the stations, with the unit each one uses. */
public enum Indicator {
    PM2_5("µg/m³"),
    PM10("µg/m³"),
    NO2("µg/m³"),
    CO2("ppm"),
    NOISE_DB("dB");

    private final String unit;

    Indicator(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
}
