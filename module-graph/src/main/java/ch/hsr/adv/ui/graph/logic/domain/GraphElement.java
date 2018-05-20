package ch.hsr.adv.ui.graph.logic.domain;

import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.ui.core.logic.domain.ADVElement;

/**
 * Object adapter. Maps an ADVVertex to an ADVElement.
 */
public class GraphElement implements ADVElement<String> {
    private long id;
    private String content;
    private ADVStyle style;
    private int fixedPosX;
    private int fixedPosY;

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
