package ch.adv.ui.stack.logic.domain;

import ch.adv.ui.core.logic.domain.ADVElement;
import ch.adv.ui.core.logic.domain.styles.ADVStyle;
import ch.adv.ui.core.logic.domain.styles.presets.ADVDefaultStyle;

/**
 * Represents one item in a Stack
 */
public class StackElement implements ADVElement<String> {

    private long id;
    private int fixedPosX;
    private int fixedPosY;
    private String content;
    private ADVStyle style = new ADVDefaultStyle();

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
