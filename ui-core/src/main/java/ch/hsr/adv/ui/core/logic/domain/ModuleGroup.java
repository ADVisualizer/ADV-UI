package ch.hsr.adv.ui.core.logic.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * A module group wraps all module specific elements and allows us to address
 * multiple modules in a snapshot.
 */
public class ModuleGroup {

    private String moduleName;
    private List<ADVElement> elements = new ArrayList<>();
    private List<ADVRelation> relations = new ArrayList<>();

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * Adds a element to the snapshot
     *
     * @param element element to add
     */
    public void addElement(ADVElement<?> element) {
        elements.add(element);
    }

    /**
     * Adds a relation to the snapshot
     *
     * @param relation relation to add
     */
    public void addRelation(ADVRelation relation) {
        relations.add(relation);
    }

    public List<ADVElement> getElements() {
        return elements;
    }

    public List<ADVRelation> getRelations() {
        return relations;
    }


}
