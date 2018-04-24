package ch.adv.ui.core.logic;

import ch.adv.ui.core.logic.domain.Session;

import java.util.List;

public interface SessionStore {

    List<Session> getAll();

    Session get(long id);

    void add(Session object);

    Session getCurrent();

    void setCurrent(long id);

    void delete(long id);

    void clear();

    boolean contains(long id);
}
