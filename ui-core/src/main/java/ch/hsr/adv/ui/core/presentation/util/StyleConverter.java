package ch.hsr.adv.ui.core.presentation.util;

import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;

import java.util.HashMap;

/**
 * Creates JavaFX BorderStrokeStyle Enums from Strings.
 *
 * @author mtrentini
 */
public class StyleConverter {
    private static final HashMap<String, BorderStrokeStyle> MAP = new
            HashMap<>();

    static {
        MAP.put("dotted", BorderStrokeStyle.DOTTED);
        MAP.put("dashed", BorderStrokeStyle.DASHED);
        MAP.put("solid", BorderStrokeStyle.SOLID);
        MAP.put("none", BorderStrokeStyle.NONE);
    }

    /**
     * @param name of the desired enum
     * @return the corresponding enum from the input string
     */
    public static BorderStrokeStyle getStrokeStyle(String name) {
        return MAP.get(name.toLowerCase());
    }

    /**
     * @param colorValue to be converted
     * @return JavaFX Color of the input value
     */
    public static Color getColorFromHexValue(int colorValue) {
        String hex = Integer.toHexString(colorValue);
        int hexColorLength = 6;
        int difference = hexColorLength - hex.length();
        if (difference > 0) {
            String addedZeros = "";
            for (int i = 0; i < difference; i++) {
                addedZeros += "0";
            }
            hex = addedZeros + hex;
        }
        return Color.web(hex);
    }

    /**
     * Return either black or white depending on the brightness of the input
     * color.
     *
     * @param color
     *          reference color
     * @return either black or white depending on the brightness of the input
     *         color
     */
    public static Color getLabelColor(Color color) {
        double brightness = color.getBrightness();
        if (brightness >= 0.90) {
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }

}
