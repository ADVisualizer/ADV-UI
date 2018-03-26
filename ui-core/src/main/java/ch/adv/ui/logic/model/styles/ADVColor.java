package ch.adv.ui.logic.model.styles;

public enum ADVColor {
    STANDARD("standard"), BLACK("black"), WHITE("white"), DARKGREY(
            "darkgrey"), GREY("grey"), LIGHTGREY("lightgrey"), BLUE(
            "blue"), LIGHTBLUE("lightblue"), RED(
            "red"), YELLOW("yellow"), ORANGE("orange"), GREEN("green");

    private String color;

    ADVColor(String color) {
        this.color = color.toLowerCase();
    }

    public String getColor() {
        return color;
    }

    public static ADVColor byName(String colorName) {
        return valueOf(colorName.toUpperCase());
    }
}
