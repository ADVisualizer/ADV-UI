package ch.adv.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.inject.Inject;

public class RootViewModel {

    private ObservableList<String> availableSessions;

    @Inject
    public  RootViewModel(SessionStore sessionStore) {
        this.availableSessions = FXCollections.observableArrayList();
        loadSessionView();
    }

    private void loadSessionView() {
        //TODO Fill with socket Data
        availableSessions.add("Array 1");
        availableSessions.add("Graph 1");
        availableSessions.add("Array 2");
    }

    public ObservableList<String> getAvailableSessions() {
        return availableSessions;
    }
}
