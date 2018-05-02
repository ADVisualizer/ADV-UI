package ch.hsr.adv.ui.graph.logic.domain;

import ch.adv.ui.core.logic.domain.ADVElement;
import ch.adv.ui.core.logic.domain.styles.ADVStyle;

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
        return 0;
    }

    @Override
    public ADVStyle getStyle() {
        return null;
    }

    @Override
    public int getFixedPosX() {
        return 0;
    }

    @Override
    public int getFixedPosY() {
        return 0;
    }

    @Override
    public String getContent() {
        return null;
    }
}
