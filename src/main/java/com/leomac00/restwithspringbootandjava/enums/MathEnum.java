package com.leomac00.restwithspringbootandjava.enums;

import java.lang.Math;
import java.util.ArrayList;

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
            return convertToDouble(a) + convertToDouble(b);
        }
    },
    MULTIPLICATION{
        @Override
        public Double calc(String a, String b) {
            return convertToDouble(a) + convertToDouble(b);
        }
    },
    DIVISION{
        @Override
        public Double calc(String a, String b) {
            return convertToDouble(a) + convertToDouble(b);
        }
    },
    SQRT{
        @Override
        public Object calc(String a, String b) {
            ArrayList results = new ArrayList<>();
            results.add(Math.sqrt(convertToDouble(a)));
            results.add(Math.sqrt(convertToDouble(b)));

            return results;
        }
    };

    public abstract Object calc (String a, String b);

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
