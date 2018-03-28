package ch.adv.ui.logic.model.styles;

/**
 * Represents a stroke thickness
 * <p>
 * Available options: STANDARD, BOLD, SLIGHT, FAT
 */
public enum ADVStrokeThickness {

    STANDARD("standard"), BOLD("bold"), SLIGHT("slight"), FAT("fat");

    private String thickness;

    ADVStrokeThickness(String thickness) {
        this.thickness = thickness.toLowerCase();
    }

    public String getThickness() {
        return thickness;
    }

    /**
     * Cases insensitive lookup for the stroke thickness
     *
     * @param ticknessName name
     * @return stroke thickness
     */
    public static ADVStrokeThickness byName(String ticknessName) {
        return valueOf(ticknessName.toUpperCase());
    }
}
