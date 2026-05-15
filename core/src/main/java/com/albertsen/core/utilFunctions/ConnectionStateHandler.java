package com.albertsen.core.utilFunctions;

import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectionStateHandler {

    private static State popupState = State.Pending;
    private static String fingerprint;

    public static AtomicBoolean connectionOnGoing;

    public static boolean closeListner = false;

    public static final Object popupLock = new Object();

    public static State getPopupState() {
        return popupState;
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
