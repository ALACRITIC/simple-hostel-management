package org.remipassmoilesel.bookme.utils.colors;


import org.remipassmoilesel.bookme.utils.Utils;

import java.awt.*;

public class DefaultColor {

    private final Color color;
    private final String name;

    public DefaultColor(String name, String hexStr) {
        this.color = Utils.hexToColor(hexStr);
        this.name = name;
    }

    public DefaultColor(String name, Color color) {
        this.color = color;
        this.name = name;
    }

    public String toHexadecimal() {
        return Utils.colorToHex(color);
    }

    public String toRgbString() {
        return Utils.colorToRgbString(color);
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }
}