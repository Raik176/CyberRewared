package org.rhm.util;

public class CyberUtil {
    public static String prettyTime(long seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }
}
