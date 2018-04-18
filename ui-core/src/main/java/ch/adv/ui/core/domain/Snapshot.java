package ch.adv.ui.core.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents the state of a data structure in the user's module
 * implementation. A snapshot always belongs to a session. It is sent to the
 * ADV UI to be displayed.
 */
public class Snapshot {

    private static final transient AtomicLong SNAPSHOT_COUNTER = new
            AtomicLong(0);
    private final long snapshotId = SNAPSHOT_COUNTER.incrementAndGet();

    private String snapshotDescription;
    private List<ADVElement> elements = new ArrayList<>();
    private List<ADVRelation> relations = new ArrayList<>();


    public long getSnapshotId() {
        return snapshotId;
    }

    public String getSnapshotDescription() {
        return snapshotDescription;
    }

    public void setSnapshotDescription(String snapshotDescription) {
        this.snapshotDescription = snapshotDescription;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Snapshot snapshot = (Snapshot) o;
        return snapshotId == snapshot.snapshotId
                && Objects.equals(snapshotDescription, snapshot
                .snapshotDescription)
                && elements.size() == snapshot.elements.size()
                && relations.size() == snapshot.relations.size();

    }

    @Override
    public int hashCode() {
        return Objects.hash(snapshotId, snapshotDescription, elements,
                relations);
    }
}
