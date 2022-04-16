package de.beisser.po;

import java.util.TimeZone;

public final class PhotoOrganizerConstants {

    private PhotoOrganizerConstants() {
    }

    public static final String TIME_ZONE_BERLIN_STRING = "Europe/Berlin";
    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone(TIME_ZONE_BERLIN_STRING);
    public static final String SOURCE_OPTION_SHORT = "s";
    public static final String TARGET_OPTION_SHORT = "t";
    public static final String TARGET_OPTION_UNKNOWN_DATE_SHORT = "ud";
    public static final String SOURCE_OPTION_LONG = "source";
    public static final String TARGET_OPTION_LONG = "target";
    public static final String TARGET_OPTION_UNKNOWN_DATE_LONG = "unknownDate";
}
