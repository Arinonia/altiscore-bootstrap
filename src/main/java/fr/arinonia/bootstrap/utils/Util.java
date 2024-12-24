package fr.arinonia.bootstrap.utils;

public class Util {

    public static int crossMult(final int value, final int maximum, final int coefficient) {
        return (int)((double)value / (double)maximum * (double)coefficient);
    }

}
