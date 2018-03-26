package ch.adv.ui.logic.model;

import ch.adv.ui.logic.model.styles.ADVStyle;

public interface ADVElement<T> {

    long getElementId();

    ADVStyle getStyle();

    int getFixedPosX();

    int getFixedPosY();

    T getContent();
}
