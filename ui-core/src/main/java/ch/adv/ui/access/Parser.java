package ch.adv.ui.access;

import ch.adv.ui.logic.model.Session;

public interface Parser {

    Session parse(String json);
}
