package ch.adv.ui.core.presentation.sessionviewmodel;

/**
 * Guice factory interface for
 * {@link ch.adv.ui.core.presentation.sessionviewmodel.ReplayViewModel}
 */
public interface ReplayViewModelFactory {

    /**
     * factory method
     *
     * @param stateViewModel    state viewmodel
     * @param steppingViewModel step viewmodel
     * @return instance
     */
    ReplayViewModel create(StateViewModel stateViewModel, SteppingViewModel
            steppingViewModel);

}
