package ch.adv.ui.core.presentation;

import ch.adv.ui.core.presentation.sessionviewmodel.StateViewModel;
import ch.adv.ui.core.presentation.sessionviewmodel.SteppingViewModel;

/**
 * Factory interface for Guice
 *
 * @see https://github.com/google/guice/wiki/AssistedInject
 */
public interface SessionReplayFactory {

    /**
     * Construct a SessionReplay with dependencies, which are not
     * automatically injectable.
     *
     * @param stateViewModel    StateViewModel of corresponding Session
     * @param steppingViewModel SteppingViewModel of corresponding Session
     * @return new instance of SessionReplay
     */
    SessionReplay create(StateViewModel stateViewModel, SteppingViewModel
            steppingViewModel);
}

