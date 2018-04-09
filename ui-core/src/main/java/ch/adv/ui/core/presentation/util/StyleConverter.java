package ch.adv.ui.core.presentation.util;

import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;

import java.util.HashMap;

/**
 * Creates JavaFX BorderStrokeStyle Enums from Strings.
 */
public class StyleConverter {
    private static final HashMap<String, BorderStrokeStyle> map = new
            HashMap<>();

    static {
        map.put("dotted", BorderStrokeStyle.DOTTED);
        map.put("dashed", BorderStrokeStyle.DASHED);
        map.put("solid", BorderStrokeStyle.SOLID);
        map.put("none", BorderStrokeStyle.NONE);
    }

    /**
     * @param name of the desired enum
     * @return the corresponding enum from the input string
     */
    public static BorderStrokeStyle getStrokeStyle(String name) {
        return map.get(name);
    }

    /**
     * @param colorValue to be converted
     * @return JavaFX Color of the input value
     */
    public static Color getColor(int colorValue) {
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
}
