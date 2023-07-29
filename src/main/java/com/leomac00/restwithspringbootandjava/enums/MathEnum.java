package com.leomac00.restwithspringbootandjava.enums;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public enum MathEnum {
    SUM{
        @Override
        public Double calc(String a, String b) {
            return convertToDouble(a) + convertToDouble(b);
        }
    },
    SUBTRACTION{
        @Override
        public Double calc(String a, String b) {
            return convertToDouble(a) - convertToDouble(b);
        }
    },
    MULTIPLICATION{
        @Override
        public Double calc(String a, String b) {
            return convertToDouble(a) * convertToDouble(b);
        }
    },
    DIVISION{
        @Override
        public Double calc(String a, String b) {
            return convertToDouble(a) / convertToDouble(b);
        }
    },
    SQRT{
        @Override
        public Double calc(String a, String b) { return Math.sqrt(convertToDouble(a)); }
    };

    public abstract Number calc (String a, String b);


    private static Double convertToDouble(String strNumber) {
        if (strNumber == null) return 0d;
        String number = replaceCommas(strNumber);
        if (isNumeric(number)) return Double.parseDouble(number);
        return 1.0d;
    }
    private static String replaceCommas(String strNumber) {
        return strNumber.replaceAll(",", ".");
    }
    private static boolean isNumeric(String strNumber) {
        if (strNumber == null) return false;
        String number = replaceCommas(strNumber);
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }
}

