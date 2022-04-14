package com.dispensa.utilities;

public class Constants {

    private static Boolean isConfigured = false;

    public static Boolean getConfigured() {
        return isConfigured;
    }

    public static void setConfigured(Boolean configured) {
        isConfigured = configured;
    }
}
