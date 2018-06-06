package ch.hsr.adv.ui.core.presentation;

/**
 * Guice factory interface for
 * {@link ReplayViewModel}
 *
 * @author mwieland
 */
public interface ReplayViewModelFactory {

    /**
     * factory method
     *
     * @param stateViewModel    state viewmodel
     * @param steppingViewModel step viewmodel
     * @return instance
     */
    ReplayViewModel create(StateViewModel stateViewModel,
                           SteppingViewModel steppingViewModel);

}
