package de.cyklon.snayolo.util;

public interface WaveBase {

    default int getIntValue(int wave) {
        return 0;
    }

    default double getDoubleValue(int wave) {
        return 0;
    }

    default boolean getBoolValue(int wave) {
        return false;
    }

    default Object getValue(int wave) {
        return new Object();
    }


}
