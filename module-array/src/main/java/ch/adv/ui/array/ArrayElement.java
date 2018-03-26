package ch.adv.ui.array;

import ch.adv.ui.logic.model.ADVElement;
import ch.adv.ui.logic.model.styles.ADVStyle;

/**
 * Represents one Item in a Array
 */
public class ArrayElement implements ADVElement {

    private long id;
    private ADVStyle style;
    private int fixedPosX;
    private int fixedPosY;
    private String content;

    @Override
    public long getElementId() {
        return id;
    }

    @Override
    public ADVStyle getStyle() {
        return style;
    }

    @Override
    public int getFixedPosX() {
        return fixedPosX;
    }

    @Override
    public int getFixedPosY() {
        return fixedPosY;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStyle(ADVStyle style) {
        this.style = style;
    }

    public void setFixedPosX(int fixedPosX) {
        this.fixedPosX = fixedPosX;
    }

    public void setFixedPosY(int fixedPosY) {
        this.fixedPosY = fixedPosY;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
