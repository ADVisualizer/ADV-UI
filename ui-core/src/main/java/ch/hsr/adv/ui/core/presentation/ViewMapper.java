package ch.hsr.adv.ui.core.presentation;

import ch.hsr.adv.ui.core.logic.domain.Session;
import com.google.inject.Singleton;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps Sessions to their corresponding tab or stage.
 *
 * @author mtrentini
 */
@Singleton
public class ViewMapper {

    private final ObservableList<Session> sessionList;
    private final Map<Session, Tab> sessionToTapMap = new HashMap<>();
    private final Map<Tab, Session> tabToSessionMap = new HashMap<>();
    private final Map<Session, Stage> sessionToStageMap = new HashMap<>();
    private final Map<Stage, Session> stageToSessionMap = new HashMap<>();

    ViewMapper(ObservableList<Stage> stages, ObservableList<Tab>
            tabs, ObservableList<Session> sessionList) {
        this.sessionList = sessionList;
        stages.addListener(handleStageListChange());
        tabs.addListener(handleTabListChange());
    }

    private ListChangeListener<Stage> handleStageListChange() {
        return change -> {
            while (change.next()) {
                change.getAddedSubList().forEach(stage -> {
                    String sessionString = stage.getTitle();
                    sessionToStageMap
                            .put(getSession(sessionString), stage);
                    stageToSessionMap
                            .put(stage, getSession(sessionString));
                });
                change.getRemoved().forEach(stage -> {
                    Session s = stageToSessionMap.get(stage);
                    stageToSessionMap.remove(stage);
                    sessionToStageMap.remove(s);
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
        return sessionList.stream()
                .filter(session -> session.toString().equals(sessionString))
                .findFirst().orElse(null);
    }

    /**
     * Get associated stage to this session
     *
     * @param session to query for
     * @return the associated stage or null if no stage is present
     */
    public Stage getStage(Session session) {
        return sessionToStageMap.get(session);
    }

    /**
     * Get associated Tab to this session
     *
     * @param session to query for
     * @return the associated Tab or null if no tab is present
     */
    public Tab getTab(Session session) {
        return sessionToTapMap.get(session);
    }

    /**
     * Get associated Session to this Tab
     *
     * @param tab to query for
     * @return the associated Session or null if no session is present
     */
    public Session getSession(Tab tab) {
        return tabToSessionMap.get(tab);
    }
}
