package org.remipassmoilesel.bookme.utils;

import org.springframework.ui.Model;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by remipassmoilesel on 18/02/17.
 */
public class DefaultColors {

    public static class DefaultColor {

        private final Color color;
        private final String name;

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

    }

    private static final List<DefaultColor> colors = Arrays.asList(
            new DefaultColor("Blue", Color.BLUE),
            new DefaultColor("Red", Color.RED),
            new DefaultColor("Green", Color.GREEN)
    );

    public static void includeColors(Model model) {
        model.addAttribute("defaultColors", colors);
    }

}
