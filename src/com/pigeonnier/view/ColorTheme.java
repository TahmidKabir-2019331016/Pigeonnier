package com.pigeonnier.view;

public enum ColorTheme {
    Light, Default, Dark;

    public static String getPath(ColorTheme colorTheme) {
        return switch (colorTheme) {
            case Default -> "css/themeDefault.css";
            case Dark -> "css/themeDark.css";
            case Light -> "css/themeLight.css";
        };
    }
}
