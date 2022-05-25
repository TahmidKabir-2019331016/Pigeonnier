package com.pigeonnier.view;

public enum FontSize {
    Small, Default, Big;

    public static String getPath(FontSize fontSize) {
        return switch (fontSize) {
            case Default -> "css/fontDefault.css";
            case Small -> "css/fontSmall.css";
            case Big -> "css/fontBig.css";
        };
    }
}


