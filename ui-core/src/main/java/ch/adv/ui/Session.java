package ch.adv.ui;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Session {

    private final Long id;
    private final String sessionName;
    private final String moduleName;
    private final List<Snapshot> snapshots;

    public Session(String moduleName, String sessionName) {
        this.id = Instant.now().toEpochMilli();
        this.moduleName = moduleName;
        this.sessionName = sessionName;
        this.snapshots = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getSessionName() {
        return sessionName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public List<Snapshot> getSnapshots() {
        return snapshots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        return Objects.equals(id, session.id) &&
                Objects.equals(sessionName, session.sessionName) &&
                Objects.equals(moduleName, session.moduleName) &&
                Objects.equals(snapshots, session.snapshots);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, sessionName, moduleName, snapshots);
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", sessionName='" + sessionName + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", snapshots=" + snapshots +
                '}';
    }
}
