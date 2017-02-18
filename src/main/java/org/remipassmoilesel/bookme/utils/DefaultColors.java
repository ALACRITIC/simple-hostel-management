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

    }


    public static void includeColors(Model model) {
        model.addAttribute("defaultColors", colors);
    }

    private static final List<DefaultColor> colors = Arrays.asList(
            new DefaultColor("AliceBlue", "#F0F8FF"),
            new DefaultColor("AntiqueWhite", "#FAEBD7"),
            new DefaultColor("Aqua", "#00FFFF"),
            new DefaultColor("Aquamarine", "#7FFFD4"),
            new DefaultColor("Azure", "#F0FFFF"),
            new DefaultColor("Beige", "#F5F5DC"),
            new DefaultColor("Bisque", "#FFE4C4"),
            new DefaultColor("Black", "#000000"),
            new DefaultColor("BlanchedAlmond", "#FFEBCD"),
            new DefaultColor("Blue", "#0000FF"),
            new DefaultColor("BlueViolet", "#8A2BE2"),
            new DefaultColor("Brown", "#A52A2A"),
            new DefaultColor("BurlyWood", "#DEB887"),
            new DefaultColor("CadetBlue", "#5F9EA0"),
            new DefaultColor("Chartreuse", "#7FFF00"),
            new DefaultColor("Chocolate", "#D2691E"),
            new DefaultColor("Coral", "#FF7F50"),
            new DefaultColor("CornflowerBlue", "#6495ED"),
            new DefaultColor("Cornsilk", "#FFF8DC"),
            new DefaultColor("Crimson", "#DC143C"),
            new DefaultColor("Cyan", "#00FFFF"),
            new DefaultColor("DarkBlue", "#00008B"),
            new DefaultColor("DarkCyan", "#008B8B"),
            new DefaultColor("DarkGoldenRod", "#B8860B"),
            new DefaultColor("DarkGray", "#A9A9A9"),
            new DefaultColor("DarkGrey", "#A9A9A9"),
            new DefaultColor("DarkGreen", "#006400"),
            new DefaultColor("DarkKhaki", "#BDB76B"),
            new DefaultColor("DarkMagenta", "#8B008B"),
            new DefaultColor("DarkOliveGreen", "#556B2F"),
            new DefaultColor("DarkOrange", "#FF8C00"),
            new DefaultColor("DarkOrchid", "#9932CC"),
            new DefaultColor("DarkRed", "#8B0000"),
            new DefaultColor("DarkSalmon", "#E9967A"),
            new DefaultColor("DarkSeaGreen", "#8FBC8F"),
            new DefaultColor("DarkSlateBlue", "#483D8B"),
            new DefaultColor("DarkSlateGray", "#2F4F4F"),
            new DefaultColor("DarkSlateGrey", "#2F4F4F"),
            new DefaultColor("DarkTurquoise", "#00CED1"),
            new DefaultColor("DarkViolet", "#9400D3"),
            new DefaultColor("DeepPink", "#FF1493"),
            new DefaultColor("DeepSkyBlue", "#00BFFF"),
            new DefaultColor("DimGray", "#696969"),
            new DefaultColor("DimGrey", "#696969"),
            new DefaultColor("DodgerBlue", "#1E90FF"),
            new DefaultColor("FireBrick", "#B22222"),
            new DefaultColor("FloralWhite", "#FFFAF0"),
            new DefaultColor("ForestGreen", "#228B22"),
            new DefaultColor("Fuchsia", "#FF00FF"),
            new DefaultColor("Gainsboro", "#DCDCDC"),
            new DefaultColor("GhostWhite", "#F8F8FF"),
            new DefaultColor("Gold", "#FFD700"),
            new DefaultColor("GoldenRod", "#DAA520"),
            new DefaultColor("Gray", "#808080"),
            new DefaultColor("Grey", "#808080"),
            new DefaultColor("Green", "#008000"),
            new DefaultColor("GreenYellow", "#ADFF2F"),
            new DefaultColor("HoneyDew", "#F0FFF0"),
            new DefaultColor("HotPink", "#FF69B4"),
            new DefaultColor("IndianRed ", "#CD5C5C"),
            new DefaultColor("Indigo ", "#4B0082"),
            new DefaultColor("Ivory", "#FFFFF0"),
            new DefaultColor("Khaki", "#F0E68C"),
            new DefaultColor("Lavender", "#E6E6FA"),
            new DefaultColor("LavenderBlush", "#FFF0F5"),
            new DefaultColor("LawnGreen", "#7CFC00"),
            new DefaultColor("LemonChiffon", "#FFFACD"),
            new DefaultColor("LightBlue", "#ADD8E6"),
            new DefaultColor("LightCoral", "#F08080"),
            new DefaultColor("LightCyan", "#E0FFFF"),
            new DefaultColor("LightGoldenRodYellow", "#FAFAD2"),
            new DefaultColor("LightGray", "#D3D3D3"),
            new DefaultColor("LightGrey", "#D3D3D3"),
            new DefaultColor("LightGreen", "#90EE90"),
            new DefaultColor("LightPink", "#FFB6C1"),
            new DefaultColor("LightSalmon", "#FFA07A"),
            new DefaultColor("LightSeaGreen", "#20B2AA"),
            new DefaultColor("LightSkyBlue", "#87CEFA"),
            new DefaultColor("LightSlateGray", "#778899"),
            new DefaultColor("LightSlateGrey", "#778899"),
            new DefaultColor("LightSteelBlue", "#B0C4DE"),
            new DefaultColor("LightYellow", "#FFFFE0"),
            new DefaultColor("Lime", "#00FF00"),
            new DefaultColor("LimeGreen", "#32CD32"),
            new DefaultColor("Linen", "#FAF0E6"),
            new DefaultColor("Magenta", "#FF00FF"),
            new DefaultColor("Maroon", "#800000"),
            new DefaultColor("MediumAquaMarine", "#66CDAA"),
            new DefaultColor("MediumBlue", "#0000CD"),
            new DefaultColor("MediumOrchid", "#BA55D3"),
            new DefaultColor("MediumPurple", "#9370DB"),
            new DefaultColor("MediumSeaGreen", "#3CB371"),
            new DefaultColor("MediumSlateBlue", "#7B68EE"),
            new DefaultColor("MediumSpringGreen", "#00FA9A"),
            new DefaultColor("MediumTurquoise", "#48D1CC"),
            new DefaultColor("MediumVioletRed", "#C71585"),
            new DefaultColor("MidnightBlue", "#191970"),
            new DefaultColor("MintCream", "#F5FFFA"),
            new DefaultColor("MistyRose", "#FFE4E1"),
            new DefaultColor("Moccasin", "#FFE4B5"),
            new DefaultColor("NavajoWhite", "#FFDEAD"),
            new DefaultColor("Navy", "#000080"),
            new DefaultColor("OldLace", "#FDF5E6"),
            new DefaultColor("Olive", "#808000"),
            new DefaultColor("OliveDrab", "#6B8E23"),
            new DefaultColor("Orange", "#FFA500"),
            new DefaultColor("OrangeRed", "#FF4500"),
            new DefaultColor("Orchid", "#DA70D6"),
            new DefaultColor("PaleGoldenRod", "#EEE8AA"),
            new DefaultColor("PaleGreen", "#98FB98"),
            new DefaultColor("PaleTurquoise", "#AFEEEE"),
            new DefaultColor("PaleVioletRed", "#DB7093"),
            new DefaultColor("PapayaWhip", "#FFEFD5"),
            new DefaultColor("PeachPuff", "#FFDAB9"),
            new DefaultColor("Peru", "#CD853F"),
            new DefaultColor("Pink", "#FFC0CB"),
            new DefaultColor("Plum", "#DDA0DD"),
            new DefaultColor("PowderBlue", "#B0E0E6"),
            new DefaultColor("Purple", "#800080"),
            new DefaultColor("RebeccaPurple", "#663399"),
            new DefaultColor("Red", "#FF0000"),
            new DefaultColor("RosyBrown", "#BC8F8F"),
            new DefaultColor("RoyalBlue", "#4169E1"),
            new DefaultColor("SaddleBrown", "#8B4513"),
            new DefaultColor("Salmon", "#FA8072"),
            new DefaultColor("SandyBrown", "#F4A460"),
            new DefaultColor("SeaGreen", "#2E8B57"),
            new DefaultColor("SeaShell", "#FFF5EE"),
            new DefaultColor("Sienna", "#A0522D"),
            new DefaultColor("Silver", "#C0C0C0"),
            new DefaultColor("SkyBlue", "#87CEEB"),
            new DefaultColor("SlateBlue", "#6A5ACD"),
            new DefaultColor("SlateGray", "#708090"),
            new DefaultColor("SlateGrey", "#708090"),
            new DefaultColor("Snow", "#FFFAFA"),
            new DefaultColor("SpringGreen", "#00FF7F"),
            new DefaultColor("SteelBlue", "#4682B4"),
            new DefaultColor("Tan", "#D2B48C"),
            new DefaultColor("Teal", "#008080"),
            new DefaultColor("Thistle", "#D8BFD8"),
            new DefaultColor("Tomato", "#FF6347"),
            new DefaultColor("Turquoise", "#40E0D0"),
            new DefaultColor("Violet", "#EE82EE"),
            new DefaultColor("Wheat", "#F5DEB3"),
            new DefaultColor("White", "#FFFFFF"),
            new DefaultColor("WhiteSmoke", "#F5F5F5"),
            new DefaultColor("Yellow", "#FFFF00"),
            new DefaultColor("YellowGreen", "#9ACD32")
            );
    
}
