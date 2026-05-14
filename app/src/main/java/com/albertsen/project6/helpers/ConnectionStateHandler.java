package com.albertsen.project6.helpers;

public class ConnectionStateHandler {

    private static State popupState = State.Pending;
    private static String fingerprint;

    public static int getPopupState() {
        return popupState.ordinal();
    }

    public static void setPopupState(State popupState) {
        ConnectionStateHandler.popupState = popupState;
    }

    public static String getFingerprint() {
        return fingerprint;
    }

    public static void setFingerprint(String fingerprint) {
        ConnectionStateHandler.fingerprint = fingerprint;
    }
}
