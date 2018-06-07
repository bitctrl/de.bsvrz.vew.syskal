package de.bsvrz.vew.syskal;

public class Fehler {
    public enum FehlerType {
        VERWEIS, COMMON;
    }

    private final FehlerType type;
    private final String message;

    private Fehler(FehlerType type, String message) {
        this.type = type;
        this.message = message;
    }

    public static Fehler common(String message) {
        return new Fehler(FehlerType.COMMON, message);
    }

    public static Fehler verweis(String message) {
        return new Fehler(FehlerType.VERWEIS, message);
    }
    
    public FehlerType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
    
    @Override
    public String toString() {
        return type + ": " + message;
    }
}
