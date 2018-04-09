package ch.adv.ui.array;

import ch.adv.ui.core.domain.ADVElement;
import ch.adv.ui.core.domain.styles.ADVStyle;
import ch.adv.ui.core.domain.styles.presets.ADVDefaultStyle;

/**
 * Represents one Item in a Array
 */
public class ArrayElement implements ADVElement {

    private long id;
    private ADVStyle style;
    private int fixedPosX;
    private int fixedPosY;
    private String content;

    public ArrayElement() {
        this.style = new ADVDefaultStyle();
    }

    @Override
    public long getElementId() {
        return id;
    }

    @Override
    public ADVStyle getStyle() {
        return style;
    }

    public void setStyle(ADVStyle style) {
        this.style = style;
    }

    @Override
    public int getFixedPosX() {
        return fixedPosX;
    }

    public void setFixedPosX(int fixedPosX) {
        this.fixedPosX = fixedPosX;
    }

    @Override
    public int getFixedPosY() {
        return fixedPosY;
    }

    public void setFixedPosY(int fixedPosY) {
        this.fixedPosY = fixedPosY;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(long id) {
        this.id = id;
    }
}
