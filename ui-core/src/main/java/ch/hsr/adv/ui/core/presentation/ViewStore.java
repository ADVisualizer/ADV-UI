package ch.hsr.adv.ui.core.presentation;

import ch.hsr.adv.ui.core.logic.domain.Session;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.HashMap;
import java.util.Map;

public class ViewStore {

    private final ObservableList<Session> sessionList;
    private final Map<Session, Tab> sessionToTapMap = new HashMap<>();
    private final Map<Tab, Session> tabToSessionMap = new HashMap<>();
    private final Map<Session, Window> sessionToWindowMap = new HashMap<>();
    private final Map<Window, Session> windowToSessionMap = new HashMap<>();

    public ViewStore(ObservableList<Window> windows, ObservableList<Tab>
            tabs, ObservableList<Session> sessionList) {
        this.sessionList = sessionList;
        windows.addListener(handleWindowListChange());
        tabs.addListener(handleTabListChange());
    }

    private ListChangeListener<Window> handleWindowListChange() {
        return change -> {
            while (change.next()) {
                change.getAddedSubList().forEach(window -> {
                    if (!Tooltip.class.isAssignableFrom(window.getClass())) {
                        String sessionString = ((Stage) window).getTitle();
                        sessionToWindowMap
                                .put(getSession(sessionString), window);
                        windowToSessionMap
                                .put(window, getSession(sessionString));
                    }
                });
                change.getRemoved().forEach(window -> {
                    Session s = windowToSessionMap.get(window);
                    windowToSessionMap.remove(window);
                    sessionToWindowMap.remove(s);
                });
            }
        };
    }


    private ListChangeListener<Tab> handleTabListChange() {
        return change -> {
            while (change.next()) {
                change.getAddedSubList().forEach(tab -> {
                    sessionToTapMap.put(getSession(tab.getText()), tab);
                    tabToSessionMap.put(tab, getSession(tab.getText()));
                });
                change.getRemoved().forEach(tab -> {
                    Session s = tabToSessionMap.get(tab);
                    tabToSessionMap.remove(tab);
                    sessionToTapMap.remove(s);
                });
            }
        };
    }

    private Session getSession(String sessionString) {
        Session associatedSession = sessionList.stream()
                .filter(session -> session.toString().equals(sessionString))
                .findFirst().orElse(null);
        return associatedSession;
    }

    public Window getWindow(Session session) {
        return sessionToWindowMap.get(session);
    }

    public Window getWindow(String sessionString) {
        return sessionToWindowMap.get(getSession(sessionString));
    }

    public Tab getTab(Session session) {
        return sessionToTapMap.get(session);
    }

    public Tab getTab(String sessionString) {
        return sessionToTapMap.get(getSession(sessionString));
    }

    public Session getSession(Tab tab) {
        return tabToSessionMap.get(tab);
    }
}
