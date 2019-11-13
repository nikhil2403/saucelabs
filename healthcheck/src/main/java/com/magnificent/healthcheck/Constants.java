package com.magnificent.healthcheck;

public class Constants {
    public static boolean isIsServiceDown() {
        return isServiceDown;
    }

    public static void setIsServiceDown(boolean isServiceDown) {
        Constants.isServiceDown = isServiceDown;
    }

    private static boolean isServiceDown;
}
