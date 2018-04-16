package ch.adv.ui.core.presentation.sessionviewmodel;

/**
 * Guice factory interface for
 * {@link ch.adv.ui.core.presentation.sessionviewmodel.SteppingViewModel}
 */
public interface SteppingViewModelFactory {

    /**
     * factory method
     *
     * @param stateViewModel state view model
     * @return instance
     */
    SteppingViewModel create(StateViewModel stateViewModel);
}
