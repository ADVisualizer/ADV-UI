package ch.hsr.adv.ui.tree.domain;

import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.commons.tree.logic.domain.ADVGeneralTreeNode;

import java.util.ArrayList;
import java.util.List;

public class GeneralTreeTestNode<T> implements ADVGeneralTreeNode<T> {

    private List<ADVGeneralTreeNode<T>> children;
    private ADVStyle style;
    private T content;

    public GeneralTreeTestNode(T content) {
        this(content, null);
    }

    public GeneralTreeTestNode(T content, ADVStyle style) {
        this.content = content;
        this.style = style;
        children = new ArrayList<>();
    }

    @Override
    public List<ADVGeneralTreeNode<T>> getChildren() {
        return children;
    }

    public void addChild(ADVGeneralTreeNode<T> child) {
        children.add(child);
    }

    @Override
    public ADVStyle getStyle() {
        return style;
    }

    @Override
    public T getContent() {
        return content;
    }
}
